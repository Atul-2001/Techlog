<%@ tag description="Query form template for navbar" pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="classes" required="true" rtexprvalue="true" %>

<form class="${classes}">
    <div class="input-group input-group-sm">
        <input class="form-control bg-transparent" type="search" placeholder="Search Techlog"
               aria-label="Search" autocapitalize="off">
        <button class="btn btn-outline-light" type="submit"><i class="fas fa-search"></i></button>
    </div>
</form>