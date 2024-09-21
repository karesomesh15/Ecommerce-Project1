<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@include file="../base.jsp" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="ISO-8859-1">

    <title>My Orders</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">

	<style>

		table{

			margin-top:1rem;

			box-shadow:8px 8px 8px black;

			}

			.btn{

				width:100px; 

				height:40px; 

				background-color:red !important;

				color:white;

				font-size:17px;

			}

		

	</style>

</head>

<body>

    <section>

        <div class="container mt-5 p-5">

            <div class="row">

                <p class="text-center fs-3" style="font-weight:bold">My Orders</p>



                <c:if test="${sessionScope.succMsg != null}">

                    <p class="text-success fw-bold text-center">${sessionScope.succMsg}</p>

                    <c:out value="${sessionScope.succMsg}"/>

                    <c:out value="${commnServiceImpl.removeSessionMessage()}"/>

                </c:if>



                <c:if test="${sessionScope.errorMsg != null}">

                    <p class="text-danger fw-bold text-center">${sessionScope.errorMsg}</p>

                    <c:out value="${sessionScope.errorMsg}"/>

                    <c:out value="${commnServiceImpl.removeSessionMessage()}"/>

                </c:if>



                <div class="col-md-12">

                    <table class="table">

                        <thead>

                            <tr>

                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:20px">Order Id</th>

                                <th scope="col" style="background-color:black; color:white;text-align:center;font-size:20px">Date</th>

                                <th scope="col" style="background-color:black; color:white;text-align:center;font-size:20px">Product Details</th>

                                <th scope="col" style="background-color:black; color:white;text-align:center;font-size:20px">Price</th>

                                <th scope="col" style="background-color:black; color:white;text-align:center;font-size:20px">Status</th>

                                <th scope="col" style="background-color:black; color:white;text-align:center;font-size:20px">Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            <c:forEach var="o" items="${orders}">

                                <tr>

                                    <th scope="row">${o.orderId}</th>

                                    <td>${o.orderDate}</td>

                                    <td>${o.product.title}</td>

                                    <td>

                                        <strong>Quantity :</strong> ${o.quantity} <br>

                                        <strong>Price :</strong> ${o.price} <br>

                                        <strong>Total Price :</strong> ${o.quantity * o.price}

                                    </td>

                                    <td>${o.status}</td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${o.status != 'Cancelled'}">

                                                <a href="${pageContext.request.contextPath}/order/update-status?id=${o.id}&st=6" class="btn btn-sm btn-danger">Cancel</a>

                                            </c:when>

                                            <c:otherwise>

                                                <a href="#" class="btn btn-sm  disabled" >Cancel</a>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                </tr>

                            </c:forEach>

                        </tbody>

                    </table>

                </div>

            </div>

        </div>

    </section>

</body>

</html>