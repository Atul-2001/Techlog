<%@ page session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="mt" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="cfn" uri="/WEB-INF/tld/UserTag" %>

<mt:base title="Techlog" user="${cfn:getSessionAttribute(pageContext.request.session, 'user')}">
    <jsp:attribute name="head">
        <link href="<c:url value="/assets/stylesheets/style.css"/>" rel="stylesheet">
    </jsp:attribute>
    <jsp:attribute name="content">
        <c:set var="message" scope="page" value="${pageContext.request.session.getAttribute('message')}" />
        <c:if test="${message != null}">
            <div class="alert flash flash-full flash-info alert-dismissible fade show mb-0 ps-4" role="alert">
                <i class="fas fa-info-circle fa-lg d-none octicon octicon-info" aria-hidden="true"></i>
                <i class="fas fa-exclamation-circle fa-lg d-none octicon octicon-warn" aria-hidden="true"></i>
                <i class="fas fa-exclamation-triangle fa-lg d-none octicon octicon-alert" aria-hidden="true"></i>
                <c:out value="${message.content}" />
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            ${pageContext.request.session.invalidate()} 
        </c:if>
        <section class="header bg-primary bg-gradient">
            <div class="container-fluid d-flex justify-content-end py-4">
                <div class="container text-white">
                    <h6 class="display-6">Welcome to Techlog</h6>
                    <h2 class="display-2 pb-5 fw-bold">Where good ideas find you</h2>

                    <blockquote class="blockquote">
                        Read and share new perspectives on just about any topic.<br>Everyone’s welcome.
                    </blockquote>
                    <button class="btn btn-success btn-lg" onclick="document.location.href = '/join'">
                        Sign up for Techlog
                    </button>
                </div>
                <picture class="d-none d-sm-block me-5">
                    <img src="<c:url value="/assets/images/satellite.svg"/>" class="img-fluid h-75" alt="satellite" width="512px">
                </picture>
            </div>

            <div>
                <svg class="waves" xmlns="http://www.w3.org/2000/svg"
                     xmlns:xlink="http://www.w3.org/1999/xlink"
                     viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
                    <defs>
                        <path id="gentle-wave"
                              d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"></path>
                    </defs>
                    <g class="parallax">
                        <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(255,255,255,0.7"></use>
                        <use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.5)"></use>
                        <use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.3)"></use>
                        <use xlink:href="#gentle-wave" x="48" y="7" fill="#fff"></use>
                    </g>
                </svg>
            </div>
        </section>
        <section class="container">
            <div class="row mb-2">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Java Programming</h5>
                            <p class="card-text">
                                Some quick example text to build on the card title and
                                make up the bulk of the card's content.
                            </p>
                            <a href="#" class="btn primary-background text-white">Read more</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Java Programming</h5>
                            <p class="card-text">
                                Some quick example text to build on the card title and
                                make up the bulk of the card's content.
                            </p>
                            <a href="#" class="btn primary-background text-white">Read more</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Java Programming</h5>
                            <p class="card-text">
                                Some quick example text to build on the card title and
                                make up the bulk of the card's content.
                            </p>
                            <a href="#" class="btn primary-background text-white">Read more</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Java Programming</h5>
                            <p class="card-text">
                                Some quick example text to build on the card title and
                                make up the bulk of the card's content.
                            </p>
                            <a href="#" class="btn primary-background text-white">Read more</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Java Programming</h5>
                            <p class="card-text">
                                Some quick example text to build on the card title and
                                make up the bulk of the card's content.
                            </p>
                            <a href="#" class="btn primary-background text-white">Read more</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Java Programming</h5>
                            <p class="card-text">
                                Some quick example text to build on the card title and
                                make up the bulk of the card's content.
                            </p>
                            <a href="#" class="btn primary-background text-white">Read more</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </jsp:attribute>
</mt:base>