<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cfn" uri="/WEB-INF/tld/UserTag" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link href="<c:url value="/assets/stylesheets/base.css"/>" type="text/css" rel="stylesheet">
    <link href="<c:url value="/assets/stylesheets/components/email-verification.css"/>" type="text/css" rel="stylesheet">
    <link href="<c:url value="/assets/stylesheets/components/footer.css"/>" type="text/css" rel="stylesheet">
    <script src="https://kit.fontawesome.com/4418ec4da8.js" crossorigin="anonymous"></script>
    <script src="<c:url value="/assets/scripts/browser.js"/>" rel="script" type="text/javascript"></script>
    <script src="<c:url value="/assets/scripts/level.js"/>"></script>
    <title>Forgot your Password? - Techlog</title>
</head>
<body class="session-authentication" style="word-wrap: break-word;">
<header class="pt-5 pb-4 align-self-center">
    <div class="container w-100 text-center">
        <a href="<c:url value="/"/>" aria-label="Homepage"
           data-ga-click="(Logged out) Header, go to homepage">
            <i id="brand-logo" class="fas fa-blog fa-4x"></i>
        </a>
    </div>
</header>

<main>
    <div class="container-sm px-3">
        <div id="password_reset_carousel" class="carousel slide carousel-fade" data-bs-touch="false">
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <form id="email_form" accept-charset="UTF-8">
                        <div class="auth-form-header p-0">
                            <h1>Reset your password</h1>
                        </div>
                        <div id="verification-alert" class="alert d-none" role="alert"></div>
                        <div class="auth-form-body mt-3">
                            <label for="email_field">
                                Enter your user account's verified email address and we will send you a One-Time-Password (OTP) for verification.
                            </label>
                            <div class="input-group flex-nowrap input-block">
                                <span class="input-group-text" id="email-wrapping"><i class="fas fa-envelope"></i></span>
                                <c:set var="user" value="${cfn:getSessionAttribute(pageContext.request.session, 'user')}" scope="page"/>
                                <c:choose>
                                    <c:when test="${user == null}">
                                        <input type="email" name="email_field" id="email_field" class="form-control"
                                               placeholder="Enter your email address" aria-label="Email"
                                               aria-describedby="email-wrapping" required>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="email" name="email_field" id="email_field" class="form-control"
                                               value="${user.email}" aria-label="Email" aria-describedby="email-wrapping" required>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <h6 id="timer" class="text-end d-none">00:00</h6>
                            <button type="submit" id="verifyEmailBtn" class="btn btn-primary btn-block" disabled>
                                <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                Send password reset code
                            </button>
                        </div>
                    </form>

                    <form id="otp_form" class="d-none" accept-charset="UTF-8" autocomplete="off" autocapitalize="off" spellcheck="false">
                        <div class="auth-form-body mt-3">
                            <label for="otp_field">Verify your account</label>
                            <div class="input-group flex-nowrap input-block">
                                <span class="input-group-text" id="otp-wrapping"><i class="fas fa-keyboard"></i></span>
                                <input type="number" name="otp_field" id="otp_field" class="form-control" placeholder="Enter OTP"
                                       aria-label="OTP" aria-describedby="opt-wrapping" required>
                            </div>
                            <button type="submit" id="verifyOtpBtn" class="btn btn-success btn-block" disabled>
                                <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                Verify
                            </button>
                        </div>
                    </form>
                </div>
                <div class="carousel-item">
                    <form id="password_form" accept-charset="UTF-8" autocomplete="off" autocapitalize="off" spellcheck="false">
                        <div class="auth-form-header p-0">
                            <h1>Create password</h1>
                        </div>
                        <div id="password-reset-alert" class="alert d-none" role="alert"></div>
                        <div class="auth-form-body mt-3">
                            <label for="password_field">Create New Password</label>
                            <input type="password" name="password_field" id="password_field" class="form-control mt-2"
                                   placeholder="Enter new password" aria-label="Password" required>
                            <input type="password" name="repeat_password_field" id="repeat_password_field" class="form-control mt-3"
                                   placeholder="Confirm new password" aria-label="Repeat password" required>
                            <div class="form-check mt-2 mb-4">
                                <input class="form-check-input" type="checkbox" value="" id="CheckPassword">
                                <label class="form-check-label" for="CheckPassword" id="CheckPasswordLabel">
                                    Show password
                                </label>
                            </div>
                            <button type="submit" id="resetPasswordBtn" class="btn btn-primary btn-block" disabled>
                                <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                Reset Password
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="footer fixed-bottom container-lg p-responsive py-6 mt-6 f6" role="contentinfo">
    <ul class="list-style-none d-flex justify-content-center">
        <li class="me-3"><a href="#" data-ga-click="Footer, go to terms, text:terms">Terms</a></li>
        <li class="me-3"><a href="#" data-ga-click="Footer, go to privacy, text:privacy">Privacy</a></li>
        <li class="me-3"><a href="#" data-ga-click="Footer, go to security, text:security">Security</a></li>
        <li><a class="link--secondary" data-ga-click="Footer, go to contact, text:contact" href="#">Contact Techlog</a></li>
    </ul>
</footer>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
        integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"
        integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT"
        crossorigin="anonymous"></script>
<script src="<c:url value="/assets/scripts/password_reset.js"/>"></script>

</body>
</html>