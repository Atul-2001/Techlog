<%@ tag description="Matser Page template" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags" %>

<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="head" fragment="true" %>
<%@ attribute name="content" fragment="true" %>
<%@ attribute name="script" fragment="true" %>
<%@ attribute name="user" required="false" type="com.signature.techlog.model.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link href="<c:url value="/assets/stylesheets/base.css"/>" rel="stylesheet">
    <link href="<c:url value="/assets/stylesheets/components/navbar.css"/>" rel="stylesheet">
    <jsp:invoke fragment="head"/>
    <script src="<c:url value="/assets/scripts/level.js"/>"></script>
    <script src="https://kit.fontawesome.com/4418ec4da8.js" crossorigin="anonymous"></script>
    <script src="https://apis.google.com/js/api:client.js" async defer></script>
    <script>
        window.onload = function () {
            gapi.load('auth2', function () {
                auth2 = gapi.auth2.init({
                    client_id: '[YOUR CLIENT ID]',
                    cookiepolicy: 'single_host_origin',
                });
            });
        };

        function signOut() {
            const auth2 = gapi.auth2.getAuthInstance();
            auth2.signOut();
        }
    </script>
</head>
<body>

<nav:navbar user="${user}"/>

<jsp:invoke fragment="content"/>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
        integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"
        integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT"
        crossorigin="anonymous"></script>
<jsp:invoke fragment="script"/>
</body>
</html>