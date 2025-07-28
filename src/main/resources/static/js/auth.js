export function setupLogout(){
    const logoutBtn = document.querySelector(".logout");
       if(logoutBtn){
            logoutBtn.addEventListener("click",()=>{
                  $.ajax({
                    url : "/api/auth/logout",
                    method : "post",
                    success : function(response){
                      location.href ="/login?logout";
                    },
                    error: function(error){
                      console.log(error);
                    }
                  })
                })
            }
       }

