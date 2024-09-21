<%@include file="../base.jsp" %>

<!DOCTYPE html>
<html >
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">

<style>
	.heading{
		text-align:center;
		font-size:40px;
		margin-top:3rem;
		margin-bottom:1rem;
		font-weight:bold;
		animation:anim 1.4s infinite;
	}
	@keyframes anim{
		0%{
			color:red;
		}
		50%{
			color:blue;
		}
		100%{
			color:green;
		}
	}
	.card-body{
		text-align:center;
		height:150px;
		box-shadow:8px 8px 8px black;
		background-color:white;
		transition:all ease-in 0.4s;
    	}
    	.card-body:hover{
    		cursor:pointer;
    		transform:scale(1.02);
    	}
	.card-body lable{
		text-align:center;
		margin-top:2.5rem;
		font-weight:bold;
		font-size:30px;
	}
	.card-body i{
		font-size:60px;
		margin-left:10px;
	}

</style>
</head>
<body>
	<section><br>
		<div class="container p-5" style="margin-top:1rem;">
			<p class="heading">Admin Dashboard</p>
			<div class="row ">
				<div class="col-md-4 mt-2">
					<a href="/admin/loadAddProduct" class="text-decoration-none">
						<div class="card card-sh">
							<div class="card-body text-center text-primary">
								<i class="fa-solid fa-square-plus fa-3x"></i>
								<h4>Add Product</h4>
							</div>
						</div>
					</a>
				</div>


				<div class="col-md-4 mt-2">
					<a href="/admin/category" class="text-decoration-none">
						<div class="card card-sh">
							<div class="card-body text-center text-primary">
								<i class="fa-solid fa-list fa-3x"></i>
								<h4>Add Category</h4>
							</div>
						</div>
					</a>
				</div>


				<div class="col-md-4 mt-2">
					<a href="/admin/products" class="text-decoration-none">
						<div class="card card-sh">
							<div class="card-body text-center text-primary">
								<i class="fa-solid fa-table-list fa-3x"></i>
								<h4>View Product</h4>
							</div>
						</div>
					</a>
				</div>


				<div class="col-md-4 mt-4">
					<a href="/admin/orders" class="text-decoration-none">
						<div class="card card-sh">
							<div class="card-body text-center text-primary">
								<i class="fa-solid fa-box-open fa-3x"></i>
								<h4>Orders</h4>
							</div>
						</div>
					</a>
				</div>


				<div class="col-md-4 mt-4">
					<a href="/admin/users?type=1" class="text-decoration-none">
						<div class="card card-sh">
							<div class="card-body text-center text-primary">
								<i class="fa-solid fa-circle-user fa-3x"></i>
								<h4>Users</h4>
							</div>
						</div>
					</a>
				</div>


				<div class="col-md-4 mt-4">
					<a href="/admin/add-admin" class="text-decoration-none">
						<div class="card card-sh">
							<div class="card-body text-center text-primary">
								<i class="fa-solid fa-user-plus fa-3x"></i>
								<h4>Add Admin</h4>
							</div>
						</div>
					</a>
				</div>

				<div class="col-md-4 mt-4">
					<a href="/admin/users?type=2" class="text-decoration-none">
						<div class="card card-sh">
							<div class="card-body text-center text-primary">
								<i class="fa-solid fa-circle-user fa-3x"></i>
								<h4>Admin</h4>
							</div>
						</div>
					</a>
				</div>
 

			</div>
		</div>
	</section>
</body>
</html>