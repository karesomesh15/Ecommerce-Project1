<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@include file="../base.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
    <section>
        <div class="container-fluid p-5 mt-5">
            <div class="row">
                <div class="col-md-3">
                    <div class="card card-sh">
                        <div class="card-header text-center">
                            <p class="fs-4">Add Category</p>

                            <c:if test="${not empty sessionScope.succMsg}">
                                <p class="text-success fw-bold">${sessionScope.succMsg}</p>
                                <c:set var="sessionScope.succMsg" value="" /> <!-- Clear message -->
                            </c:if>

                            <c:if test="${not empty sessionScope.errorMsg}">
                                <p class="text-danger fw-bold">${sessionScope.errorMsg}</p>
                                <c:set var="sessionScope.errorMsg" value="" /> <!-- Clear message -->
                            </c:if>

                        </div>
                        <div class="card-body">
                            <form action="/admin/saveCategory" method="post" enctype="multipart/form-data">
                                <!-- Required category name -->
                                <div class="mb-3">
                                    <label>Enter Category</label>
                                    <input type="text" name="name" class="form-control" required>
                                </div>

                                <!-- Required status selection -->
                                <div class="mb-3">
                                    <label>Status</label>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" checked value="true" name="isActive" id="flexRadioDefault1" required>
                                        <label class="form-check-label" for="flexRadioDefault1">Active</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="isActive" value="false" id="flexRadioDefault2" required>
                                        <label class="form-check-label" for="flexRadioDefault2">Inactive</label>
                                    </div>
                                </div>

                                <!-- Required image upload -->
                                <div class="mb-3">
                                    <label>Upload Image</label>
                                    <input type="file" name="file" class="form-control" required>
                                </div>

                                <button class="btn btn-primary col-md-12 mt-2" type="submit">Save</button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-md-8">
                    <div class="card card-sh">
                        <div class="card-header text-center fs-4">Category Details</div>
                        <div class="card-body">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th scope="col">Sl No</th>
                                        <th scope="col">Category</th>
                                        <th scope="col">Status</th>
                                        <th scope="col">Image</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="cat" varStatus="status" items="${categorys}">
                                        <tr>
                                            <th scope="row">${status.count}</th>
                                            <td>${cat.name}</td>
                                            <td>${cat.isActive}</td>
                                            <td><img src="/img/category_img/${cat.imageName}" width="50px" height="50px"></td>
                                            <td>
                                                <a href="/admin/loadEditCategory/${cat.id}" class="btn btn-primary btn-sm">
                                                    <i class="fa-solid fa-pen-to-square fa-1x"></i> Edit
                                                </a>
                                                <a href="/admin/deleteCategory/${cat.id}" class="btn btn-danger btn-sm">
                                                    <i class="fa-solid fa-trash fa-1x"></i> Delete
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <div class="row">
                                <div class="col-md-4">Total Category: ${totalElements}</div>
                                <div class="col-md-6">
                                    <nav aria-label="Page navigation example">
                                        <ul class="pagination">
                                            <li class="page-item ${isFirst ? 'disabled' : ''}">
                                                <a class="page-link" href="/admin/category?pageNo=${pageNo - 1}" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>

                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${pageNo + 1 == i ? 'active' : ''}">
                                                    <a class="page-link" href="/admin/category?pageNo=${i - 1}">${i}</a>
                                                </li>
                                            </c:forEach>

                                            <li class="page-item ${isLast ? 'disabled' : ''}">
                                                <a class="page-link" href="/admin/category?pageNo=${pageNo + 1}" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </section>
</body>
</html>
