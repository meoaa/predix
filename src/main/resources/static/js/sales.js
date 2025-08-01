const monthInput = document.querySelector("#type_month");
   const weekInput = document.querySelector("#type_week");
   const inputBox = document.querySelector("#input_box");
   const inputItemBox = document.querySelector("#input_item_box");
   const btnBox = document.querySelector("#btn_box");

   monthInput.addEventListener("click", () => {
     inputItemBox.innerHTML = "";
     btnBox.innerHTML = "";

     const addBtn = document.createElement("button");
     const submitBtn = document.createElement("button");
     const typeInput = document.createElement("input");

     addBtn.textContent = "추가";
     submitBtn.textContent = "제출";

     typeInput.type = "hidden";
     typeInput.value = "MONTH";
     typeInput.name = "sales_type";

     initMonthInput();

     btnBox.appendChild(addBtn);
     btnBox.appendChild(submitBtn);

     inputItemBox.appendChild(typeInput);

     addBtn.addEventListener("click", initMonthInput);

     submitBtn.addEventListener("click", () => {
       if (confirm("제출 하시겠습니까?")) {
         submitData();
       }
     });
   });

   weekInput.addEventListener("click", () => {
     inputItemBox.innerHTML = "";
     btnBox.innerHTML = "";

     const addBtn = document.createElement("button");
     addBtn.textContent = "추가";

     const submitBtn = document.createElement("button");
     submitBtn.textContent = "제출";

     const typeInput = document.createElement("input");
     typeInput.type = "hidden";
     typeInput.value = "WEEK";
     typeInput.name = "sales_type";

     initWeekInput();

     btnBox.appendChild(addBtn);
     btnBox.appendChild(submitBtn);
     inputItemBox.appendChild(typeInput);

     addBtn.addEventListener("click", initWeekInput);

     submitBtn.addEventListener("click", () => {
       if (confirm("제출 하시겠습니까?")) {
         submitData();
       }
     });
   });

   function submitData() {
     const salesData = [];
     const inputRows = inputItemBox.querySelectorAll(".input_row");
     const salesType = inputItemBox.querySelector(
       "input[name='sales_type']"
     ).value;

     let checkValue = true;
     inputRows.forEach((row) => {
       const startDate = row.querySelector("input[name='start_date']").value;
       const endDate = row.querySelector("input[name='end_date']").value;
       const amount = row.querySelector("input[name='amount']").value;
       const label = row.querySelector(".flatpickr-input").value;

       if (!startDate || !endDate || !amount) {
         checkValue = false;
       }

       if (amount) {
         salesData.push({
           startDate,
           endDate,
           amount: parseFloat(amount),
           type: salesType,
           label
         });
       }
     });

     const duplicateDate = hasDuplicateStartDates(salesData);

     if (!checkValue) {
       alert("모든 값을 입력해주세요.");
       return;
     } else if (duplicateDate) {
       alert("중복된 기간이 존재합니다.");
       return;
     }

     const jsonData = JSON.stringify(salesData);

     $.ajax({
       url: "/api/sales",
       method: "POST",
       contentType: "application/json",
       data: jsonData,
       dataType: "json",
       success: function(response){
        if(response.success){
          window.location.href="/chart?added";
        }

       },
       error: function(error){
        console.error(error);
       }
     });
   }

   function initMonthInput() {
     const row = document.createElement("div");
     row.className = "input_row";

     const startDateInput = document.createElement("input");
     startDateInput.type = "hidden";
     startDateInput.name = "start_date";

     const endDateInput = document.createElement("input");
     endDateInput.type = "hidden";
     endDateInput.name = "end_date";

     const monthInput = document.createElement("input");
     monthInput.type = "text";
     monthInput.placeholder = "월 선택";
     monthInput.className = "month-picker";

     const inputTag = document.createElement("input");
     inputTag.type = "number";
     inputTag.name = "amount";
     inputTag.placeholder = "매출액을 입력해주세요.";
     inputTag.addEventListener("focus", function(event){
         const target = event.target;
         const monthInput = target.previousElementSibling;
         if(monthInput.value == ""){
          showToast("월을 먼저 선택해주세요.","red","bottom");
          target.blur();
          monthInput.focus();
          return;
         }
         // 2. 현재 row가 마지막 row인지 확인
    // inputItemBox의 모든 input_row를 가져와 현재 row가 마지막인지 판단합니다.
    const allInputRows = inputItemBox.querySelectorAll(".input_row");
    const isLastRow = target.closest(".input_row") === allInputRows[allInputRows.length - 1];

    // 3. 현재 row의 월 값을 기반으로 다음 달 계산
    // monthInput.value는 "YYYY년 M월" 형식입니다 (예: "2025년 7월").
    const currentMonthStr = monthInput.value;
    const yearMatch = currentMonthStr.match(/(\d{4})년/);
    const monthMatch = currentMonthStr.match(/(\d{1,2})월/);

    if (!yearMatch || !monthMatch) {
        console.error("월 형식 파싱 오류:", currentMonthStr);
        return; // 유효한 월 형식이 아니면 더 진행하지 않습니다.
    }

    const currentYear = parseInt(yearMatch[1], 10);
    const currentMonth = parseInt(monthMatch[1], 10); // 1-12월

    // 다음 달 계산: JavaScript Date 객체의 월은 0-11입니다.
    // 다음 달이 12월(11)을 넘어가면 다음 해 1월(0)이 됩니다.
    let nextMonth = currentMonth; // 1-12월 기준
    let nextYear = currentYear;

    if (nextMonth === 12) { // 현재 12월이면 다음 해 1월로
        nextMonth = 1;
        nextYear += 1;
    } else { // 현재 12월이 아니면 그냥 다음 달로
        nextMonth += 1;
    }

    // 다음 달의 Date 객체 생성 (일자는 첫째 날로 고정)
    // Date 객체 월은 0부터 시작하므로 nextMonth - 1
    const nextDateObj = new Date(nextYear, nextMonth - 1, 1);

    // flatpickr의 dateFormat에 맞춰 다음 달 문자열 생성 ("YYYY년 M월")
    const nextMonthFormatted = `${nextYear}년 ${nextMonth}월`;

    // 중복된 기간이 있는지 확인하는 함수
    // salesData와 유사하게 현재 존재하는 모든 input_row의 월 값을 확인해야 합니다.
    const isMonthDuplicate = (checkMonthStr) => {
        let isDuplicate = false;
        allInputRows.forEach(row => {
            const rowMonthInput = row.querySelector(".month-picker");
            if (rowMonthInput && rowMonthInput.value === checkMonthStr) {
                isDuplicate = true;
            }
        });
        return isDuplicate;
    };


    // 4. 로직 실행 조건: 현재 포커스된 매출액 input이 마지막 row에 속하고,
    //    다음 달 값이 아직 없는 경우에만 새 row 추가
    if (isLastRow && !isMonthDuplicate(nextMonthFormatted)) {
        // 새로운 input_row를 추가합니다.
        initMonthInput(); // 새로운 input_row를 DOM에 추가

        // 새로 추가된 row를 찾습니다.
        // 현재 inputItemBox의 마지막 자식 (가장 최근에 추가된 row)이 새로 추가된 row입니다.
        const newRow = inputItemBox.lastElementChild;
        const newMonthInput = newRow.querySelector(".month-picker");

        // 새로 추가된 monthInput 필드에 다음 달 값을 설정합니다.
        if (newMonthInput) {
            // flatpickr 인스턴스를 직접 업데이트
            // flatpickr는 setValue 메서드를 제공합니다.
            // flatpickr 인스턴스에 접근해야 합니다. monthInput에 flatpickr를 연결할 때 반환되는 인스턴스를 저장해두면 좋습니다.
            // 하지만 현재 구조에서는 flatpickr가 자동으로 초기화되므로, 단순히 value를 설정해도 flatpickr가 인식합니다.
            newMonthInput.value = nextMonthFormatted;

            // start_date와 end_date도 함께 업데이트
            const newStartDateInput = newRow.querySelector("input[name='start_date']");
            const newEndDateInput = newRow.querySelector("input[name='end_date']");

            if (newStartDateInput && newEndDateInput) {
                const { firstDay, lastDay } = getMonthRange(nextDateObj); // 다음 달의 범위 계산
                newStartDateInput.value = formatDateToYYYYMMDD(firstDay);
                newEndDateInput.value = formatDateToYYYYMMDD(lastDay);
            }
        }
    }

     })

     const deleteBtn = document.createElement("button");
     deleteBtn.textContent = "제거";
     deleteBtn.addEventListener("click", deleteContent);

     row.appendChild(startDateInput);
     row.appendChild(endDateInput);
     row.appendChild(monthInput);
     row.appendChild(inputTag);
     row.appendChild(deleteBtn);

     inputItemBox.appendChild(row);

     flatpickr(monthInput, {
       dateFormat: "Y년 n월",
       locale: "ko",
       onChange: function ([selectedDate]) {
         if (!selectedDate) return;
         const { firstDay, lastDay } = getMonthRange(selectedDate);
         startDateInput.value = formatDateToYYYYMMDD(firstDay);
         endDateInput.value = formatDateToYYYYMMDD(lastDay);
       },
     });
   }

   function initWeekInput() {
     const row = document.createElement("div");
     row.className = "input_row";

     const startDateInput = document.createElement("input");
     startDateInput.type = "hidden";
     startDateInput.name = "start_date";

     const endDateInput = document.createElement("input");
     endDateInput.type = "hidden";
     endDateInput.name = "end_date";

     const weekInput = document.createElement("input");
     weekInput.type = "text";
     weekInput.placeholder = "주 선택";
     weekInput.className = "week-picker";

     const inputTag = document.createElement("input");
     inputTag.type = "number";
     inputTag.name = "amount";
     inputTag.placeholder = "매출액을 입력해주세요.";

     inputTag.addEventListener("focus",function(event){
        const target = event.target;
        const currentWeekInput = target.previousElementSibling;

        if(!currentWeekInput.value){
          showToast("주를 먼저 선택해주세요.", "red","bottom");
          target.blur();
          weekInput.focus();
          return;
        }
        const allInputRows = inputItemBox.querySelectorAll(".input_row");
        const isLastRow = target.closest(".input_row") === allInputRows[allInputRows.length - 1];

        if(isLastRow){
          // 현재 주차 입력 필드에서 연도와 주차 번호를 추출합니다 (예: "2025년 30주차").
          const currentWeekStr = currentWeekInput.value;
          const yearMatch = currentWeekStr.match(/(\d{4})년/);
          const weekMatch = currentWeekStr.match(/(\d{1,2})주차/);

          if (!yearMatch || !weekMatch) {
            console.error("주차 형식 파싱 오류:", currentWeekStr);
            return;
          }

          const currentYear = parseInt(yearMatch[1], 10);
          const currentWeek = parseInt(weekMatch[1], 10);

          // 다음 주차를 계산합니다.
          let nextWeek = currentWeek + 1;
          let nextYear = currentYear;

          // 연도 변경 처리 (현재 주차가 연도의 마지막 주차인 경우)
          // 여기서는 최대 52주 또는 53주라고 가정합니다. 정확한 계산을 위해서는 해당 연도의 총 주차 수를 알아야 합니다.
          if (nextWeek > 52) { // 대부분의 경우 52주를 가정합니다. 필요에 따라 조정하세요.
              nextWeek = 1;
              nextYear += 1;
          }

          const nextWeekFormatted = `${nextYear}년 ${nextWeek}주차`;

          // 중복된 주차가 있는지 확인합니다.
          const isWeekDuplicate = (checkWeekStr) => {
              let isDuplicate = false;
              allInputRows.forEach(row => {
                  const rowWeekInput = row.querySelector(".week-picker");
                  if (rowWeekInput && rowWeekInput.value === checkWeekStr) {
                      isDuplicate = true;
                  }
              });
              return isDuplicate;
          };

          if (!isWeekDuplicate(nextWeekFormatted)) {
              initWeekInput(); // 새로운 행을 생성합니다.

              const newRow = inputItemBox.lastElementChild;
              const newWeekInput = newRow.querySelector(".week-picker");
              const newStartDateInput = newRow.querySelector("input[name='start_date']");
              const newEndDateInput = newRow.querySelector("input[name='end_date']");
              const newDateTag = newRow.querySelector("p"); // 새로운 행의 p 태그를 가져옵니다.


              if (newWeekInput) {
                  // 새로운 주차 입력 필드의 값을 설정합니다.
                  newWeekInput.value = nextWeekFormatted;

                  // 현재 행의 시작 날짜를 기준으로 다음 주차의 시작 및 종료 날짜를 계산합니다.
                  const currentStartDateStr = startDateInput.value; // 현재 행의 시작 날짜
                  if (currentStartDateStr) {
                      const currentStart = new Date(currentStartDateStr);
                      const nextWeekStart = new Date(currentStart);
                      nextWeekStart.setDate(currentStart.getDate() + 7); // 다음 주차의 시작일

                      const nextWeekEnd = new Date(nextWeekStart);
                      nextWeekEnd.setDate(nextWeekStart.getDate() + 6); // 다음 주차의 종료일

                      newStartDateInput.value = formatDateToYYYYMMDD(nextWeekStart);
                      newEndDateInput.value = formatDateToYYYYMMDD(nextWeekEnd);
                      newDateTag.innerText = `${newStartDateInput.value} ~ ${newEndDateInput.value}`;
                  }
              }
          }
        }
     })

     const delBtn = document.createElement("button");
     delBtn.textContent = "제거";
     delBtn.addEventListener("click", deleteContent);

     const dateTag = document.createElement("p");

     row.appendChild(weekInput);
     row.appendChild(inputTag);
     row.appendChild(delBtn);
     row.appendChild(dateTag);

     row.appendChild(startDateInput);
     row.appendChild(endDateInput);

     inputItemBox.appendChild(row);

     flatpickr(weekInput, {
       locale: "ko",
       plugins: [new weekSelect({})],
       dateFormat: "Y년 W주차",
       onChange: function ([selectedDate], dateStr) {
         if (!selectedDate) return;

         const start = new Date(selectedDate);
         const day = start.getDay();
         const sunday = new Date(start);
         sunday.setDate(start.getDate() - day);
         const saturday = new Date(sunday);
         saturday.setDate(sunday.getDate() + 6);
         const formattedStartDate = formatDateToYYYYMMDD(sunday);
         const formattedEndDate = formatDateToYYYYMMDD(saturday);

         startDateInput.value = formattedStartDate;
         endDateInput.value = formattedEndDate;

         dateTag.innerText = `${formattedStartDate} ~ ${formattedEndDate}`;
       },
     });
   }

   function deleteContent(e) {
     const target = e.target.closest("div");
     target.remove();
   }

   function getMonthRange(date) {
     const year = date.getFullYear();
     const month = date.getMonth();

     const firstDay = new Date(year, month, 1);
     const lastDay = new Date(year, month + 1, 0);
     return { firstDay, lastDay };
   }

   function formatDateToYYYYMMDD(dateObj) {
     const year = dateObj.getFullYear();
     const month = String(dateObj.getMonth() + 1).padStart(2, "0");
     const day = String(dateObj.getDate()).padStart(2, "0");
     return `${year}-${month}-${day}`;
   }

   function hasDuplicateStartDates(salesData) {
     const seenStartDates = new Set();

     for (const item of salesData) {
       const startDate = item.startDate;

       if (seenStartDates.has(startDate)) {
         return true;
       }

       seenStartDates.add(startDate);
     }

     return false;
   }
