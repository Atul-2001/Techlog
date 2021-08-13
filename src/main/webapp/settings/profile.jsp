<%@ page session="false" contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="profile" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cfn" uri="/WEB-INF/tld/UserTag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.signature.techlog.model.AccountSettings" %>
<%@ page import="com.signature.techlog.model.User.ContactType" %>

<c:set var="user" value="${cfn:getSessionAttribute(pageContext.request.session, 'user')}" scope="page"/>
<c:if test="${user == null}">
    <c:redirect context="/" url="/"/>
</c:if>

<profile:settings
        title="Your Profile"
        user="${user}"
        option="${AccountSettings.PROFILE}">
    <jsp:attribute name="head">
        <link href="<c:url value="/assets/stylesheets/components/email-verification.css"/>" rel="stylesheet">
        <link href="<c:url value="/assets/stylesheets/custom/techlog/profile.css"/>" rel="stylesheet">
        <script src="<c:url value="/assets/scripts/browser.js"/>"></script>
    </jsp:attribute>
    <jsp:attribute name="content">
        <!-- Public Profile -->
        <div data-view-component="true" class="Subhead hx_Subhead--responsive mt-0 mb-0">
            <h2 id="public-profile-heading" data-view-component="true" class="Subhead-heading">Public profile</h2>
        </div>
        <div class="clearfix gutter d-flex flex-shrink-0 flex-column-reverse flex-md-row">
            <div class="col-12 col-md-8">
                <form class="edit_user" id="edit_user_${user.id}" aria-labelledby="public-profile-heading"
                      action="<c:url value="/user/update/profile"/>" accept-charset="UTF-8" method="post">
                    <div>
                        <dl class="form-group">
                            <dt>
                                <label for="user_profile_name">Name</label>
                            </dt>
                            <dd>
                                <input class="form-control" type="text" value="${user.name}" name="user[profile_name]" id="user_profile_name" required>
                                <div class="note">
                                    Your name may appear around Techlog where you contribute or are mentioned.
                                    You can remove it at any time.
                                </div>
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt>
                                <label for="user_profile_email">Email</label>
                            </dt>
                            <dd>
                                <input class="form-control" type="email" value="${user.email}"
                                       name="user[profile_email]" id="user_profile_email" required readonly>
                                <div class="d-flex justify-content-between" style="max-width: 440px;">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox"
                                               value="Keep my email addresses private" name="user[email_visibility]"
                                               id="user_email_visibility"
                                        <c:if test="${user.emailPrivate}"> checked </c:if> >
                                        <label class="form-check-label" for="user_email_visibility">Keep my email addresses private</label>
                                    </div>
                                    <div id="email-status">
                                        <c:choose>
                                            <c:when test="${user.emailVerified}">
                                                <label for="user_profile_email" class="text-success"><i
                                                        class="fas fa-check"></i> Verified</label>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" name="verify_email" class="button-danger"
                                                        data-bs-toggle="modal" data-bs-target="#verifyEmailModal"><i
                                                        class="far fa-times-circle"></i> Not verified
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt>
                                <label for="user_profile_phone">Phone</label>
                            </dt>
                            <dd>
                                <input class="form-control" type="tel" value="${user.phone}" name="user[profile_phone]"
                                       id="user_profile_phone">
                                <div class="note">
                                    It's optional to have a phone number, but it's good to have it.
                                    It will help in account recovery when your email is out of your reach.
                                </div>
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt>
                                <label for="user_profile_gender">Gender</label>
                            </dt>
                            <dd>
                                <select class="form-select" name="user[profile_gender]" id="user_profile_gender"
                                        required>
                                    <option value="" selected>Select gender</option>
                                    <option value="FEMALE">Female</option>
                                    <option value="MALE">Male</option>
                                    <option value="TRANSGENDER">Transgender</option>
                                </select>
                                <input type="hidden" name="gender" id="selected_gender" value="${user.gender}">
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt>
                                <label for="user_profile_dob">Date of birth</label>
                            </dt>
                            <dd>
                                <input class="form-control" type="date" value="${user.dateOfBirth}"
                                       name="user[profile_dob]" id="user_profile_dob">
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt>
                                <label for="user_profile_bio">Bio</label>
                            </dt>
                            <dd class="user-profile-bio-field-container">
                                <textarea class="form-control user-profile-bio-field"
                                          placeholder="Tell us a little bit about yourself" data-input-max-length="160"
                                          data-warning-text="{{remaining}} remaining" name="user[profile_bio]"
                                          id="user_profile_bio">${user.about}</textarea>
                                <p class="note">
                                    You can <strong>@mention</strong> other users and
                                    organizations to link to them.
                                </p>
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt>
                                <label for="youtube">Contacts</label>
                            </dt>
                            <dd>
                                <div class="input-group flex-nowrap mb-3">
                                    <span class="input-group-text" id="youtube"><i class="fab fa-youtube"></i></span>
                                    <input type="url" class="form-control" name="user[profile_contact_youtube]"
                                           placeholder="Youtube channel link" aria-label="Youtube channel link"
                                           aria-describedby="youtube" value="${user.contacts[ContactType.YOUTUBE]}">
                                </div>
                            </dd>
                            <dd>
                                <div class="input-group flex-nowrap mb-3">
                                    <span class="input-group-text" id="facebook"><i class="fab fa-facebook"></i></span>
                                    <input type="url" class="form-control" name="user[profile_contact_facebook]"
                                           placeholder="Facebook profile link" aria-label="Facebook profile link"
                                           aria-describedby="facebook" value="${user.contacts[ContactType.FACEBOOK]}">
                                </div>
                            </dd>
                            <dd>
                                <div class="input-group flex-nowrap mb-3">
                                    <span class="input-group-text" id="instagram"><i
                                            class="fab fa-instagram"></i></span>
                                    <input type="url" class="form-control" name="user[profile_contact_instagram]"
                                           placeholder="Instagram profile link" aria-label="Instagram profile link"
                                           aria-describedby="instagram" value="${user.contacts[ContactType.INSTAGRAM]}">
                                </div>
                            </dd>
                            <dd>
                                <div class="input-group flex-nowrap mb-3">
                                    <span class="input-group-text" id="twitter"><i class="fab fa-twitter"></i></span>
                                    <input type="url" class="form-control" name="user[profile_contact_twitter]"
                                           placeholder="Twitter profile link" aria-label="Twitter profile link"
                                           aria-describedby="twitter" value="${user.contacts[ContactType.TWITTER]}">
                                </div>
                            </dd>
                        </dl>
                        <dl class="form-group">
                            <dt>
                                <label for="user_profile_country">Country</label>
                            </dt>
                            <dd>
                                <select class="form-select" name="user[profile_country]" id="user_profile_country" required>
                                    <option value="" selected>Select country</option>
                                </select>
                                <input type="hidden" name="country" id="selected_country" value="${user.country}">
                            </dd>
                        </dl>
                        <p class="note mb-2">
                            Some of the fields on this page are optional and can be deleted at any
                            time, and by filling them out, you're giving us consent to share this
                            data wherever your user profile appears.
                        </p>
                        <p>
                            <button type="submit" name="btn_update" id="btn_update_profile" data-view-component="true" class="btn btn-primary">
                                <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                Update profile
                            </button>
                        </p>
                    </div>
                </form>
            </div>
            <div class="col-12 col-md-4">
                <dl>
                    <dt>
                        <label class="d-block mb-2">Profile picture</label>
                    </dt>
                    <dd class="avatar-upload-container clearfix position-relative">
                        <form id="avatar-upload" class="edit_user" novalidate="novalidate"
                              aria-label="Profile picture" accept-charset="UTF-8"
                              enctype="multipart/form-data" method="post" action="<c:url value="/user/update/avatar"/>">
                            <div class="js-upload-avatar-image">
                                <input type="file" id="avatar_upload" name="user[profile_avatar]"
                                       class="manual-file-chooser width-full ml-0" hidden>
                                <input type="submit" id="submit" hidden/>
                                <div class="avatar-upload">
                                    <div class="upload-state text-danger file-empty">
                                        This file is empty.
                                    </div>
                                    <div class="upload-state text-danger too-big">
                                        Please upload a picture smaller than 1 MB.
                                    </div>
                                    <div class="upload-state text-danger bad-dimensions">
                                        Please upload a picture smaller than 10,000x10,000.
                                    </div>
                                    <div class="upload-state text-danger bad-file">
                                        We only support PNG, GIF, or JPG pictures.
                                    </div>
                                    <div class="upload-state text-danger failed-request">
                                        Something went really wrong and we can’t process that picture.
                                    </div>
                                    <div class="upload-state text-danger bad-format">
                                        File contents don’t match the file extension.
                                    </div>
                                </div>
                            </div>
                        </form>
                        <div class="avatar-upload">
                            <details class="dropdown details-reset details-overlay">
                                <summary aria-haspopup="menu" role="button">
                                    <img class="avatar rounded-2 avatar-user" src="<c:url value="/user/profile/${user.id}"/>"
                                         width="200" height="200" alt="${user.username}">
                                    <div class="position-absolute bg-white rounded-2 text-dark px-2 py-1 left-0 bottom-0 ms-2 mb-2 border">
                                        <i class="fas fa-pen"></i> Edit
                                    </div>
                                </summary>
                                <div class="dropdown-menu dropdown-menu-se details-menu" style="z-index: 99" role="menu">
                                    <label for="avatar_upload" class="dropdown-item text-normal"
                                           style="cursor: pointer;" role="menuitem" tabindex="0">
                                        Upload a photo…
                                    </label>
                                    <c:if test="${!fn:contains(user.profile, '/icons/')}">
                                        <form id="reset-avatar-form" action="<c:url value="/settings/reset_avatar"/>"
                                              accept-charset="UTF-8" method="post">
                                            <button class="btn-link dropdown-item js-detect-gravatar"
                                                    type="submit" name="op" value="reset" role="menuitem"
                                                    data-confirm="Are you sure you want to reset your current avatar?">
                                                Remove photo
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </details>
                        </div>
                    </dd>
                </dl>
            </div>
        </div>
    </jsp:attribute>
    <jsp:attribute name="modal">
        <c:if test="${!user.emailVerified}">
            <div id="verifyEmailModal" class="modal modal-overlay-dark fade" data-bs-backdrop="static" data-bs-keyboard="false"
                 tabindex="-1" aria-labelledby="verifyEmailModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Verify Email</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div id="verification-alert" data-view-component="true" class="flash flash-full d-none">
                            <i class="fas fa-info-circle fa-lg d-none octicon octicon-info"></i>
                            <i class="fas fa-exclamation-circle fa-lg d-none octicon octicon-warn"></i>
                            <i class="fas fa-exclamation-triangle fa-lg d-none octicon octicon-alert"></i>
                            <h6 class="alert-msg"></h6>
                        </div>
                        <div class="modal-body session-authentication">
                            <form id="email-form" accept-charset="UTF-8">
                                <div class="auth-form-body">
                                    <label for="email_field">Enter your user account's verified email address and we will
                                        send you a One-Time-Password (OTP) for verification.</label>
                                    <div class="input-group flex-nowrap input-block">
                                    <span class="input-group-text" id="email-wrapping">
                                        <i class="fas fa-envelope"></i>
                                    </span>
                                        <input type="email" name="email_field" id="email_field" class="form-control" value="${user.email}"
                                               aria-label="Email" aria-describedby="email-wrapping" required readonly>
                                    </div>
                                    <h6 id="timer" class="text-end d-none">00:00</h6>
                                    <button type="submit" id="verifyEmailBtn" class="btn btn-primary btn-block">
                                        <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                        Send password reset code
                                    </button>
                                </div>
                            </form>

                            <form id="otp-form" class="d-none" accept-charset="UTF-8" autocomplete="off"
                                  autocapitalize="off" spellcheck="false">
                                <div class="auth-form-body mt-3">
                                    <label for="otp-field">Verify your account</label>
                                    <div class="input-group flex-nowrap input-block">
                                    <span class="input-group-text" id="otp-wrapping"><i
                                            class="fas fa-keyboard"></i></span>
                                        <input type="number" name="otp_field"
                                               id="otp-field" class="form-control" placeholder="Enter OTP"
                                               aria-label="OTP" aria-describedby="opt-wrapping" required>
                                    </div>
                                    <input type="hidden" name="email" value="${user.email}">
                                    <button type="submit" id="verifyOtpBtn" class="btn btn-success btn-block" disabled>
                                    <span class="spinner-border spinner-border-sm d-none"
                                          role="status" aria-hidden="true"></span>
                                        Verify
                                    </button>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-sm btn-secondary" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <div id="upload-avatar" class="modal fade modal-overlay-dark" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1">
            <div class="modal-dialog modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Image preview</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <img id="avatar" class="img-fluid" src="" alt="">
                    </div>
                    <div class="modal-footer">
                        <button id="btn-set-profile" type="button" class="btn btn-block btn-success">Set new profile picture</button>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
    <jsp:attribute name="script">
        <script>
            const gender = document.querySelector("#user_profile_gender");
            const selectedGender = document.querySelector("#selected_gender");
            for (let option of gender.options) {
                if (option.value === selectedGender.value) {
                    option.selected = true;
                }
            }

            const countries = document.querySelector("#user_profile_country");
            const selectedCountry = document.querySelector("#selected_country");
            fetch("https://restcountries.eu/rest/v2/all")
                .then(response => response.json())
                .then(result => {
                    for (let country of result) {
                        const option = document.createElement("option");
                        option.value = country.name;
                        option.text = country.name;
                        countries.appendChild(option);
                        if (country.name === selectedCountry.value) {
                            option.selected = true;
                        }
                    }
                })
                .catch(error => console.log("ERROR: " + error));
        </script>
        <script src="<c:url value="/assets/scripts/profile.js"/>" type="text/javascript"></script>
        <script>
            const profileForm = document.querySelector("#edit_user_${user.id}");
            const updateBtn = profileForm.elements.namedItem("btn_update_profile");
            const spinner = updateBtn.children[0];

            profileForm.addEventListener('submit', function (event) {
                event.preventDefault();

                updateBtn.disabled = true;
                spinner.classList.remove("d-none");

                messageIcon.classList.remove("flash-info", "flash-warn", "flash-error");
                fetch('/user/update/profile', {
                    method: "POST",
                    body: new URLSearchParams(new FormData(event.target).entries())
                })
                    .then(response => response.json())
                    .then(result => {
                        messageContent.innerHTML = result.content;
                        if (result.level === Level.INFO) {
                            messageIcon.classList.add("flash-info");
                            neededReload = true;
                        } else if (result.level === Level.WARN) {
                            messageIcon.classList.add("flash-warn");
                        } else if (result.level === Level.ERROR) {
                            messageIcon.classList.add("flash-error");
                        } else {
                            messageIcon.classList.add("flash-error");
                            messageContent.innerText = "Something went wrong, please try again!";
                        }
                        messageModal.show();

                        updateBtn.disabled = false;
                        spinner.classList.add("d-none");
                    })
                    .catch(error => {
                        console.error(error);

                        messageIcon.classList.add("flash-error");
                        messageContent.innerText = "Something went wrong, please try again!";
                        messageModal.show();

                        updateBtn.disabled = false;
                        spinner.classList.add("d-none");
                    });
            })
        </script>
    </jsp:attribute>
</profile:settings>