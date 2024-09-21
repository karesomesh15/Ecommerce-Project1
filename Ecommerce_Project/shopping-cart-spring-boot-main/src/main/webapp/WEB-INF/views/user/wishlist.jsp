<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../base.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>Wishlist</title>
    <!-- Add your CSS files here -->
</head>
<body>
    <!-- Display messages if any -->
    <c:if test="${not empty succMsg}">
        <div class="alert alert-success">
            ${succMsg}
        </div>
        <c:remove var="succMsg" />
    </c:if>

    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger">
            ${errorMsg}
        </div>
        <c:remove var="errorMsg" />
    </c:if>

    <section>
        <div class="container mt-5" style="margin-top: 100px !important;"> <!-- Adjust the margin here -->
            <h3>Your Wishlist</h3>
            <c:if test="${not empty wishlist}">
                <div class="row">
                    <c:forEach var="item" items="${wishlist}">
                        <div class="col-md-3 mt-2">
                            <div class="card">
                                <div class="card-body text-center">
                                    <img alt="" src="/img/product_img/${item.product.image}" width="150px" height="150px">
                                    <p class="fs-5 text-center">${item.product.title}</p>
                                    <a href="/product/${item.product.id}" class="btn btn-primary col-md-6 offset-md-3">
                                        View Details
                                    </a>
                                    <a href="/wishlist/remove?productId=${item.product.id}" class="btn btn-danger col-md-6 offset-md-3 mt-2">
                                        Remove from Wishlist
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty wishlist}">
                <p class="text-center">Your wishlist is empty</p>
            </c:if>
        </div>
    </section>
</body>
</html>