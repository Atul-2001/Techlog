const browserReport = browserReportSync();

function createHiddenFormInput(name, value) {
    const input = document.createElement("input");
    input.type = 'hidden';
    input.name = name;
    input.value = value;

    return input;
}

const verificationAlert = document.querySelector("#verification-alert");
const emailForm = document.querySelector("#email_form");
const otpForm = document.querySelector("#otp_form");
const passwordForm = document.querySelector("#password_form");
const footer = document.querySelector(".footer");

emailForm.appendChild(createHiddenFormInput("os", `${browserReport.browser.name} - ${browserReport.browser.version}`));
emailForm.appendChild(createHiddenFormInput("browser", `${browserReport.os.name} ${browserReport.os.version}`));
emailForm.appendChild(createHiddenFormInput("timestamp", `${browserReport.timestamp}`));

window.addEventListener("orientationchange", function () {
    if (window.matchMedia("(max-height: 510px)").matches) {
        footer.classList.remove("fixed-bottom");
    } else if (!otpForm.classList.contains("d-none") && window.matchMedia("(max-height: 690px)").matches) {
        footer.classList.remove("fixed-bottom");
    }
}, false);

const password_reset_carousel = document.querySelector("#password_reset_carousel");
const carousel = new bootstrap.Carousel(password_reset_carousel);

carousel.pause();

const timer = document.querySelector("#timer");
const emailField = emailForm.elements.namedItem("email_field");
const verifyEmailBtn = emailForm.elements.namedItem("verifyEmailBtn");

verifyEmailBtn.disabled = !emailField.value;
emailField.addEventListener('input', function () {
    verifyEmailBtn.disabled = !emailField.value;

    if (verifyEmailBtn.innerText === 'Resend OTP') {
        verifyEmailBtn.innerText = 'Send password reset code';
        timer.classList.add("d-none");
        verificationAlert.classList.add("d-none");
        otpForm.classList.add("d-none");

        if (otpForm.classList.contains("d-none") && !window.matchMedia("(max-height: 690px)").matches) {
            footer.classList.add("fixed-bottom");
        }
    }
});

emailForm.addEventListener('submit', function (event) {
    event.preventDefault();

    if (!verifyEmailBtn.disabled) {
        const spinner = verifyEmailBtn.children[0];

        verifyEmailBtn.disabled = true;
        spinner.classList.toggle("d-none");

        verificationAlert.classList.remove("d-none", "alert-success", "alert-danger", "alert-warning");
        verificationAlert.classList.toggle("d-none");

        fetch('/auth/verify/email', {
            method: "POST",
            body: new URLSearchParams(new FormData(event.target).entries())
        })
            .then(response => response.json())
            .then(result => {
                verificationAlert.innerHTML = result.content;
                if (result.level === Level.INFO) {
                    verificationAlert.classList.toggle("alert-success");
                    setTimeout(function () {
                        startTimer(verifyEmailBtn);
                        otpForm.appendChild(createHiddenFormInput("email", emailField.value));
                        otpForm.classList.remove("d-none");
                        if (!otpForm.classList.contains("d-none") && window.matchMedia("(max-height: 690px)").matches) {
                            footer.classList.remove("fixed-bottom");
                        }
                    }, 1000);
                } else if (result.level === Level.WARN) {
                    verificationAlert.classList.toggle("alert-warning");
                } else if (result.level === Level.ERROR) {
                    verificationAlert.classList.toggle("alert-danger");
                } else {
                    verificationAlert.classList.toggle("alert-danger");
                    verificationAlert.innerHTML = "Something went wrong, please try again!";
                }

                verificationAlert.classList.toggle("d-none");

                verifyEmailBtn.disabled = false;
                spinner.classList.toggle("d-none");
            }).catch(error => {
                console.error(error);
                verificationAlert.classList.toggle("alert-warning");
                verificationAlert.innerHTML = "Something went wrong, please try again!";
                verificationAlert.classList.toggle("d-none");
                verifyEmailBtn.disabled = false;
                spinner.classList.toggle("d-none");
            });
    }
});

function startTimer(button) {
    button.disabled = true;
    const TOTAL_TIME = 180; //seconds
    let TIME_LEFT = TOTAL_TIME;
    let TIME_PASSED = 0;

    const intervalID = setInterval(function () {
        timer.innerText = formatTime(TIME_LEFT);
        TIME_PASSED += 1;
        TIME_LEFT = TOTAL_TIME - TIME_PASSED;

        if (TIME_LEFT < 0) {
            clearInterval(intervalID);
            button.disabled = false;
            button.innerText = "Resend OTP"
        }
    }, 1000);

    timer.classList.remove("d-none");
}

function formatTime(time) {
    // The largest round integer less than or equal to the result of time divided being by 60.
    let minutes = Math.floor(time / 60);

    // If the value of minutes is less than 10, then display minutes with a leading zero
    if (minutes < 10) {
        minutes = `0${minutes}`;
    }

    // Seconds are the remainder of the time divided by 60 (modulus operator)
    let seconds = time % 60;

    // If the value of seconds is less than 10, then display seconds with a leading zero
    if (seconds < 10) {
        seconds = `0${seconds}`;
    }

    // The output in MM:SS format
    return `${minutes}:${seconds}`;
}

