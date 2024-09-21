<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../base.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<!-- Add any required CSS or JS files here -->
<style type="text/css">
	.row1{
		margin-left:5rem;
		width:90%;
		background-color:lightgrey;
		margin-top:5rem;
		box-shadow:8px 8px 8px black;
	}
	.head{
		font-weight:bold;
		margin-top:1rem;
		color:blue;
		font-size:30px !important
	}
	.mb-3 label{
		font-weight:699;
		margin-top:5px;
	}
	.mb-3 input[type=text],
	.mb-3 input[type=number]
	{
		margin-top:10px;
		height:40px;
		border:1px solid black;
	}
	
	.btn{
		background-color:blue;
		color:white;
		heigth:50px ;
		width:100%;
	}
	.btn:hover{
		color:white;
		background-color:blue;
	}
</style>
</head>
<body>
	<section>
		<div class="container p-5 mt-3">
			<div class="row1">
				<div class="" >
					<div class="card card-sh"  style="background-color:;">
						<div class="card-header text-center">
							<p class="head" >Add Product</p>

							<!-- Success Message -->
							<c:if test="${not empty sessionScope.succMsg}">
								<p class="text-success fw-bold">${sessionScope.succMsg}</p>
								<%
								session.removeAttribute("succMsg");
								%>
							</c:if>

							<!-- Error Message -->
							<c:if test="${not empty sessionScope.errorMsg}">
								<p class="text-danger fw-bold">${sessionScope.errorMsg}</p>
								<%
								session.removeAttribute("errorMsg");
								%>
							</c:if>

						</div>
						<div class="card-body" >
							<form
								action="${pageContext.request.contextPath}/admin/saveProduct"
								method="post" enctype="multipart/form-data">
								
								<div class="mb-3">
									<label>Enter Title</label> 
									<input type="text" name="title" class="form-control" required>
								</div>

								<div class="mb-3">
									<label>Enter Description</label>
									<textarea rows="3" style="border:1px solid black;" class="form-control" name="description" required></textarea>
								</div>

								<div class="mb-3">
									<label>Category</label> 
									<select style="border:1px solid black;" class="form-control" name="category" required>
										<option value="">--select--</option>
										<!-- Iterating through categories using JSTL -->
										<c:forEach var="c" items="${categories}">
											<option value="${c.name}">${c.name}</option>
										</c:forEach>
									</select>
								</div>

								<div class="mb-3">
									<label>Enter Price</label> 
									<input type="number" name="price" class="form-control" required>
								</div>

								<div class="mb-3">
									<label>Status</label>
									<div class="form-check">
										<input class="form-check-input" type="radio" value="true" name="isActive" id="flexRadioDefault1" required>
										<label class="form-check-label" for="flexRadioDefault1">Active</label>
									</div>
									<div class="form-check">
										<input class="form-check-input" type="radio" name="isActive" value="false" id="flexRadioDefault2">
										<label class="form-check-label" for="flexRadioDefault2">Inactive</label>
									</div>
								</div>

								<div class="row">
									<div class="mb-3 col">
										<label>Enter Stock</label> 
										<input type="text" name="stock" class="form-control" required>
									</div>
									<div class="mb-3 col">
										<label>Upload Image</label> 
										<input type="file" style="border:1px solid black;" name="file" class="form-control" required>
									</div>
								</div>

								<button class="btn" type="submit">Submit</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>
