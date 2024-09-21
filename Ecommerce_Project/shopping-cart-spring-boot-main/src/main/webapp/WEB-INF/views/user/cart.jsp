<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../base.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>Cart Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
    <section>
        <div class="container-fluid mt-5 p-5">
            <div class="card card-sh">
                <div class="card-header text-center" style="background-color: lightgrey;">
                    <p class="fs-4" style="font-weight: 599; font-size: 30px !important; text-transform: uppercase; color: blue;">Cart Page</p>

                    <!-- Display success message -->
                    <c:if test="${not empty sessionScope.succMsg}">
                        <p class="text-success fw-bold">${sessionScope.succMsg}</p>
                        <c:remove var="sessionScope.succMsg" />
                    </c:if>

                    <!-- Display error message -->
                    <c:if test="${not empty sessionScope.errorMsg}">
                        <p class="text-danger fw-bold">${sessionScope.errorMsg}</p>
                        <c:remove var="sessionScope.errorMsg" />
                    </c:if>
                </div>

                <div class="card-body">
                    <form id="cartForm" action="${pageContext.request.contextPath}/cart/update" method="post">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th scope="col" style="background-color: black; color: white;">Sr No</th>
                                    <th scope="col" style="background-color: black; color: white">Image</th>
                                    <th scope="col" style="background-color: black; color: white">Product Name</th>
                                    <th scope="col" style="background-color: black; color: white">Price</th>
                                    <th scope="col" class="text-center" style="background-color: black; color: white">Quantity</th>
                                    <th scope="col" style="background-color: black; color: white">Total Price</th>
                                    <th scope="col" style="background-color: black; color: white">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cart" items="${carts}" varStatus="status">
                                    <tr>
                                        <th scope="row">${status.index +1 }</th>
                                        <td><img src="${pageContext.request.contextPath}/img/product_img/${cart.product.image}" width="70px" height="70px"></td>
                                        <td>${cart.product.title}</td>
                                        <td>${cart.product.discountPrice}</td>
                                        <td class="text-center">
                                            <span>${cart.quantity}</span>
                                            <!-- Hidden input for form submission -->
                                            <input type="hidden" id="updateCartQuantity_${cart.id}" name="updateCartQuantity[${cart.id}]" value="${cart.quantity}">
                                        </td>
                                        <td id="totalPrice_${cart.id}">${cart.totalPrice}</td>
                                        <td>
                                            <button type="submit" name="remove" value="${cart.id}" style="background-color: red; border: none; outline: none; width: 100px; height: 40px; color: white;">Remove</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td colspan="5" class="fw-bold text-end">Total Price</td>
                                    <td class="fw-bold">&#8377; ${totalOrderPrice}</td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                    <div class="text-center" style="width: 160px; height: 33px; margin-left: 30rem; margin-top: 10px; background-color: blue;">
                        <a href="${pageContext.request.contextPath}/order/orders" style="color: white; text-align: center; font-size: 17px; text-decoration: none;">Proceed Payment</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
