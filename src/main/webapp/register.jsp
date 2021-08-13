<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib prefix="register" tagdir="/WEB-INF/tags" %>

<register:authentication title="Register - Techlog">
    <jsp:attribute name="script">
        <script>
            const div = document.querySelector(".container");
            div.classList.add("sign-up-mode");
            if (window.matchMedia("(max-width: 870px)").matches) {
                container.style.height = "120vh";
            } else if (window.matchMedia("(max-height: 620px)").matches) {
                container.style.height = "112vh";
            } else if (window.matchMedia("(max-height: 630px)").matches) {
                container.style.height = "114vh";
            } else if (window.matchMedia("(max-height: 640px)").matches) {
                container.style.height = "116vh";
            }
        </script>
    </jsp:attribute>
</register:authentication>