const otpField = otpForm.elements.namedItem("otp_field");
const verifyOTPBtn = otpForm.elements.namedItem("verifyOtpBtn");

otpField.addEventListener('input', function() {
    verifyOTPBtn.disabled = !otpField.value;
});

otpForm.addEventListener('submit', function(event) {
    event.preventDefault();

    if (!verifyOTPBtn.disabled) {
        const spinner = verifyOTPBtn.children[0];

        verifyOTPBtn.disabled = true;
        spinner.classList.toggle("d-none");

        verificationAlert.classList.remove("d-none", "alert-success", "alert-danger", "alert-warning");
        verificationAlert.classList.toggle("d-none");

        fetch('/auth/validate/otp', {
            method: "POST",
            body: new URLSearchParams(new FormData(event.target).entries())
        })
            .then(response => response.json())
            .then(result => {
                verificationAlert.innerHTML = result.content;
                if (result.level === Level.INFO) {
                    verificationAlert.classList.toggle("alert-success");
                    setTimeout(function () {
                        passwordForm.appendChild(createHiddenFormInput("email", new FormData(event.target).get("email")));
                        carousel.next();
                    }, 1000);
                } else if (result.level === Level.WARN) {
                    verificationAlert.classList.toggle("alert-warning");
                } else if (result.level === Level.ERROR) {
                    verificationAlert.classList.toggle("alert-danger");
                } else {
                    verificationAlert.classList.toggle("alert-danger");
                    verificationAlert.innerHTML = "Something went wrong, please try again!";
                }

                verificationAlert.classList.toggle("d-none");

                verifyOTPBtn.disabled = false;
                spinner.classList.toggle("d-none");
            }).catch(error => {
                console.error(error);
                verificationAlert.classList.toggle("alert-warning");
                verificationAlert.innerHTML = "Something went wrong, please try again!";
                verificationAlert.classList.toggle("d-none");
                verifyOTPBtn.disabled = false;
                spinner.classList.toggle("d-none");
            });
    }
});

const password = passwordForm.elements.namedItem("password_field");
const repeatPassword = passwordForm.elements.namedItem("repeat_password_field");

const checkPassword = document.querySelector("#CheckPassword");
const checkPasswordLabel = document.querySelector("#CheckPasswordLabel");

checkPassword.addEventListener('click', function () {
    if (password.type === "password" && repeatPassword.type === "password") {
        password.type = "text";
        repeatPassword.type = "text";
        checkPasswordLabel.innerText = "Hide Password"
    } else {
        password.type = "password";
        repeatPassword.type = "password";
        checkPasswordLabel.innerText = "Show Password"
    }
});

const passwordResetAlert = document.querySelector("#password-reset-alert");
const passwordResetBtn = document.querySelector("#resetPasswordBtn");

password.addEventListener('input', function () {
    passwordResetBtn.disabled = !password.value || !password.value;
});

repeatPassword.addEventListener('input', function () {
    passwordResetBtn.disabled = !password.value || !password.value;
});

passwordForm.addEventListener('submit', function (event) {
    event.preventDefault();

    if (!passwordResetBtn.disabled) {
        const spinner = passwordResetBtn.children[0];

        passwordResetBtn.disabled = true;
        spinner.classList.toggle("d-none");

        passwordResetAlert.classList.remove("d-none", "alert-success", "alert-danger", "alert-warning");
        passwordResetAlert.classList.toggle("d-none");

        fetch('/user/password/reset', {
            method: "POST",
            body: new URLSearchParams(new FormData(event.target).entries())
        })
            .then(response => response.json())
            .then(result => {
                passwordResetAlert.innerHTML = result.content;
                if (result.level === Level.INFO) {
                    passwordResetAlert.classList.toggle("alert-success");
                    setTimeout(function () {
                        window.location.href = result.redirectURI;
                    }, 1000);
                } else if (result.level === Level.WARN) {
                    passwordResetAlert.classList.toggle("alert-warning");
                } else if (result.level === Level.ERROR) {
                    passwordResetAlert.classList.toggle("alert-danger");
                } else {
                    passwordResetAlert.classList.toggle("alert-danger");
                    passwordResetAlert.innerHTML = "Something went wrong, please try again!";
                }

                passwordResetAlert.classList.toggle("d-none");

                passwordResetBtn.disabled = false;
                spinner.classList.toggle("d-none");
            }).catch(error => {
                console.error(error);
                passwordResetAlert.classList.toggle("alert-warning");
                passwordResetAlert.innerHTML = "Something went wrong, please try again!";
                passwordResetAlert.classList.toggle("d-none");
                passwordResetBtn.disabled = false;
                spinner.classList.toggle("d-none");
            });
    }
});