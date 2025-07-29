window.showToast = function(message, color, gravity) {
  const background = color;
  Toastify({
    text: message,
    duration: 3000,
    gravity: gravity, // 'top' 또는 'bottom'
    position: "center", // 'left', 'center', 'right'
    stopOnFocus: true, // 토스트가 활성 상태일 때 자동 숨김 방지
    style: {
      background
    }
  }).showToast();
};