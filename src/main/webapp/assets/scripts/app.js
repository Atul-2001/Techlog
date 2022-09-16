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

function prepareLoginEvent() {
    loginBtn.disabled = true;
    loginBtnSpinner.classList.remove("d-none");

    loginMsg.classList.remove("d-none", "alert-success", "alert-danger", "alert-warning");
    loginMsg.classList.add("d-none");
}

loginForm.addEventListener('submit', function (event) {
    event.preventDefault();

    prepareLoginEvent();

    fetch('/ajax/login', {
        method: "POST",
        body: new URLSearchParams(new FormData(event.target).entries())
    })
        .then(response => response.json())
        .then(result => {
            showLoginResult(result);
        }).catch(error => {
            handleAjaxError(error, loginMsg, loginBtn, loginBtnSpinner);
        });
});

function handleCredentialResponseFromGoogleLogin(googleUser) {
    prepareLoginEvent();

    fetch('/oauth/google/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({'credential': googleUser.credential})
    })
        .then(response => response.json())
        .then(result => {
            showLoginResult(result);
        })
        .catch(error => {
            handleAjaxError(error, loginMsg, loginBtn, loginBtnSpinner);
        });
}

function showLoginResult(result) {
    loginMsg.innerHTML = result.content;
    if (result.level === Level.INFO) {
        loginMsg.classList.add("alert-success");
        setTimeout(function () {
            window.location.href = result.redirect;
        }, 1000);
    } else if (result.level === Level.WARN) {
        loginMsg.classList.add("alert-warning");
    } else if (result.level === Level.ERROR) {
        loginMsg.classList.add("alert-danger");
    } else {
        loginMsg.classList.add("alert-danger");
        loginMsg.innerHTML = "Something went wrong, please try again!";
    }
    loginMsg.classList.remove("d-none");

    loginBtn.disabled = false;
    loginBtnSpinner.classList.add("d-none");
}

const registerForm = document.querySelector(".sign-up-form");
const registerMsg = document.querySelector("#register-alert");
const registerBtn = registerForm.elements.namedItem("register");
const registerBtnSpinner = registerBtn.children[0];

function prepareRegisterEvent() {
    registerBtn.disabled = true;
    registerBtnSpinner.classList.remove("d-none");

    registerMsg.classList.remove("d-none", "alert-success", "alert-danger", "alert-warning");
    registerMsg.classList.add("d-none");
}

registerForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    prepareRegisterEvent();

    const formData = new FormData(event.target);
    fetch('/ajax/register', {
        method: "POST",
        body: new URLSearchParams(formData.entries())
    })
        .then(response => response.json())
        .then(result => {
            showRegisterResult(result, function () {
                loginForm.elements.email.value = formData.get("email");
                container.classList.remove("sign-up-mode");
            });
        })
        .catch(error => {
            handleAjaxError(error, registerMsg, registerBtn, registerBtnSpinner);
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

function handleCredentialResponseFromGoogleRegister(googleUser) {
    prepareRegisterEvent();

    fetch('/oauth/google/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({'credential': googleUser.credential})
    })
        .then(response => response.json())
        .then(result => {
            registerMsg.classList.remove("alert-primary");
            registerMsg.classList.add("d-none");
            showRegisterResult(result, function () {
                window.location.href = result.redirect;
            });
        })
        .catch(error => {
            handleAjaxError(error, registerMsg, registerBtn, registerBtnSpinner);
            adjustView();
        });
}

function showRegisterResult(result, successCallback) {
    registerMsg.innerHTML = result.content;
    if (result.level === Level.INFO) {
        registerMsg.classList.add("alert-success");
        adjustView();
        setTimeout(successCallback, 1000);
    } else if (result.level === Level.WARN) {
        registerMsg.classList.add("alert-warning");
        adjustView();
        if (window.matchMedia("(max-width: 870px)").matches && result.content.startsWith("Password length")) {
            container.style.height = "152vh";
        }
    } else if (result.level === Level.ERROR) {
        registerMsg.classList.add("alert-danger");
        adjustView();
    } else {
        registerMsg.classList.add("alert-danger");
        registerMsg.innerHTML = "Something went wrong, please try again!";
        adjustView();
    }

    registerMsg.classList.remove("d-none");

    registerBtn.disabled = false;
    registerBtnSpinner.classList.add("d-none");
}

function handleAjaxError(error, alert, btn, btnSpinner) {
    console.error(error);
    alert.classList.add("alert-danger");
    alert.innerHTML = "Something went wrong, please try again!";
    alert.classList.remove("d-none");
    btn.disabled = false;
    btnSpinner.classList.add("d-none");
}