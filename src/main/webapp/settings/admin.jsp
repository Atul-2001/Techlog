<%@ page session="false" contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="admin" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cfn" uri="/WEB-INF/tld/UserTag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.signature.techlog.catalog.AccountSettings" %>

<c:set var="user" value="${cfn:getSessionAttribute(pageContext.request.session, 'user')}" scope="page"/>
<c:if test="${user == null}">
    <c:redirect context="/" url="/"/>
</c:if>

<admin:settings
        title="Account settings"
        user="${user}"
        option="${AccountSettings.ACCOUNT}">
    <jsp:attribute name="head">
        <link href="<c:url value="/assets/stylesheets/custom/techlog/account.css"/>" rel="stylesheet">
        <script src="https://accounts.google.com/gsi/client" async defer></script>
    </jsp:attribute>
    <jsp:attribute name="content">
        <!-- Change Username -->
        <div data-view-component="true" class="Subhead hx_Subhead--responsive">
            <h2 data-view-component="true" class="Subhead-heading">Change username</h2>
        </div>

        <p>Changing your username can have <a href="">unintended side effects</a>.</p>

        <button type="button" class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#rename-warning-dialog">
            Change username
        </button>

        <div class="modal fade modal-overlay-dark" id="rename-warning-dialog" data-bs-backdrop="static" data-bs-keyboard="false"
             aria-hidden="true" aria-labelledby="renameWarningDialog" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Really change your username?</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div data-view-component="true" class="flash flash-full flash-error">
                        <i class="fas fa-exclamation-triangle octicon octicon-alert"></i>
                        Unexpected bad things will happen if you don’t read this!
                    </div>
                    <div class="modal-body">
                        <ul class="mb-3 ms-3">
                            <li>We <strong>will not</strong> set up redirects for your old profile page.</li>
                            <li>We <strong>will not</strong> set up redirects for Pages sites.</li>
                            <li>We <strong>will</strong> create redirects for your blogs.</li>
                            <li>Renaming may take a few minutes to complete.</li>
                        </ul>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-sm btn-block btn-outline-danger" data-bs-target="#rename-form-dialog" data-bs-toggle="modal" data-bs-dismiss="modal">
                            I understand, let’s change my username
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade modal-overlay-dark" id="rename-form-dialog" data-bs-backdrop="static" data-bs-keyboard="false"
             aria-hidden="true" aria-labelledby="renameFormDialog" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalToggleLabel2">Enter a new username</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form id="rename-form" aria-label="Change username" action="<c:url value="/user/username/rename"/>" accept-charset="UTF-8" method="post">
                        <div class="modal-body">
                            <dl class="form-group has-validation required">
                                <dt class="input-label"><%--/account/rename_check?suggest_usernames=true--%></dt>
                                <dd>
                                    <input type="text" name="login" pattern="^[a-zA-Z0-9](?:[a-zA-Z0-9]|-(?=[a-zA-Z0-9])){0,38}$" autocomplete="off"
                                           spellcheck="false" autocapitalize="off" autofocus required class="form-control" aria-label="Username"
                                           data-bs-container="body" data-bs-placement="bottom"
                                           data-bs-content="Username may only contain alphanumeric characters or single hyphens,
                                            and cannot begin or end with a hyphen.">
                                    <p class="note" id="input-check">Choose a new username</p>
                                </dd>
                            </dl>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" name="submit" class="btn btn-sm btn-block btn-primary" disabled>
                                <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                Change my username
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <p class="small text-secondary mt-3">
            <i class="far fa-question-circle"></i>
            Looking to manage account security settings? You can find them in the
            <a href="<c:url value="/settings/security"/>">Account security</a> tab.
        </p>

        <!-- Download Everything -->
        <div data-view-component="true" class="Subhead hx_Subhead--responsive Subhead--spacious">
            <h2 data-view-component="true" class="Subhead-heading">Export account data</h2>
        </div>

        <p> Export all repositories and profile metadata for <strong>${user.username}</strong>. Exports will be available for 7 days.</p>

        <c:choose>
            <c:when test="${user.archives.size() <= 0}">
                <form method="GET" action="<c:url value="/settings/migration"/>">
                    <input class="btn btn-sm btn-outline-secondary" type="submit" value="Start export">
                </form>
