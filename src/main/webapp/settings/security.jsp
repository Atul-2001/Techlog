<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="security" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cfn" uri="/WEB-INF/tld/UserTag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.signature.techlog.catalog.AccountSettings" %>

<c:set var="user" value="${cfn:getSessionAttribute(pageContext.request.session, 'user')}" scope="page"/>
<c:if test="${user == null}">
    <c:redirect context="/" url="/"/>
</c:if>

<security:settings
        title="Account security"
        user="${user}"
        option="${AccountSettings.ACCOUNT_SECURITY}">
    <jsp:attribute name="head">
        <link href="<c:url value="/assets/stylesheets/custom/techlog/security.css"/>" rel="stylesheet">
    </jsp:attribute>
    <jsp:attribute name="content">
        <!-- Change Password -->
        <div data-view-component="true" class="Subhead hx_Subhead--responsive">
            <h2 data-view-component="true" class="Subhead-heading">Change password</h2>
        </div>
        <form class="edit_user" id="change_password" aria-label="Change password"
              action="<c:url value="/account/password"/>" accept-charset="UTF-8" method="post">
            <dl class="form-group password-confirmation-form">
                <dt>
                    <label for="user_old_password">Old password</label>
                </dt>
                <dd>
                    <input type="password" name="user[old_password]" id="user_old_password"
                           required="required" autocomplete="current-password" class="form-control" minlength="8" maxlength="64" >
                </dd>
            </dl>
            <div>
                <dl class="form-group password-confirmation-form has-validation required">
                    <dt>
                        <label for="user_new_password">New password</label>
                    </dt>
                    <dd>
                        <input type="password" name="user[password]" id="user_new_password" required="required"
                               autocomplete="off" class="form-control" spellcheck="false" aria-describedby="new password"
                               data-bs-container="body" data-bs-placement="bottom" minlength="8" maxlength="64" >
                    </dd>
                </dl>
                <dl class="form-group password-confirmation-form">
                    <dt>
                        <label for="user_confirm_new_password">Confirm new password</label>
                    </dt>
                    <dd>
                        <input type="password" name="user[password_confirmation]" id="user_confirm_new_password"
                               required="required" autocomplete="new-password" class="form-control" minlength="8" maxlength="64" >
                    </dd>
                </dl>
                <p class="note">
                    Make sure it's <span data-more-than-n-chars="">at least 15 characters</span> OR <span data-min-chars="">at least 8 characters</span>
                    <span data-number-requirement="">including a number</span> <span data-letter-requirement="">and a lowercase letter</span>.
                </p>
            </div>
            <p>
                <button type="submit" name="submit" data-view-component="true" class="btn btn-outline-secondary me-2">
                    <span class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
                    Update password
                </button>
                <span>
                    <a href="<c:url value="/password_reset"/>">I forgot my password</a>
                </span>
            </p>
        </form>
    </jsp:attribute>
    <jsp:attribute name="script">
        <script src="<c:url value="/assets/scripts/security.js"/>" type="text/javascript"></script>
    </jsp:attribute>
</security:settings>