<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../base.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>Order Success</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <section>
        <div class="container mt-5 p-5">
            <div class="row">
                <div class="col-md-6 offset-md-3 text-center">
                    <i class="fa-solid fa-circle-check fa-5x text-success"></i>
                    <p class="fs-3">Product Ordered Successfully</p>
                    <p class="fs-4">Product will be delivered within 7 days</p>
                    <div class="text-center">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Home</a>
                        <a href="${pageContext.request.contextPath}/order/user-orders" class="btn btn-primary">Your Order</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
