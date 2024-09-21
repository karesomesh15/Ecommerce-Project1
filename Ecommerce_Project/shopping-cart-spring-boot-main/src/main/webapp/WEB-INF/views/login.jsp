<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<title>Login Page</title>

<!-- Add Bootstrap CSS -->

<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/5.3.0/css/bootstrap.min.css"
	rel="stylesheet">

<style>
* {
	margin: 0px;
	padding: 0px;
}

.mbox img {
	width: 100%;
	height: 638px;
}

.box {
	position: absolute;
	width: 70%;
	height: 500px;
	top: 10%;
	left: 15%;
	display: flex;
	flex-direction: row;
	border-radius: 10px;
	box-shadow: 12px 8px 8px black;
	background-color: rgba(255, 255, 255, 0.486);
}

.box .desc {
	width: 60%;
	height: 500px;
	border-top-left-radius: 10px;
	border-bottom-left-radius: 10px;
	background-color: white;
}

.box .desc h1 {
	text-align: center;
	margin-top: 20px;
	letter-spacing: 2px;
	text-transform: uppercase;
}

.box .desc img {
	width: 80%;
	height: 380px;
	margin: 1rem 3rem;
}

.box .loginbox {
	width: 40%;
	height: 500px;
	border-top-right-radius: 10px;
	border-bottom-right-radius: 10px;
	background-color: white;
}

.box .loginbox .loginb {
	width: 90%;
	height: 400px;
	margin-top: 4rem;
	border-radius: 10px;
	background: transparent;
	border: 2px solid black;
}

.box .loginbox .loginb h1 {
	text-align: center;
	margin-top: 2rem;
	letter-spacing: 2px;
	text-transform: uppercase;
}

.loginbox .loginb input {
	width: 83%;
	height: 40px;
	font-size: 15px;
	margin-top: 2rem;
	margin-left: 1.5rem;
	padding-left: 10px;
	transition: all ease-in 0.4s;
}

::placeholder {
	padding-left: 10px;
}

.loginbox .loginb input:focus {
	outline: 2px solid blue;
}

.loginbox .loginb a {
	text-decoration: none;
	margin-left: 30px;
	padding-top: 10px;
	font-weight: 599;
	font-size: 15px !important;
}

.loginbox .loginb button {
	width: 300px;
	height: 50px;
	background-color: rgba(0, 0, 255, 0.788);
	font-size: 20px;
	border: none;
	outline: none;
	color: white;
	border-radius: 10px;
	margin: 1rem 1.5rem;
	text-align: center;
	transition: all ease-in 0.4s;
}

.loginbox .loginb button:hover {
	cursor: pointer;
	background-color: blue;
}

.loginbox .loginb h3 {
	font-size: 20px;
	margin-left: 26px;
}

.loginbox .loginb .a1 {
	text-decoration: none;
	padding-top: 10px;
	margin-left: 0px;
	font-size: 20px !important;
}

/* Tablet View */
@media ( max-width : 768px) {
	.box {
		width: 90%;
		left: 5%;
		flex-direction: column;
		height: auto;
	}
	.box .desc, .box .loginbox {
		width: 100%;
		height: auto;
		border-radius: 0;
		margin: 0;
	}
	.box .desc img {
		width: 100%;
		margin: 1rem 0;
	}
	.box .loginbox .loginb {
		margin-top: 2rem;
	}
	button {
		width: 30% !important; /* Full width with some padding */
		/* Center the button */
		height: 45px; /* Adjust height */
		font-size: 18px; /* Slightly smaller font size */
	}
}

/* Mobile View */
@media ( max-width : 480px) {
	.box {
		width: 100%;
		top: 5%;
		left: 0;
		box-shadow: none;
	}
	.box .desc h1, .box .loginbox .loginb h1 {
		font-size: 18px;
		letter-spacing: 1px;
		margin-top: 10px;
	}
	.box .desc img {
		width: 90%;
		margin: 1rem auto;
	}
	.loginbox .loginb input {
		width: 90%;
		margin-left: 5%;
		font-size: 14px;
	}
	.loginbox .loginb button {
		width: 60%;
		margin-left: 5%;
		height: 45px;
		font-size: 18px;
	}
}
</style>

</head>

<body>

	<div class="container center-container">

		<div class="row justify-content-center">

			<div class="col-md-6">

				<div class="card shadow p-3 mb-5 bg-body-tertiary rounded">

					<div class="card-header text-center">

						<!-- Error Message -->

						<c:if test="${param.error != null}">

							<div class="alert alert-danger">

								<h1 style="color: red;">
									<c:out
										value="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}" />
								</h1>

							</div>

						</c:if>

						<!-- Success Message -->

						<c:if test="${param.logout != null}">

							<div class="alert alert-success"></div>

						</c:if>

					</div>

					<div class="card-body">

						<form class="form"
							action="${pageContext.request.contextPath}/login" method="post">

							<div class="mbox">

								<img
									src="https://www.bettylukens.com/cdn/shop/products/Large-Blue-Background._grande.jpg?v=1571438714">

								<div class="box">

									<div class="desc">

										<h1>ONLINE SHOPPING</h1>

										<img
											src="https://5.imimg.com/data5/GQ/FP/MY-26802338/online-shopping-system-500x500.jpg">

									</div>

									<div class="loginbox">

										<div class="loginb">

											<h1>Login</h1>

											<input type="text" placeholder="Enter Your Email"
												name="username" required> <input type="password"
												placeholder="Enter Your Password" name="password" required><br>

											

											<button>Sign In</button>

											<h3>
												Don't have an account? <a href="/register" class="a1">Sign
													Up</a>
											</h3>

										</div>

									</div>

								</div>

							</div>

						</form>

					</div>

				</div>

			</div>

		</div>

	</div>

	<!-- Add Bootstrap JavaScript -->

	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>

</body>

</html>