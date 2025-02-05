const passwordInput = document.querySelector("#password");
const passwordToggle = document.querySelector(".password-toggle")
const passwordToggleIcon = document.querySelector(".password-toggle-icon");

passwordToggle.addEventListener("click", () => {
    const isPassword = passwordInput.type == "password";
    passwordInput.type = isPassword ? "text" : "password";

    passwordToggleIcon.classList.toggle("bi-eye-slash");
    passwordToggleIcon.classList.toggle("bi-eye");
})

const passwordAckInput = document.querySelector("#password-ack");
const passwordAckToggle = document.querySelector(".password-ack-toggle")
const passwordAckToggleIcon = document.querySelector(".password-ack-toggle-icon");

passwordAckToggle.addEventListener("click", () => {
    const isPassword = passwordAckInput.type == "password";
    passwordAckInput.type = isPassword ? "text" : "password";

    passwordAckToggleIcon.classList.toggle("bi-eye-slash");
    passwordAckToggleIcon.classList.toggle("bi-eye");
})

const passwordRegisterInput = document.querySelector("#password-register");
const passwordRegisterToggle = document.querySelector(".password-register-toggle")
const passwordRegisterToggleIcon = document.querySelector(".password-register-toggle-icon");

passwordRegisterToggle.addEventListener("click", () => {
    const isPassword = passwordRegisterInput.type == "password";
    passwordRegisterInput.type = isPassword ? "text" : "password";

    passwordRegisterToggleIcon.classList.toggle("bi-eye-slash");
    passwordRegisterToggleIcon.classList.toggle("bi-eye");
})


const row = document.querySelector(".row");
const registerButton = document.querySelector(".btn-register");
const loginButton = document.querySelector(".btn-login");

registerButton.addEventListener("click", () => {
    row.classList.add("active")
})

loginButton.addEventListener("click", () => {
    row.classList.remove("active")
})
