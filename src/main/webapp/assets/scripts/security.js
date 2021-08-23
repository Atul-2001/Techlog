const changePasswordForm = document.querySelector("#change_password");
if (changePasswordForm) {
    const newPassword = changePasswordForm.elements.namedItem("user[password]");
    const newPasswordConfirmation = changePasswordForm.elements.namedItem("user[password_confirmation]");
    const submit = changePasswordForm.elements.namedItem("submit");
    const spinner = submit.children[0];

    const popover = new bootstrap.Popover(newPassword, {
        trigger: 'manual'
    });

    const length15 = new RegExp("^.{15,}$");
    const length8 = new RegExp("^.{8,}$");
    const lowercase = new RegExp("[a-z]");
    const digit = new RegExp("\\d");

    const moreThanNChars = document.querySelector("[data-more-than-n-chars]");
    const minChars = document.querySelector("[data-min-chars]");
    const numberRequirement = document.querySelector("[data-number-requirement]");
    const letterRequirement = document.querySelector("[data-letter-requirement]");

    if (newPassword) {
        newPassword.addEventListener('input', function (event) {
            newPassword.classList.remove("is-loading", "is-valid", "is-invalid");

            moreThanNChars.classList.remove("text-success", "text-danger");
            minChars.classList.remove("text-success", "text-danger");
            numberRequirement.classList.remove("text-success", "text-danger");
            letterRequirement.classList.remove("text-success", "text-danger");

            if (newPassword.value) {
                newPassword.classList.add("is-loading");

                if (length15.test(event.target.value)) {
                    moreThanNChars.classList.add("text-success");
                } else {
                    moreThanNChars.classList.add("text-danger");

                    if (length8.test(event.target.value)) {
                        minChars.classList.add("text-success");
                    } else {
                        minChars.classList.add("text-danger");
                    }

                    if (digit.test(event.target.value)) {
                        numberRequirement.classList.add("text-success");
                    } else {
                        numberRequirement.classList.add("text-danger");
                    }

                    if (lowercase.test(event.target.value)) {
                        letterRequirement.classList.add("text-success");
                    } else {
                        letterRequirement.classList.add("text-danger");
                    }
                }

                const form = new FormData();
                form.append("user[password]", event.target.value);
                fetch('/users/password', {
                    method: "POST",
                    body: new URLSearchParams(form.entries())
                })
                    .then(response => {
                        if (response.ok) {
                            response.json().then(result => {
                                newPassword.classList.replace("is-loading", "is-valid");
                                popover.hide();
                            })
                        } else if (response.status === 422) {
                            response.json().then(result => {
                                newPassword.classList.replace("is-loading", "is-invalid");
                                popover._config.content = result.content;
                                popover.show();
                            })
                        } else {
                            throw new Error("Unknown Exception: " + response.status);
                        }
                    }).catch(error => {
                        newPassword.classList.remove("is-loading");
                        popover.hide();
                        console.error(error);
                });
            } else {
                popover.hide();
            }

            if (newPasswordConfirmation.value) {
                newPasswordConfirmation.classList.remove("is-loading", "is-valid", "is-invalid");
                if (event.target.value === newPasswordConfirmation.value) {
                    newPasswordConfirmation.classList.add("is-valid");
                } else {
                    newPasswordConfirmation.classList.add("is-invalid");
                }
            }
        })
    } else {
        console.error("New Password field is not available!");
    }

    if (newPasswordConfirmation) {
        newPasswordConfirmation.addEventListener('input', function (event) {
            newPasswordConfirmation.classList.remove("is-loading", "is-valid", "is-invalid");

            if (newPasswordConfirmation.value) {
                newPasswordConfirmation.classList.add("is-loading");

                if (event.target.value === newPassword.value) {
                    newPasswordConfirmation.classList.replace("is-loading", "is-valid");
                } else {
                    newPasswordConfirmation.classList.replace("is-loading", "is-invalid");
                }
            }
        })
    } else {
        console.error("Repeat Password field is not available!");
    }

    changePasswordForm.addEventListener('submit', function (event) {
        event.preventDefault();

        submit.disabled = true;
        spinner.classList.remove("d-none");

        messageIcon.classList.remove("flash-info", "flash-warn", "flash-error");
        fetch('/account/password', {
            method: "POST",
            body: new URLSearchParams(new FormData(event.target).entries())
        })
            .then(response => {
                response.json().then(result => {
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

                    submit.disabled = false;
                    spinner.classList.add("d-none");

                    messageModal.show();
                })
            }).catch(error => {
            console.error(error);

            messageIcon.classList.add("flash-error");
            messageContent.innerText = "Something went wrong, please try again!";
            messageModal.show();

            submit.disabled = false;
            spinner.classList.add("d-none");
        });
    })
} else {
    console.error("Change password form is not available!");
}