<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<%@include file="base.jsp" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="ISO-8859-1">

    <title>Product Details</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    <script>

        function updateQuantity(change) {

            let qtyInput = document.getElementById('quantity');

            let currentQty = parseInt(qtyInput.value);

            let newQty = currentQty + change;

            if (newQty < 1) newQty = 1; // Ensure quantity is at least 1

            qtyInput.value = newQty;

            updateOrderQuantity();

        }



        function updateOrderQuantity() {

            document.getElementById('orderQuantity').value = document.getElementById('quantity').value;

            document.getElementById('addCartQuantity').value = document.getElementById('quantity').value;

        }

    </script>

    <style>

        .quantity-controls {

            display: flex;

            align-items: center;

            justify-content: center;

        }

        .quantity-controls i {

            cursor: pointer;

        }

        .quantity-controls input {

            width: 60px;

            text-align: center;

        }

    </style>

</head>

<body>

    <section>

        <div class="container card-sh" style="margin-top: 70px; margin-bottom: 100px">

            <div class="col-md-12 p-5">

                <div class="row">

                    <c:if test="${sessionScope.succMsg != null}">

                        <p class="text-success alert alert-success text-center" role="alert">${sessionScope.succMsg}</p>

                        <c:out value="${commnServiceImpl.removeSessionMessage()}"/>

                    </c:if>



                    <c:if test="${sessionScope.errorMsg != null}">

                        <p class="text-danger text-center alert alert-danger">${sessionScope.errorMsg}</p>

                        <c:out value="${commnServiceImpl.removeSessionMessage()}"/>

                    </c:if>



                    <div class="col-md-6 text-end">

                        <img alt="" src="${pageContext.request.contextPath}/img/product_img/${product.image}" style="width:80%; height:700; margin-right:5rem;margin-top:20px;">

                    </div>



                    <div class="col-md-6">

                        <p class="fs-3" style="font-weight:599;">${product.title}</p>

                        <p>

                            <span class="fw-bold" style="font-size:20px;">Description :</span><br>${product.description}

                        </p>

                        <p>

                            <span class="fw-bold" style="font-size:20px;">Product Details:</span><br>

                            <strong style="font-size:20px;">Status:</strong>

                            <c:choose>

                                <c:when test="${product.stock > 0}">

                                    <span class="badge bg-success" style="height:25px;font-size:17px;">Available</span>

                                </c:when>

                                <c:otherwise>

                                    <span class="badge bg-warning">Out of Stock</span>

                                </c:otherwise>

                            </c:choose>

                            <br><strong style="font-size:20px;">Category:</strong> ${product.category}<br>

                            <strong style="font-size:20px;">Policy:</strong> 7 Days Replacement & Return

                        </p>

                        <p class="fs-5 fw-bold">

                            Price: &nbsp;

                            <i class="fas fa-rupee-sign"></i> ${product.discountPrice} 

                            <span class="fs-6 text-decoration-line-through text-secondary">${product.price}</span>

                            <span class="fs-6 text-success">${product.discount}% off</span>

                        </p>



                        <div class="row mb-3">

                            <div class="col-md-6 quantity-controls">

                                <i class="fas fa-minus fa-2x" onclick="updateQuantity(-1)"></i>

                                <input type="number" id="quantity" name="quantity" value="1" min="1" onchange="updateOrderQuantity()" class="form-control mx-2">

                                <i class="fas fa-plus fa-2x" onclick="updateQuantity(1)"></i>

                            </div>

                        </div>



                        <div class="row">

                            <div class="col-md-6">

                                <!-- Add To Cart Button -->

                                <form action="${pageContext.request.contextPath}/cart/add" method="get">

                                    <input type="hidden" name="pid" value="${product.id}">

                                    <input type="hidden" name="quantity" id="addCartQuantity" value="1">

                                    <button type="submit" class="btn btn-danger col-md-12" style="height:50px;font-size:20px;">Add To Cart</button>

                                </form>

                            </div>

                            <div class="col-md-6">

                                <!-- Buy Now Button -->

                                <form action="${pageContext.request.contextPath}/order/orderNow" method="get">

                                    <input type="hidden" name="pid" value="${product.id}">

                                    <input type="hidden" name="quantity" id="orderQuantity" value="1">

                                    <button type="submit" class="btn col-md-12" onclick="updateOrderQuantity()" style="height:50px;font-size:20px;background-color:blue;color:white">Buy Now</button>

                                </form>

                            </div>

                        </div>



                        <!-- Review Form -->

                        <div class="mt-5">

                            <h3>Leave a Review:</h3>

                            <c:if test="${user != null}">

                                <form action="${pageContext.request.contextPath}/saveReview" method="post">

                                    <input type="hidden" name="productId" value="${product.id}">

                                    <input type="hidden" name="userId" value="${user.id}">

                                    <div class="form-group">

                                        <label for="rating" style="font-weight:bold;">Rating:</label>

                                        <input type="number" name="rating" id="rating" class="form-control" min="1" max="5" style="border:1px solid black;" required>

                                    </div>

                                    <div class="form-group mt-2">

                                        <label for="comment" style="font-weight:bold;">Comment:</label>

                                        <textarea name="comment" id="comment" class="form-control" rows="4" style="border:1px solid black;" required></textarea>

                                    </div>

                                    <button type="submit" class="btn btn-primary mt-3" style="height:50px;font-size:20px;">Submit Review</button>

                                </form>

                            </c:if>

                            <c:if test="${user == null}">

                                <p>Please <a href="${pageContext.request.contextPath}/signin">sign in</a> to leave a review.</p>

                            </c:if>

                        </div>



                        <!-- Display Reviews -->

                        <div class="mt-5">

                            <h3>Reviews:</h3>

                            <c:forEach var="review" items="${reviews}">

                                <div class="border p-3 mb-3">

                                    <p><strong>${review.user.name}</strong> - <span>${review.rating} Stars</span></p>

                                    <p>${review.comment}</p>

                                    <p><small>Date: ${fn:substring(review.date, 0, 19)}</small></p>

                                </div>

                            </c:forEach>

                            <c:if test="${empty reviews}">

                                <p>No reviews yet. Be the first to leave a review!</p>

                            </c:if>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    </section>

</body>

</html>