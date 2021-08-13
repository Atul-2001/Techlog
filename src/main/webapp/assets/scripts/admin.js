const renameForm = document.querySelector("#rename-form");
if (renameForm) {
    const username = renameForm.elements.namedItem("login");
    const inputCheckMsg = document.querySelector("#input-check");
    const submit = renameForm.elements.namedItem("submit");
    const spinner = submit.children[0];

    const popover = new bootstrap.Popover(username, {
        trigger: 'manual'
    });

    const renameFormDialog = document.querySelector("#rename-form-dialog");
    if (renameFormDialog) {
        renameFormDialog.addEventListener('hidden.bs.modal', function () {
            renameForm.reset();
            username.classList.remove("is-loading", "is-valid", "is-invalid");
            inputCheckMsg.innerText = "Choose a new username";
        });
    }

    const pattern = new RegExp("^[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}$");
    if (username) {
        username.addEventListener('input', function (event) {
            username.classList.remove("is-loading", "is-valid", "is-invalid");
            inputCheckMsg.classList.remove("text-danger");

            if (username.value) {
                if (pattern.test(username.value)) {
                    popover.hide();
                    username.classList.add("is-loading");
                    fetch(`/account/rename_check?given_username=${event.target.value}`)
                        .then(response => {
                            if (response.status === 406) {
                                username.classList.replace("is-loading", "is-invalid");
                                inputCheckMsg.innerText = "Username cannot be empty.";
                                inputCheckMsg.classList.add("text-danger");
                            } else if (response.status === 401) {
                                response.redirect("home");
                            } else if (response.status === 304) {
                                username.classList.replace("is-loading", "is-invalid");
                                inputCheckMsg.innerText = "Username must be different.";
                                inputCheckMsg.classList.add("text-danger");
                            } else if (response.status === 409) {
                                username.classList.replace("is-loading", "is-invalid");
                                inputCheckMsg.innerText = `Username ${event.target.value} is not available.`;
                                inputCheckMsg.classList.add("text-danger");
                            } else if (response.status === 202 || response.ok) {
                                username.classList.replace("is-loading", "is-valid")
                                inputCheckMsg.innerText = `${event.target.value} is available.`;
                                submit.disabled = false;
                            } else {
                                throw new Error("Unknown Exception: " + response.status);
                            }
                        }).catch(error => {
                            username.classList.remove("is-loading");
                            console.error(error);
                        });
                } else {
                    popover.show();
                    inputCheckMsg.innerText = `Username ${event.target.value} is not available.`;
                }
            } else {
                inputCheckMsg.innerText = "Choose a new username";
                submit.disabled = true;
                popover.hide();
            }
        });

        renameForm.addEventListener('submit', function (event) {
            event.preventDefault();

            submit.disabled = true;
            spinner.classList.remove("d-none");

            messageIcon.classList.remove("flash-info", "flash-warn", "flash-error");
            fetch('/user/username/rename', {
                method: "POST",
                body: new URLSearchParams(new FormData(event.target).entries())
            })
                .then(response => {
                    response.json().then(result => {
                        messageContent.innerHTML = result.content;
                        if (result.level === Level.INFO) {
                            new bootstrap.Modal(renameFormDialog).hide();
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
                })
                .catch(error => {
                    console.error(error);

                    messageIcon.classList.add("flash-error");
                    messageContent.innerText = "Something went wrong, please try again!";
                    messageModal.show();

                    submit.disabled = false;
                    spinner.classList.add("d-none");
                });
        });
    } else {
        console.error("Input Field not available.");
    }
} else {
    console.error("Rename Form not available.");
}

const deleteWarningDialog = document.querySelector("#delete-warning-dialog");
if (deleteWarningDialog) {
    const deleteForm = document.querySelector("#delete-form");
    if (deleteForm) {
        const submit = deleteForm.elements.namedItem("submit");
        const spinner = submit.children[0];

        deleteForm.addEventListener('change', function () {
            submit.disabled = !deleteForm.checkValidity();
        });

        deleteWarningDialog.addEventListener('hidden.bs.modal', function () {
            deleteForm.reset();
            submit.disabled = true;
            spinner.classList.add("d-none");
        });

        deleteForm.addEventListener('submit', function (event) {
            event.preventDefault();

            submit.disabled = true;
            spinner.classList.remove("d-none");

            messageIcon.classList.remove("flash-info", "flash-warn", "flash-error");
            fetch('/user/account/delete', {
                method: "POST",
                body: new URLSearchParams(new FormData(event.target).entries())
            })
                .then(response => {
                    response.json().then(result => {
                        messageContent.innerHTML = result.content;
                        if (result.level === Level.INFO) {
                            fetch('/user/account/delete', {
                                method: "DELETE"
                            }).then(response => {
                                response.json().then(result => {
                                    messageContent.innerHTML = result.content;
                                    if (result.level === Level.INFO) {
                                        messageIcon.classList.add("flash-info");
                                        setTimeout(() => {
                                            location.assign("/");
                                        }, 1000);
                                    } else if (result.level === Level.WARN) {
                                        messageIcon.classList.add("flash-warn");
                                    } else if (result.level === Level.ERROR) {
                                        messageIcon.classList.add("flash-error");
                                    } else {
                                        messageIcon.classList.add("flash-error");
                                        messageContent.innerText = "Something went wrong, please try again!";
                                    }

                                    if (result.level !== Level.INFO) {
                                        submit.disabled = false;
                                        spinner.classList.add("d-none");

                                        messageModal.show();
                                    }
                                });
                            })
                        } else if (result.level === Level.WARN) {
                            messageIcon.classList.add("flash-warn");
                        } else if (result.level === Level.ERROR) {
                            messageIcon.classList.add("flash-error");
                        } else {
                            messageIcon.classList.add("flash-error");
                            messageContent.innerText = "Something went wrong, please try again!";
                        }

                        if (result.level !== Level.INFO) {
                            submit.disabled = false;
                            spinner.classList.add("d-none");

                            messageModal.show();
                        }
                    })
                })
                .catch(error => {
                    console.error(error);

                    messageIcon.classList.add("flash-error");
                    messageContent.innerText = "Something went wrong, please try again!";
                    messageModal.show();

                    submit.disabled = false;
                    spinner.classList.add("d-none");
                });
        });
    } else {
        console.error("Delete form not available! Reload the page.");
    }
} else {
    console.error("Delete Warning Dialog not available! Reload the page.");
}