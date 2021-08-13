<%@ tag description="Navigation bar template" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/components" %>

<%@ attribute name="user" required="true" type="com.signature.techlog.model.User" %>

<c:choose>
    <c:when test="${user == null}">
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary border-1 border-info border-bottom sticky-top">
            <div class="container-fluid px-lg-4 py-lg-1 px-md-2">
                <component:brand-logo/>
                <div>
                    <button class="btn btn-success d-inline-block d-lg-none me-1" type="button"
                            onclick="window.location.href = '/register'" hidden>Sign up
                    </button>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                </div>
                <component:nav-collapse
                        navbar_nav_classes="me-auto mt-3 mb-4 my-lg-0"
                        nav_item_1="border-top border-lg-top-0 border-bottom border-lg-bottom-0 border-white-fade"
                        nav_item_2="dropdown border-bottom border-lg-bottom-0 border-white-fade"
                        nav_item_3="border-bottom border-lg-bottom-0 border-white-fade"
                        nav_item_4="border-bottom border-lg-bottom-0 border-white-fade">
                    <jsp:attribute name="navbar_collapse_end">
                        <component:search-form classes="d-flex"/>
                        <button class="btn btn-primary mx-lg-2 me-2 my-2" type="button" onclick="window.location.href = '/login'">
                            Sign in
                        </button>
                        <button class="btn btn-success me-2 my-2" type="button" onclick="window.location.href = '/register'">
                            Sign up
                        </button>
                    </jsp:attribute>
                </component:nav-collapse>
            </div>
        </nav>
    </c:when>
    <c:otherwise>
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary border-1 border-info border-bottom sticky-top">
            <div class="container-fluid px-lg-4 py-lg-1 px-md-2">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <component:brand-logo/>
                <a class="d-inline-block d-lg-none me-1 "><i id="write" class="fas fa-pen-alt fa-lg me-1"></i></a>
                <component:nav-collapse
                        navbar_nav_classes="me-auto mt-4 mb-2 my-lg-0"
                        nav_item_1="border-top border-lg-top-0 border-white-fade"
                        nav_item_2="dropdown border-top border-lg-top-0 border-white-fade"
                        nav_item_3="border-top border-lg-top-0 border-white-fade"
                        nav_item_4="border-top border-lg-top-0 border-white-fade">
                    <jsp:attribute name="navbar_collapse_start">
                        <component:search-form classes="d-flex me-lg-2 mt-3 mt-lg-0"/>
                    </jsp:attribute>
                    <jsp:attribute name="navbar_nav_end">
                        <li class="nav-item d-lg-none border-top border-lg-top-0 border-white-fade">
                            <a class="nav-link" href="#">
                                <img src="<c:url value="/user/profile/${user.id}"/>" alt="${user.username}" class="user-avatar-sm rounded-circle me-1">
                                    ${user.name}
                            </a>
                        </li>
                        <li class="nav-item d-lg-none border-top border-lg-top-0 border-white-fade">
                            <a class="nav-link" href="<c:url value="/user/logout"/>" onclick="signOut()">
                                <i class="fas fa-sign-out-alt ms-2 me-2"></i>Sign out</a>
                        </li>
                    </jsp:attribute>
                    <jsp:attribute name="navbar_collapse_end">
                        <ul class="navbar-nav d-none d-lg-inline-flex">
                            <li class="nav-item dropdown notification">
                                <a class="nav-link nav-icons" href="#" id="navbarDropdown2" role="button" data-bs-toggle="dropdown"
                                   aria-expanded="false">
                                    <i id="bell" class="fas fa-fw fa-bell"></i>
                                    <span class="indicator"></span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end notification-dropdown">
                                    <li>
                                        <div class="notification-title">Notification</div>
                                        <div class="notification-list">
                                            <div class="list-group">
                                                <a href="#" class="list-group-item list-group-item-action active">
                                                    <div class="notification-info">
                                                        <div class="notification-list-user-img">
                                                            <img src="https://img.icons8.com/office/100/000000/administrator-female.png"
                                                                 alt="" class="user-avatar-md rounded-circle">
                                                        </div>
                                                        <div class="notification-list-user-block">
                                                            <span class="notification-list-user-name">Shivang Verma</span>accepted
                                                            your invitation to join the team.
                                                            <div class="notification-date">2 min ago</div>
                                                        </div>
                                                    </div>
                                                </a>
                                                <a href="#" class="list-group-item list-group-item-action">
                                                    <div class="notification-info">
                                                        <div class="notification-list-user-img">
                                                            <img src="https://img.icons8.com/color/48/000000/administrator-female.png"
                                                                 alt="" class="user-avatar-md rounded-circle">
                                                        </div>
                                                        <div class="notification-list-user-block">
                                                            <span class="notification-list-user-name">Vivek Dubey</span>
                                                            updated the email address
                                                            <div class="notification-date">2 days ago</div>
                                                        </div>
                                                    </div>
                                                </a>
                                                <a href="#" class="list-group-item list-group-item-action">
                                                    <div class="notification-info">
                                                        <div class="notification-list-user-img">
                                                            <img src="https://img.icons8.com/color/100/000000/name.png" alt=""
                                                                 class="user-avatar-md rounded-circle">
                                                        </div>
                                                        <div class="notification-list-user-block">
                                                            <span class="notification-list-user-name">Abhishek Singh</span>
                                                            is watching your main repository
                                                            <div class="notification-date">2 min ago</div>
                                                        </div>
                                                    </div>
                                                </a>
                                            </div>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="list-footer"><a href="#">View all notifications</a></div>
                                    </li>
                                </ul>
                            </li>
                            <li class="nav-item dropdown nav-user">
                                <a class="nav-link dropdown-toggle nav-user-img" href="#" id="navbarDropdown3" role="button"
                                   data-bs-toggle="dropdown" aria-expanded="false">
                                    <img src="<c:url value="/user/profile/${user.id}"/>" alt="${user.username}"
                                         class="user-avatar-md rounded-circle">
                                </a>
                                <div class="dropdown-menu dropdown-menu-end nav-user-dropdown" aria-labelledby="navbarDropdown3">
                                    <div class="nav-user-info">
                                        <h5 class="mb-0 text-white nav-user-name">${user.name}</h5>
                                        <span class="status"></span><span class="ml-2">Online</span>
                                    </div>
                                    <a class="dropdown-item mt-1" href="<c:url value="/settings/profile"/>">
                                        <i class="fas fa-user me-1"></i>Account
                                    </a>
                                    <div role="none" class="dropdown-divider"></div>
                                    <a class="dropdown-item" href="#"><i class="fas fa-history me-1"></i>History</a>
                                    <a class="dropdown-item" href="#"><i class="fab fa-blogger-b me-1"></i>Your blogs</a>
                                    <a class="dropdown-item" href="#"><i class="fas fa-save me-1"></i>Saved</a>
                                    <a class="dropdown-item" href="#"><i class="far fa-clock me-1"></i>Read later</a>
                                    <a class="dropdown-item" href="#"><i class="fas fa-thumbs-up me-1"></i>Liked blogs</a>
                                    <div role="none" class="dropdown-divider"></div>
                                    <a class="dropdown-item" href="<c:url value="/settings/profile"/>"><i class="fas fa-cog me-1"></i>Setting</a>
                                    <div role="none" class="dropdown-divider"></div>
                                    <a class="dropdown-item mb-1" href="<c:url value="/user/logout"/>"
                                       onclick="signOut()"><i class="fas fa-power-off me-1"></i>Logout</a>
                                </div>
                            </li>
                        </ul>
                    </jsp:attribute>
                </component:nav-collapse>
            </div>
        </nav>
    </c:otherwise>
</c:choose>