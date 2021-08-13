<%@ tag description="Navbar supported content template" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="navbar_collapse_start" fragment="true" %>
<%@ attribute name="navbar_collapse_end" fragment="true" %>
<%@ attribute name="navbar_nav_start" fragment="true" %>
<%@ attribute name="navbar_nav_end" fragment="true" %>
<%@ attribute name="navbar_nav_classes" required="true" rtexprvalue="true" %>
<%@ attribute name="nav_item_1" required="true" rtexprvalue="true" %>
<%@ attribute name="nav_item_2" required="true" rtexprvalue="true" %>
<%@ attribute name="nav_item_3" required="true" rtexprvalue="true" %>
<%@ attribute name="nav_item_4" required="true" rtexprvalue="true" %>

<div class="collapse navbar-collapse" id="navbarSupportedContent">

    <jsp:invoke fragment="navbar_collapse_start"/>

    <ul class="navbar-nav ${navbar_nav_classes}">

        <jsp:invoke fragment="navbar_nav_start"/>

        <li class="nav-item ${nav_item_1}">
            <a class="nav-link active" aria-current="page" href="<c:url value="/home"/>"><i class="fas fa-home me-1"></i>Home</a>
        </li>
        <li class="nav-item ${nav_item_2}">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
               data-bs-toggle="dropdown" aria-expanded="false">
                <i class="fas fa-check-square me-1"></i>Categories
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                <li><a class="dropdown-item" href="#">Programming</a></li>
                <li><a class="dropdown-item" href="#">Data Structure</a></li>
                <li><a class="dropdown-item" href="#">Algorithms</a></li>
                <li>
                    <hr class="dropdown-divider">
                </li>
                <li><a class="dropdown-item" href="#">View all...</a></li>
            </ul>
        </li>
        <li class="nav-item ${nav_item_3}">
            <a class="nav-link" href="#"><i class="fas fa-pen-alt me-1"></i>Write</a>
        </li>
        <li class="nav-item ${nav_item_4}">
            <a class="nav-link" href="#"><i class="fas fa-users me-1"></i>About</a>
        </li>

        <jsp:invoke fragment="navbar_nav_end"/>

    </ul>

    <jsp:invoke fragment="navbar_collapse_end"/>

</div>