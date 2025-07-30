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

          if (!startDate || !endDate || !amount) {
            checkValue = false;
          }

          if (amount) {
            salesData.push({
              startDate,
              endDate,
              amount: parseFloat(amount),
              type: salesType,
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
          url: "/api/sales/create",
          method: "POST",
          contentType: "application/json",
          data: jsonData,
          dataType: "json",
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
        inputTag.addEventListener("focus",function(){
            console.log("heelo");
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
          dateFormat: "Y년 m월",
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