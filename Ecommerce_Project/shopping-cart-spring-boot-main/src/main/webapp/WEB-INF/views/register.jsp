<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Register</title>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">

<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">

<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>

<script
	src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<style>
* {
	margin: 0px;
	padding: 0px;
}

.registerbox img {
	width: 100%;
	height: 750px;
}

.rbox {
	position: absolute;
	width: 70%;
	height: 710px;
	top: 4%;
	left: 15%;
	box-shadow: 12px 8px 8px black;
	border-radius: 10px;
	background-color: white;
}

.rbox h1 {
	text-align: center;
	font-size: 40px;
	text-transform: uppercase;
	margin-top: 1rem;
	letter-spacing: 2px;
}

.rbox .inpbox {
	width: 90%;
	height: 90px;
	margin-top: 2rem;
	margin-left: 2.5rem;
	display: flex;
	flex-direction: row;

	/* background-color: blueviolet; */
}

.rbox .inpbox .input-field {
	width: 50%;
	height: 90px;
	display: block;

	/* border: 2px solid black;

background-color: brown; */
}

.rbox .inpbox .input-field label {
	font-size: 20px;
	margin-left: 10px;
	padding-top: 1rem;
	font-weight: 799;
}

.rbox .inpbox .input-field input {
	width: 90%;
	margin: 10px;
	height: 40px;
	border-radius: 10px;
	border: 2px solid black;
	outline: none;
	padding-left: 10px;
	font-size: 15px;
}

.rbox .inpbox .input-field input:focus {
	border: none;
	outline: 2px solid blue;
}

button {
	width: 500px;
	height: 50px;
	border-radius: 10px;
	background-color: blue;
	border: none;
	outline: none;
	color: white;
	font-size: 20px;
	margin-left: 15rem;
	margin-top: 40px;
}

.rbox h3 {
	margin-top: 15px;
	text-align: center;
	font-size: 20px;
}

.rbox a {
	text-decoration: none;
}

.rbox .inpbox .input-field label {
	font-size: 20px;
	margin-left: 10px;
	padding-top: 1rem;
	font-weight: 799;
}
</style>

</head>

<body>

	<!-- Display Success Message -->

	<c:if test="${not empty sessionScope.succMsg}">

		<div class="alert alert-success" role="alert">

			${sessionScope.succMsg}

			<c:remove var="sessionScope.succMsg" />

		</div>

	</c:if>

	<!-- Display Error Message -->

	<c:if test="${not empty sessionScope.errorMsg}">

		<div class="alert alert-danger" role="alert">

			${sessionScope.errorMsg}

			<c:remove var="sessionScope.errorMsg" />

		</div>

	</c:if>

	<form class="form" action="/saveUser" enctype="multipart/form-data"
		method="post">

		<!-- Row 1: Full Name and Mobile Number -->

		<div class="registerbox">

			<img
				src="https://wallpapers.com/images/hd/plain-light-blue-background-3440-x-1440-uzm4ioklgkh72jmt.jpg">

			<div class="rbox">

				<h1>Register</h1>

				<div class="inpbox">

					<div class="input-field">

						<label for="name">Full Name</label> <input type="text" id="name"
							placeholder="Enter your Full Name" type="text" name="name"
							required>

					</div>

					<div class="input-field">

						<label for="mobileNumber">Mobile Number</label> <input
							id="mobileNumber" placeholder="Enter your Mobile Number"
							type="number" name="mobileNumber" required>

					</div>

				</div>

				<div class="inpbox">

					<div class="input-field">

						<label for="email">Email</label> <input id="email"
							placeholder="Enter your Email" type="email" name="email" required>

					</div>

					<div class="input-field">

						<label for="address">Address</label> <input id="address"
							placeholder="Enter your Address" type="text" name="address"
							required>

					</div>

				</div>

				<div class="inpbox">

					<div class="input-field">

						<label for="city">City</label> <input id="city"
							placeholder="Enter your City" type="text" name="city" required>

					</div>

					<div class="input-field">

						<label for="state">State</label> <input id="state"
							placeholder="Enter your State" type="text" name="state" required>

					</div>

					<div class="input-field">

						<label for="pincode">Pincode</label> <input id="pincode"
							placeholder="Enter your Pincode" type="number" name="pincode"
							required>

					</div>

				</div>

				<div class="inpbox">

					<div class="input-field">

						<label for="password">Password</label> <input id="password"
							placeholder="Enter your Password" type="password" name="password"
							required>

					</div>

					<div class="input-field">

						<label for="confirmpassword">Confirm Password</label> <input
							id="confirmpassword" placeholder="Confirm your Password"
							type="password" name="confirmpassword" required>

					</div>

					<div class="input-field">

						<label for="img">Profile Image</label> <input id="img" type="file"
							name="img" style="padding-top: 5px;">

					</div>

				</div>

				<button type="submit">Register</button>

				<h3>
					Have an account? <a href="/signin" class="text-decoration-none">Login</a>
				</h3>



			</div>

		</div>

	</form>

	<!-- Bootstrap JS Bundle with Popper -->

	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js"></script>

	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>

</body>

</html>