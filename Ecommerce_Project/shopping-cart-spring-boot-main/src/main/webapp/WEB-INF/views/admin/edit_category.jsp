<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.ecom.service.CommnServiceImpl" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>Edit Category</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
    	.col-md-3{
    		background-color:white; 
    		width:50%;
    		box-shadow:8px 8px 8px black;
    		height:420px;
    		margin-top: 5rem;
    		margin-left: 20rem;
    	}
    	.fs-4{
    		text-align:center;
    		font-size:40px;
    		padding-top:10px;
    		font-weight:699;
    	}
    	.mb-3 label{
    		font-weight:699;
    		margin-left:1rem;
    		font-size:20px;
    	}
    	.mb-3 .form-control{
    		width:70%;
    		height:30px;
    		border:1px solid black;
    		border-radius:8px;
    		margin-left:10px;
    	}
    	.btn{
    		background-color:blue;
    		color:white;
    		border:none;
    		outline:none;
    		width:200px;
    		height:40px;
    		font-size:20px;
    		margin-left:13rem;
    		cursor:pointer;
    	}
    </style>
</head>
<body>
    <section>
        <div class="container-fluid p-5 mt-5">
            <div class="row">
                <div class="col-md-3 offset-md-5">
                    <div class="card card-sh">
                        <div class="card-header text-center">
                            <p class="fs-4" style="color:blue;">Edit Category</p>
                            
                            <!-- Display success message -->
                            <c:if test="${not empty sessionScope.succMsg}">
                                <p class="text-success fw-bold">${sessionScope.succMsg}</p>
                                <c:set var="dummy" value="${sessionScope.remove('succMsg')}" />
                            </c:if>

                            <!-- Display error message -->
                            <c:if test="${not empty sessionScope.errorMsg}">
                                <p class="text-danger fw-bold">${sessionScope.errorMsg}</p>
                                <c:set var="dummy" value="${sessionScope.remove('errorMsg')}" />
                            </c:if>
                        </div>
                        <div class="card-body">
                            <form action="${pageContext.request.contextPath}/admin/updateCategory" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="id" value="${category.id}">
                                <div class="mb-3">
                                    <label>Enter Category :</label>
                                    <input type="text" name="name" value="${category.name}" class="form-control">
                                </div>

                                <div class="mb-3">
                                    <div class="form-check">
                                     <label>Status</label>
                                        <input class="form-check-input" style="margin-left:4rem;margin-top:1rem;margin-bottom:1rem;" type="radio" value="true" name="isActive" id="flexRadioDefault1" <c:if test="${category.isActive}">checked</c:if>>
                                        <label class="form-check-label" for="flexRadioDefault1">Active</label>
                                    
                                        <input class="form-check-input" type="radio" value="false" name="isActive" id="flexRadioDefault2" <c:if test="${!category.isActive}">checked</c:if>>
                                        <label class="form-check-label" for="flexRadioDefault2">Inactive</label>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label>Upload Image</label>
                                    <input type="file" name="file" class="form-control" style="padding-top:10px;padding-left:1rem;">
                                    <img style="margin-left:10rem; margin-top:20px;border:1px solid black;" src="${pageContext.request.contextPath}/img/category_img/${category.imageName}" width="100px" height="100px">
                                    <br><br><button class="btn">Update</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</body>
</html>
