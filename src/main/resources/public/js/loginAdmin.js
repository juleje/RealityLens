const btn_register = document.getElementById("registreer");
const btn_login = document.getElementById("meldaan");

if (btn_register !== null) {
    btn_register.addEventListener("click", function () {
        window.location.href = '/register';
    })
}

if (btn_login !== null) {
    btn_login.addEventListener("click", function () {
        window.location.href = '/login';
    })
}





