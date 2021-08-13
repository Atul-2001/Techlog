<%@ tag description="Authentication Page template" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="script" fragment="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <script src="https://kit.fontawesome.com/4418ec4da8.js" crossorigin="anonymous"></script>
    <script src="<c:url value="/assets/scripts/level.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/assets/stylesheets/custom/techlog/auth.css"/>"/>
    <title>${title}</title>
</head>
<body>
<div class="container">
    <div class="forms-container">
        <div class="signin-signup">
            <form class="sign-in-form">
                <h2 class="title">Welcome back</h2>
                <div id="login-alert" class="alert d-none" role="alert">
                </div>
                <div class="input-field">
                    <i class="fas fa-envelope"></i>
                    <input type="email" name="email" placeholder="Email" required/>
                </div>
                <div class="input-field">
                    <i class="fas fa-lock"></i>
                    <input type="password" id="password" name="password" placeholder="Password" required/>
                </div>
                <div class="input-field password-manager">
                    <div class="form-check check-password">
                        <input class="form-check-input" type="checkbox" value="" id="CheckPasswordL">
                        <label class="form-check-label" for="CheckPasswordL" id="CheckPasswordLabelL">
                            Show password
                        </label>
                    </div>
                    <div class="forget-password">
                        <a href="password_reset">Forgot Password?</a>
                    </div>
                </div>
                <button id="login" type="submit" value="Login" class="btn solid">
                    <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                    Login
                </button>
                <p class="social-text">Or Sign in with social platforms</p>
                <div class="social-media">
                    <a id="google-sign-in-btn" class="social-icon">
                        <i class="fab fa-google"></i>
                    </a>
                    <a class="social-icon">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a class="social-icon">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a class="social-icon">
                        <i class="fab fa-linkedin-in"></i>
                    </a>
                </div>
            </form>
            <form class="sign-up-form">
                <h2 class="title">Join Techlog</h2>
                <div id="register-alert" class="alert d-none" role="alert">
                </div>
                <div class="input-field">
                    <i class="fas fa-user"></i>
                    <input name="name" type="text" placeholder="Name" required/>
                </div>
                <div class="input-field">
                    <i class="fas fa-envelope"></i>
                    <input name="email" type="email" placeholder="Email" required/>
                </div>
                <div class="input-field">
                    <i class="fas fa-venus-mars"></i>
                    <select name="gender" id="gender" required>
                        <option value="" selected>Gender</option>
                        <option value="FEMALE">Female</option>
                        <option value="MALE">Male</option>
                        <option value="TRANSGENDER">Transgender</option>
                    </select>
                </div>
                <div class="input-field">
                    <i class="fas fa-globe"></i>
                    <select name="country" id="country" required>
                        <option value="" selected>Country</option>
                    </select>
                </div>
                <div class="input-field">
                    <i class="fas fa-lock"></i>
                    <input name="password" id="key" type="password" placeholder="Password" required/>
                </div>
                <div class="input-field password-manager">
                    <div class="form-check check-password">
                        <input class="form-check-input" type="checkbox" value="" id="CheckPasswordR">
                        <label class="form-check-label" for="CheckPasswordR" id="CheckPasswordLabelR">
                            Show password
                        </label>
                    </div>
                </div>
                <button id="register" type="submit" value="Sign up" class="btn solid">
                    <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                    Sign up
                </button>
                <p class="social-text">Or Sign up with social platforms</p>
                <div class="social-media">
                    <a id="google-sign-up-btn" class="social-icon">
                        <i class="fab fa-google"></i>
                    </a>
                    <a class="social-icon">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a class="social-icon">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a class="social-icon">
                        <i class="fab fa-linkedin-in"></i>
                    </a>
                </div>
            </form>
        </div>
    </div>

    <div class="panels-container">
        <div class="panel left-panel">
            <div class="content">
                <h3>New here ?</h3>
                <p>
                    Not registered yet?
                    Please register yourself to access all features!
                </p>
                <button class="btn transparent" id="sign-up-btn">
                    Sign up
                </button>
            </div>
            <img src="<c:url value="/assets/images/login_art.svg"/>" class="image" alt=""/>
        </div>
        <div class="panel right-panel">
            <div class="content">
                <h3>One of us ?</h3>
                <p>
                    If you are already registered, please sign in!
                </p>
                <button class="btn transparent" id="sign-in-btn">
                    Sign in
                </button>
            </div>
            <img src="<c:url value="/assets/images/register_art.svg"/>" class="image" alt=""/>
        </div>
    </div>
</div>

<script src="<c:url value="/assets/scripts/app.js"/>"></script>
<jsp:invoke fragment="script"/>
<script src="https://apis.google.com/js/api:client.js?onload=onLoadCallback" async defer></script>
</body>
</html>