function setupLogout(){
    const logoutBtn = document.querySelector(".logout");
       if(logoutBtn){
            logoutBtn.addEventListener("click",()=>{
                  $.ajax({
                    url : "/api/auth/logout",
                    method : "post",
                    success : function(response){
                      // 전역 showToast 함수 사용
                      window.showToast("로그아웃 되었습니다.", "linear-gradient(to right, #00b09b, #96c93d)");
                      location.href ="/login?logout";
                    },
                    error: function(xhr, status, error){
                      console.error("로그아웃 실패:", error);
                      // 전역 showToast 함수 사용
                      window.showToast("로그아웃 실패: " + (xhr.responseJSON && xhr.responseJSON.message ? xhr.responseJSON.message : "알 수 없는 오류"), "linear-gradient(to right, #ff5f6d, #ffc371)");
                    }
                  })
                })
            }
       }

// auth.js가 로드되면 바로 setupLogout을 실행하도록 합니다.
document.addEventListener("DOMContentLoaded", function() {
  setupLogout();
});