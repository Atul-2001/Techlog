const browserReport = browserReportSync();

function createHiddenFormInput(name, value) {
    const input = document.createElement("input");
    input.type = 'hidden';
    input.name = name;
    input.value = value;

    return input;
}

const verificationDialog = document.querySelector("#verifyEmailModal");
if (verificationDialog) {
    verificationDialog.addEventListener('hidden.bs.modal', function () {
        if (window.neededReload) {
            window.location.reload();
            window.neededReload = false;
            new bootstrap.Modal(verificationDialog).dispose();
        }
    });

    const verificationAlert = document.querySelector("#verification-alert");
    if (verificationAlert) {
        const alertMsg = document.querySelector(".alert-msg");
        const emailForm = document.querySelector("#email-form");
        const otpForm = document.querySelector("#otp-form");

        emailForm.appendChild(createHiddenFormInput("os", `${browserReport.browser.name} - ${browserReport.browser.version}`));
        emailForm.appendChild(createHiddenFormInput("browser", `${browserReport.os.name} ${browserReport.os.version}`));
        emailForm.appendChild(createHiddenFormInput("timestamp", `${browserReport.timestamp}`));

        const timer = document.querySelector("#timer");

        emailForm.addEventListener('submit', function (event) {
            event.preventDefault();

            if (!verifyEmailBtn.disabled) {
                const verifyEmailBtn = emailForm.elements.namedItem("verifyEmailBtn");
                const spinner = verifyEmailBtn.children[0];

                verifyEmailBtn.disabled = true;
                spinner.classList.toggle("d-none");

                verificationAlert.classList.remove("d-none", "flash-info", "flash-warn", "flash-error");
                verificationAlert.classList.add("d-none");

                fetch('/auth/verify/email', {
                    method: "POST",
                    body: new URLSearchParams(new FormData(event.target).entries())
                })
                    .then(response => response.json())
                    .then(result => {
                        alertMsg.innerText = result.content;
                        if (result.level === Level.INFO) {
                            verificationAlert.classList.add("flash-info");
                            setTimeout(function () {
                                startTimer(verifyEmailBtn);
                                otpForm.classList.remove("d-none");
                            }, 1000);
                        } else if (result.level === Level.WARN) {
                            verificationAlert.classList.add("flash-warn");
                        } else if (result.level === Level.ERROR) {
                            verificationAlert.classList.add("flash-error");
                        } else {
                            verificationAlert.classList.add("flash-error");
                            alertMsg.innerText = "Something went wrong, please try again!";
                        }

                        verificationAlert.classList.remove("d-none");

                        verifyEmailBtn.disabled = false;
                        spinner.classList.toggle("d-none");
                    }).catch(error => {
                    console.error(error);
                    verificationAlert.classList.add("flash-error");
                    alertMsg.innerText = "Something went wrong, please try again!";
                    verificationAlert.classList.remove("d-none");
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
                    timer.classList.add("d-none");
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

        const otpField = otpForm.elements.namedItem("otp-field");
        const verifyOTPBtn = otpForm.elements.namedItem("verifyOtpBtn");

        otpField.addEventListener('input', function () {
            verifyOTPBtn.disabled = !otpField.value;
        });

        otpForm.addEventListener('submit', function (event) {
            event.preventDefault();

            if (!verifyOTPBtn.disabled) {
                const spinner = verifyOTPBtn.children[0];

                verifyOTPBtn.disabled = true;
                spinner.classList.toggle("d-none");

                verificationAlert.classList.remove("d-none", "flash-info", "flash-warn", "flash-error");
                verificationAlert.classList.add("d-none");

                fetch('/auth/validate/otp', {
                    method: "POST",
                    body: new URLSearchParams(new FormData(event.target).entries())
                })
                    .then(response => response.json())
                    .then(result => {
                        alertMsg.innerText = result.content;
                        if (result.level === Level.INFO) {
                            verificationAlert.classList.add("flash-info");

                            fetch('/user/update/email_status', {method: "POST"})
                                .then(response => response.json())
                                .then(result => {
                                    if (result.level === Level.INFO) {
                                        window.neededReload = true;
                                    } else if (result.level === Level.ERROR) {
                                        verificationAlert.classList.remove("alert-success");
                                        verificationAlert.classList.add("flash-warn");
                                        alertMsg.innerText = result.content;
                                    }
                                })
                                .catch(error => {
                                    console.error(error);
                                    verificationAlert.classList.remove("alert-success");
                                    verificationAlert.classList.add("flash-warn");
                                    alertMsg.innerText = "Something went wrong, please try again!";
                                });
                        } else if (result.level === Level.WARN) {
                            verificationAlert.classList.add("flash-warn");
                        } else if (result.level === Level.ERROR) {
                            verificationAlert.classList.add("flash-error");
                        } else {
                            verificationAlert.classList.add("flash-error");
                            alertMsg.innerText = "Something went wrong, please try again!";
                        }

                        verificationAlert.classList.remove("d-none");

                        verifyOTPBtn.disabled = false;
                        spinner.classList.toggle("d-none");
                    }).catch(error => {
                    console.error(error);
                    verificationAlert.classList.add("flash-error");
                    alertMsg.innerText = "Something went wrong, please try again!";
                    verificationAlert.classList.remove("d-none");
                    verifyOTPBtn.disabled = false;
                    spinner.classList.toggle("d-none");
                });
            }
        });
    } else {
        console.error("Verification alert box missing.");
    }
} else {
    console.error("Verification dialog not available.");
}

const resetAvatarForm = document.querySelector("#reset-avatar-form");
if (resetAvatarForm) {
    resetAvatarForm.addEventListener('submit', function (event) {
        event.preventDefault();
        let response = confirm(resetAvatarForm.elements[0].getAttribute("data-confirm"));
        if (response) {
            fetch('/settings/reset_avatar', {method: 'POST'})
                .then(response => response.json())
                .then(result => {
                    messageContent.innerHTML = result.content;
                    if (result.level === Level.INFO) {
                        messageIcon.classList.add("flash-info");
                        window.window.neededReload = true;
                    } else if (result.level === Level.WARN) {
                        messageIcon.classList.add("flash-warn");
                    } else if (result.level === Level.ERROR) {
                        messageIcon.classList.add("flash-error");
                    } else {
                        messageIcon.classList.add("flash-error");
                        messageContent.innerText = "Something went wrong, please try again!";
                    }
                    messageModal.show();
                })
                .catch(error => {
                    console.log(error);
                    messageIcon.classList.add("flash-error");
                    messageContent.innerText = "Something went wrong, please try again!";
                    messageModal.show();
                });
        }
    });
} else {
    console.error("Reset avatar form not available.");
}

const avatarUpload = document.querySelector("#avatar-upload");
const submitAvatarPrimary = avatarUpload.elements.namedItem("submit");
const msg = document.querySelector(".js-upload-avatar-image");

const previewAvatarDialog = document.querySelector("#upload-avatar");
const previewAvatarModal = new bootstrap.Modal(previewAvatarDialog);
const avatar = document.querySelector("#avatar");
const submitAvatarSecondary = document.querySelector("#btn-set-profile");

previewAvatarDialog.addEventListener('hidden.bs.modal', function () {
    fileChooser.value = "";
});

submitAvatarSecondary.addEventListener('click', function () {
    previewAvatarModal.hide();
    submitAvatarPrimary.click();
});

const allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
const fileChooser = document.querySelector("#avatar_upload");
fileChooser.addEventListener('change', function () {
    msg.classList.remove("is-bad-dimensions", "is-bad-file", "is-bad-format",
        "is-empty", "is-failed", "is-too-big");

    if (fileChooser.files.length === 0) {
        msg.classList.add("is-empty");
    } else {
        const srcFile = fileChooser.files[0].name;
        const fileSize = fileChooser.files[0].size;

        if (!allowedExtensions.exec(srcFile)) {
            msg.classList.add("is-bad-file");
        } else if (Math.round((fileSize / 1024) / 1024) > 1) {
            msg.classList.add("is-too-big");
        } else {
            if (fileChooser.files && fileChooser.files[0]) {
                const reader = new FileReader();

                reader.onload = function (e) {
                    avatar.onload = function () {
                        if (avatar.naturalWidth > 10_000 && avatar.naturalHeight > 10_000) {
                            msg.classList.add("is-bad-dimensions");
                            avatar.src = "";
                        } else {
                            // avatar.width = 414;
                            // avatar.height = Math.round((414 / avatar.naturalWidth) * avatar.naturalHeight);
                            previewAvatarModal.show();
                        }
                    }
                    avatar.src = e.target.result;
                };

                reader.onerror = function () {
                    msg.classList.add("is-failed");
                }

                reader.readAsDataURL(fileChooser.files[0]);
            }
        }
    }
});

avatarUpload.addEventListener('submit', function (event) {
    event.preventDefault();

    messageIcon.classList.remove("flash-info", "flash-warn", "flash-error");
    messageContent.innerHTML = "<div class=\"d-flex flex-column align-items-center gap-4\">" +
                                    "<div class=\"spinner-border text-info\" style=\"width: 5rem; height: 5rem;\" role=\"status\">" +
                                        "<span class=\"visually-hidden\">Loading...</span>" +
                                    "</div>" +
                                    "<div class=\"h4 w-100 text-center\">Uploading profile picture...</div>" +
                                "</div>";
    messageModal.show();

    fetch('/user/update/avatar', {
        method: 'POST',
        body: new FormData(event.target)
    })
        .then(response => response.json())
        .then(result => {
            messageContent.innerHTML = result.content;
            if (result.level === Level.INFO) {
                messageIcon.classList.add("flash-info");
                window.neededReload = true;
            } else if (result.level === Level.WARN) {
                messageIcon.classList.add("flash-warn");
            } else if (result.level === Level.ERROR) {
                messageIcon.classList.add("flash-error");
            } else {
                messageIcon.classList.add("flash-error");
                messageContent.innerText = "Something went wrong, please try again!";
            }
        })
        .catch(error => {
            console.log(error);
            messageIcon.classList.add("flash-error");
            messageContent.innerText = "Something went wrong, please try again!";
        });
    fileChooser.value = "";
});