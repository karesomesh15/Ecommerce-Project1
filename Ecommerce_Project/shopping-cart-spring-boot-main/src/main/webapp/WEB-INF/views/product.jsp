<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="base.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Product Page</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
	<section>
		<div class="container-fluid bg-primary p-4 mt-5">
			<div class="row">
				<div class="col-md-8 offset-md-2">
					<form action="/products" method="get">
						<div class="input-group">
							<input type="text" class="form-control" name="ch">
							<button class="btn btn-light text-dark ms-3 col-md-2">
								<i class="fa-solid fa-magnifying-glass"></i> Search
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>

		<div class="container-fluid mt-1">
			<div class="row">
				<div class="col-md-2 p-0">
					<div class="card shadow-sm p-0 mb-5 bg-body-tertiary rounded">
						<div class="card-body">
							<div class="list-group">
								<p class="fs-5">Category</p>
								<a href="/products"
									class="list-group-item list-group-item-action 
                                    ${paramValue == '' ? 'active' : ''}"
									aria-current="true">All</a>
								<c:forEach var="c" items="${categories}">
									<a href="/products?category=${c.name}"
										class="list-group-item list-group-item-action
                                       ${paramValue == c.name ? 'active' : ''}">
										${c.name} </a>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-10">
					<div class="card">
						<div class="card-body">
							<p class="fs-3 text-center">Products</p>
							<div class="row">
								<c:if test="${productsSize > 0}">
									<c:forEach var="p" items="${products}">
										<div class="col-md-3 mt-2">
											<div class="card card-sh">
												<div class="card-body text-center">
													<img alt="" src="/img/product_img/${p.image}" width="150px"
														height="150px">
													<p class="fs-5 text-center">${p.title}</p>
													<div class="row text-center">
														<p class="fs-6 fw-bold">
															<span>&#8377; ${p.discountPrice}</span> <br> <span
																class="text-decoration-line-through text-secondary">
																&#8377; ${p.price} </span> <span class="fs-6 text-success">
																${p.discount}% off </span>
														</p>
														<a href="/product/${p.id}"
															class="btn btn-primary col-md-6 offset-md-3"> View
															Details </a>
														
															<a style="margin-top: 4px;"
																href="/wishlist/add?productId=${p.id}"
																class="btn btn-warning col-md-6 offset-md-3"> Add to
																Wishlist </a>
														
													</div>

												</div>
											</div>
										</div>
									</c:forEach>
								</c:if>
								<c:if test="${productsSize <= 0}">
									<p class="fs-4 text-center mt-4 text-danger">Product not
										available</p>
								</c:if>
							</div>
						</div>
					</div>
					<!-- Start Pagination -->
					<div class="row">
						<div class="col-md-4">Total Products : ${totalElements}</div>
						<div class="col-md-6">
							<c:if test="${productsSize > 0}">
								<nav aria-label="Page navigation example">
									<ul class="pagination">
										<li class="page-item ${isFirst ? 'disabled' : ''}"><a
											class="page-link" href="/products?pageNo=${pageNo - 1}"
											aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
										</a></li>
										<c:forEach var="i" begin="1" end="${totalPages}">
											<li class="page-item ${pageNo + 1 == i ? 'active' : ''}">
												<a class="page-link" href="/products?pageNo=${i - 1}">${i}</a>
											</li>
										</c:forEach>
										<li class="page-item ${isLast ? 'disabled' : ''}"><a
											class="page-link" href="/products?pageNo=${pageNo + 1}"
											aria-label="Next"> <span aria-hidden="true">&raquo;</span>
										</a></li>
									</ul>
								</nav>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>