<%--                <jsp:include page="/settings/migration" flush="true"/>--%>
            </c:when>
            <c:otherwise>
                <div class="accordion">
                    <div class="accordion-item">
                        <div class="accordion-header">
                            <h2 class="accordion-title">Recent exports</h2>
                            <form method="GET" action="<c:url value="/settings/migration"/>">
                                <input class="btn btn-sm btn-outline-secondary" type="submit" value="New export">
                            </form>
                        </div>
                        <div class="accordion-body">
                            <ul class="list-group">
                                <c:forEach var="archive" items="${user.archives}">
                                    <li class="list-group-item">${archive}</li>
                                </c:forEach>
                                <li class="list-group-item">An item</li>
                                <li class="list-group-item">A second item</li>
                                <li class="list-group-item">A third item</li>
                                <li class="list-group-item">A fourth item</li>
                                <li class="list-group-item">And a fifth one</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Delete Account -->
        <div data-view-component="true" class="Subhead hx_Subhead--responsive Subhead--spacious">
            <h2 data-view-component="true" class="Subhead-heading Subhead-heading--danger">Delete account</h2>
        </div>

        <p>Once you delete your account, there is no going back. Please be certain.</p>

        <button type="button" class="btn btn-sm btn-outline-danger mb-5" data-bs-toggle="modal" data-bs-target="#delete-warning-dialog">
            Delete your account
        </button>

        <div class="modal fade modal-overlay-dark" id="delete-warning-dialog" data-bs-backdrop="static" data-bs-keyboard="false"
             aria-hidden="true" aria-labelledby="deleteWarningDialog" tabindex="-1">
            <div class="modal-dialog modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Are you sure you want to do this?</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div data-view-component="true" class="flash flash-full flash-error">
                        <i class="fas fa-exclamation-triangle octicon octicon-alert"></i>
                        This is extremely important.
                    </div>
                    <div class="modal-body">
                        <form id="delete-form" aria-label="Delete account" action="<c:url value="/user/account/delete"/>" accept-charset="UTF-8" method="post">
                            <p>We will <strong>immediately delete all of your blogs (${user.blogs.size()})</strong>,
                                along with all of your history, saved blogs, read later, likes, and comments.</p>
                            <p>You will no longer be billed, and your username will be available to anyone on Techlog.</p>
                            <p>For more help, read our article "<a href="">Deleting your user account</a>".</p>
                            <hr>

                            <p>
                                <label for="sudo_login">Your username or email:</label>
                                <input type="text" id="sudo_login" name="sudo_login" class="form-control input-block"
                                       pattern="${user.username}|${user.email}" required>
                            </p>
                            <p>
                                <label for="confirmation_phrase">
                                    To verify, type
                                    <span class="confirmation-phrase user-select-none notranslate">delete my account</span>
                                    below:
                                </label>
                                <input type="text" id="confirmation_phrase" name="confirmation_phrase"
                                       class="form-control input-block" pattern="delete my account" required>
                            </p>
                            <p>
                                <label for="sudo_password">
                                    Confirm your password:
                                </label>
                                <input type="password" name="sudo_password" id="sudo_password"
                                       class="form-control form-control input-block" autocomplete="current-password" <c:if test="${user.password == null || user.password.isEmpty()}"> disabled </c:if>>
                            </p>
                            <button type="submit" name="submit" class="btn btn-sm btn-block btn-outline-danger" disabled>
                                <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                                Cancel plan and delete this account
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
    <jsp:attribute name="script">
        <script src="<c:url value="/assets/scripts/admin.js"/>" type="text/javascript"></script>
    </jsp:attribute>
</admin:settings>