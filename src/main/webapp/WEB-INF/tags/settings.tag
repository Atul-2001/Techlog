<%@ tag description="Settings Page template" pageEncoding="UTF-8" %>

<%@ taglib prefix="mt" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="user" required="true" type="com.signature.techlog.model.User" %>
<%@ attribute name="option" required="true" type="com.signature.techlog.model.AccountSettings" %>
<%@ attribute name="head" fragment="true" %>
<%@ attribute name="content" fragment="true" %>
<%@ attribute name="modal" fragment="true" %>
<%@ attribute name="script" fragment="true" %>

<mt:base title="${title}" user="${user}">
    <jsp:attribute name="head">
        <link href="<c:url value="/assets/stylesheets/custom/techlog/settings.css"/>" rel="stylesheet">
        <link href="<c:url value="/assets/stylesheets/hacks/hx_responsive-select-menu.css"/>" rel="stylesheet">
        <link href="<c:url value="/assets/stylesheets/hacks/hx_responsive-subhead.css"/>" rel="stylesheet">
        <link href="<c:url value="/assets/stylesheets/components/flash-alert.css"/>" rel="stylesheet">
        <link href="<c:url value="/assets/stylesheets/components/dialog.css"/>" rel="stylesheet">
        <jsp:invoke fragment="head"/>
    </jsp:attribute>
    <jsp:attribute name="content">
        <main>
            <div class="pt-4 container-lg p-responsive clearfix">
                <section class="d-md-flex align-items-center justify-content-between mt-1 mb-4">
                    <div class="d-flex align-items-center mb-2 mb-md-0">
                        <div class="avatar-user avatar me-3">
                            <img src="<c:url value="/user/profile/${user.id}"/>" alt="<${user.username}" sizes="48" height="48" width="48">
                        </div>
                        <div class="flex-auto">
                            <h1 class="h-3 mb-0 lh-condensed">
                                <a href="<c:url value="/${user.username}"/>" class="text-dark">${user.name}</a>
                            </h1>
                            <div class="d-flex align-items-center flex-wrap">
                                <p class="text-secondary mb-0 me-3">Your personal account</p>
                            </div>
                        </div>
                    </div>
                    <a href="<c:url value="/${user.username}"/>" class="btn btn-outline-secondary btn-sm">Go to your personal profile</a>
                </section>
                <section class="d-flex flex-md-row flex-column px-md-0 px-3">
                    <div class="col-md-3 col-12 pe-md-4 pe-0">
                        <nav class="menu position-relative" aria-label="Personal settings">
                            <span class="menu-heading">Account settings</span>
                            <a id="PROFILE" class="menu-item" data-selected-links="/settings/profile" href="<c:url value="/settings/profile"/>">Profile</a>
                            <a id="ACCOUNT" class="menu-item" data-selected-links="/settings/admin" href="<c:url value="/settings/admin"/>">Account</a>
                            <a id="ACCOUNT_SECURITY" class="menu-item" data-selected-links="/settings/security" href="<c:url value="/settings/security"/>">Account security</a>
                            <a id="NOTIFICATIONS" class="menu-item" data-selected-links="" href="">Notifications</a>
                            <a id="YOUR_BLOGS" class="menu-item" data-selected-links="" href="">Your blogs</a>
                            <a id="HISTORY" class="menu-item" data-selected-links="" href="">History</a>
                            <a id="SAVED" class="menu-item" data-selected-links="" href="">Saved</a>
                            <a id="READ_LATER" class="menu-item" data-selected-links="" href="">Read later</a>
                            <a id="LIKED_BLOGS" class="menu-item" data-selected-links="" href="">Liked blogs</a>
                        </nav>
                    </div>
                    <div class="col-md-9 col-12">
                        <jsp:invoke fragment="content"/>
                    </div>
                </section>
            </div>
        </main>

        <%--
        <i class="far fa-check-circle"></i> // empty
        <i class="fas fa-check-circle"></i> // filled
        <i class="fas fa-info-circle"></i>
        <i class="fas fa-exclamation-triangle"></i>
        <i class="fas fa-exclamation-circle"></i>
        --%>
        <div id="message-modal" class="modal modal-overlay-dark" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Message</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="message-icon" data-view-component="true" class="d-flex flash flash-full">
                            <i class="fas fa-info-circle d-none octicon octicon-info"></i>
                            <i class="fas fa-exclamation-circle d-none octicon octicon-warn"></i>
                            <i class="fas fa-exclamation-triangle d-none octicon octicon-alert"></i>
                            <p id="message-content"></p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-sm btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <jsp:invoke fragment="modal"/>
    </jsp:attribute>
    <jsp:attribute name="script">
        <script>
            const navbar = document.querySelector("nav");
            navbar.classList.remove("sticky-top");

            const menuItems = document.querySelectorAll(".menu-item");
            for (let menuItem of menuItems) {
                if (menuItem.id === "${option.toString()}") {
                    menuItem.ariaSelected = true;
                }
            }
        </script>
        <script src="<c:url value="/assets/scripts/dialog.js"/>" type="text/javascript"></script>
        <jsp:invoke fragment="script"/>
    </jsp:attribute>
</mt:base>