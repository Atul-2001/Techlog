const password = document.querySelector("#password");
const checkPasswordL = document.querySelector("#CheckPasswordL");
const checkPasswordLabelL = document.querySelector("#CheckPasswordLabelL");

checkPasswordL.addEventListener('click', function () {
    if (password.type === "password") {
        password.type = "text";
        checkPasswordLabelL.innerText = "Hide Password";
    } else {
        password.type = "password";
        checkPasswordLabelL.innerText = "Show Password";
    }
});

const key = document.querySelector("#key");
const checkPasswordR = document.querySelector("#CheckPasswordR");
const checkPasswordLabelR = document.querySelector("#CheckPasswordLabelR");

checkPasswordR.addEventListener('click', function () {
    if (key.type === "password") {
        key.type = "text";
        checkPasswordLabelR.innerText = "Hide Password";
    } else {
        key.type = "password";
        checkPasswordLabelR.innerText = "Show Password";
    }
});

const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

let last_container_height = "120vh";
sign_up_btn.addEventListener("click", () => {
    container.classList.add("sign-up-mode");
    if (window.matchMedia("(max-width: 870px)").matches) {
        if (last_container_height < "120vh") {
            container.style.height = "120vh";
        } else {
            container.style.height = last_container_height;
        }
    } else if (window.matchMedia("(max-height: 620px)").matches) {
        container.style.height = "112vh";
    } else if (window.matchMedia("(max-height: 630px)").matches) {
        container.style.height = "114vh";
    } else if (window.matchMedia("(max-height: 640px)").matches) {
        container.style.height = "116vh";
    }
});

sign_in_btn.addEventListener("click", () => {
    container.classList.remove("sign-up-mode");
    if (window.matchMedia("(max-width: 870px)").matches) {
        last_container_height = container.style.height;
        container.style.height = "";
    } else {
        container.style.height = "";
    }
});

const gender = document.querySelector("#gender");
if (!gender.value) {
    gender.classList.add("select-default")
}
gender.addEventListener('change', function (event) {
    if (!gender.value) {
        gender.classList.add("select-default")
    } else {
        gender.classList.remove("select-default")
    }
});

const countries = document.querySelector("#country");
fetch('https://restcountries.eu/rest/v2/all')
    .then(response => response.json())
    .then(result => {
        for (let country of result) {
            const option = document.createElement("option");
            option.value = country.name;
            option.text = country.name;
            countries.appendChild(option);
        }
    })
    .catch(error => console.log("ERROR: " + error));

if (!countries.value) {
    countries.classList.add("select-default")
}
countries.addEventListener('change', function (event) {
    if (!countries.value) {
        countries.classList.add("select-default")
    } else {
        countries.classList.remove("select-default")
    }
});

const loginForm = document.querySelector(".sign-in-form");
const loginMsg = document.querySelector("#login-alert");
const loginBtn = loginForm.elements.namedItem("login");
const loginBtnSpinner = loginBtn.children[0];

function resetLoginMsgBanner() {
    loginMsg.classList.remove("d-none", "alert-success", "alert-danger", "alert-warning");
    loginMsg.classList.toggle("d-none");
}

loginForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    loginBtn.disabled = true;
    loginBtnSpinner.classList.toggle("d-none");

    resetLoginMsgBanner();

    fetch('/ajax/login', {
        method: "POST",
        body: new URLSearchParams(new FormData(event.target).entries())
    })
        .then(response => response.json())
        .then(result => {
            showLoginResult(result);

            loginBtn.disabled = false;
            loginBtnSpinner.classList.toggle("d-none");
        }).catch(error => {
        console.error(error);
        loginMsg.classList.toggle("alert-danger");
        loginMsg.innerHTML = "Something went wrong, please try again!";
        loginMsg.classList.toggle("d-none");
        loginBtn.disabled = false;
        loginBtnSpinner.classList.toggle("d-none");
    });
});

function showLoginResult(result) {
    loginMsg.innerHTML = result.content;
    if (result.level === Level.INFO) {
        loginMsg.classList.toggle("alert-success");
        setTimeout(function () {
            window.location.href = "home";
        }, 1000);
    } else if (result.level === Level.WARN) {
        loginMsg.classList.toggle("alert-warning");
    } else if (result.level === Level.ERROR) {
        loginMsg.classList.toggle("alert-danger");
    } else {
        loginMsg.classList.toggle("alert-danger");
        loginMsg.innerHTML = "Something went wrong, please try again!";
    }
    loginMsg.classList.toggle("d-none");
}

const registerForm = document.querySelector(".sign-up-form");
const registerMsg = document.querySelector("#register-alert");
const registerBtn = registerForm.elements.namedItem("register");
const registerBtnSpinner = registerBtn.children[0];

function resetRegisterMsgBanner() {
    registerMsg.classList.remove("d-none", "alert-success", "alert-danger", "alert-warning");
    registerMsg.classList.toggle("d-none");
}

registerForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    registerBtn.disabled = true;
    registerBtnSpinner.classList.toggle("d-none");

    resetRegisterMsgBanner();

    const formData = new FormData(event.target);
    fetch('/ajax/register', {
        method: "POST",
        body: new URLSearchParams(formData.entries())
    })
        .then(response => response.json())
        .then(result => {
            registerMsg.innerHTML = result.content;
            if (result.level === Level.INFO) {
                registerMsg.classList.toggle("alert-success");
                adjustView();
                setInterval(function () {
                    loginForm.elements.email.value = formData.get("email");
                    container.classList.remove("sign-up-mode");
                }, 1000);
            } else if (result.level === Level.WARN) {
                registerMsg.classList.toggle("alert-warning");
                adjustView();
                if (window.matchMedia("(max-width: 870px)").matches && result.content.startsWith("Password length")) {
                    container.style.height = "152vh";
                }
            } else if (result.level === Level.ERROR) {
                registerMsg.classList.toggle("alert-danger");
                adjustView();
            } else {
                registerMsg.classList.toggle("alert-danger");
                registerMsg.innerHTML = "Something went wrong, please try again!";
                adjustView();
            }

            registerMsg.classList.toggle("d-none");

            registerBtn.disabled = false;
            registerBtnSpinner.classList.toggle("d-none");
        })
        .catch(error => {
            console.error(error);
            registerMsg.classList.toggle("alert-danger");
            registerMsg.innerHTML = "Something went wrong, please try again!";
            registerBtn.disabled = false;
            registerBtnSpinner.classList.toggle("d-none");
            adjustView();
        });
});

function adjustView() {
    if (window.matchMedia("(max-width: 870px)").matches) {
        container.style.height = "132vh";
    } else if (window.innerHeight <= 640) {
        container.style.height = "124vh";
    } else if (window.innerHeight <= 650) {
        container.style.height = "106vh";
    } else if (window.innerHeight <= 660) {
        container.style.height = "104vh";
    }
}

/* --------------------------------- Social Authentication --------------------------------------------- */
/*---------------------------------- Google Authentication Start ----------------------------------------*/
let googleUser = {};
let auth2;

window.onLoadCallback = function () {
    gapi.load('auth2', function () {
        // Retrieve the singleton for the GoogleAuth library and set up the client.
        auth2 = gapi.auth2.init({
            client_id: '[YOUR CLIENT ID]',
            cookiepolicy: 'single_host_origin',
            // Request scopes in addition to 'profile' and 'email'
            //scope: 'additional_scope'
        });
        attachSignInBtn(document.getElementById('google-sign-in-btn'));
        attachSignUpBtn(document.getElementById('google-sign-up-btn'));
    });
}

function attachSignInBtn(element) {
    auth2.attachClickHandler(element, {},
        function (googleUser) {
            loginBtn.disabled = true;
            loginBtnSpinner.classList.toggle("d-none");

            resetLoginMsgBanner();

            let id_token = googleUser.getAuthResponse().id_token;
            fetch('/auth/google', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({'id_token': id_token})
            })
                .then(response => response.json())
                .then(result => {
                    showLoginResult(result);
                    loginBtn.disabled = false;
                    loginBtnSpinner.classList.toggle("d-none");
                })
                .catch(error => {
                    console.error(error);
                    loginMsg.classList.toggle("alert-danger");
                    loginMsg.innerHTML = "Something went wrong, please try again!";
                    loginMsg.classList.toggle("d-none");
                    loginBtn.disabled = false;
                    loginBtnSpinner.classList.toggle("d-none");
                });
        }, function (error) {
            alert(JSON.stringify(error, undefined, 2));
        });
}

function attachSignUpBtn(element) {
    auth2.attachClickHandler(element, {},
        function (googleUser) {
            registerBtn.disabled = true;
            registerBtnSpinner.classList.toggle("d-none");

            resetRegisterMsgBanner();

            let id_token = googleUser.getAuthResponse().id_token;
            fetch('/auth/google', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({'id_token': id_token})
            })
                .then(response => response.json())
                .then(result => {
                    registerMsg.innerHTML = result.content;
                    if (result.level === Level.INFO) {
                        registerMsg.classList.toggle("alert-success");
                        adjustView();
                        setTimeout(function () {
                            window.location.href = "home";
                        }, 1000);
                    } else if (result.level === Level.WARN) {
                        registerMsg.classList.toggle("alert-warning");
                        adjustView();
                        if (window.matchMedia("(max-width: 870px)").matches && result.content.startsWith("Password length")) {
                            container.style.height = "152vh";
                        }
                    } else if (result.level === Level.ERROR) {
                        registerMsg.classList.toggle("alert-danger");
                        adjustView();
                    } else {
                        registerMsg.classList.toggle("alert-danger");
                        registerMsg.innerHTML = "Something went wrong, please try again!";
                        adjustView();
                    }

                    registerMsg.classList.toggle("d-none");

                    registerBtn.disabled = false;
                    registerBtnSpinner.classList.toggle("d-none");
                })
                .catch(error => {
                    console.error(error);
                    registerMsg.classList.toggle("alert-danger");
                    registerMsg.innerHTML = "Something went wrong, please try again!";
                    registerBtn.disabled = false;
                    registerBtnSpinner.classList.toggle("d-none");
                    adjustView();
                });
        }, function (error) {
            alert(JSON.stringify(error, undefined, 2));
        });
}
/*---------------------------------- Google Authentication End ------------------------------------------*/
/* --------------------------------- Social Authentication --------------------------------------------- */