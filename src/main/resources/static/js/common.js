import{ setupLogout } from './auth.js'

export function showToast(message, color) {
  const background = color;
  Toastify({
    text: message,
    duration: 3000,
    gravity: "top",
    position: "center",
    stopOnFocus: true,
    style: {
      background
    }
  }).showToast();
}

document.addEventListener("DOMContentLoaded", function() {
  setupLogout();

});