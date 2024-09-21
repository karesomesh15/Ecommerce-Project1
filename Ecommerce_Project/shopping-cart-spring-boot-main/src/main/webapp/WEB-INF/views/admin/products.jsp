<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../base.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>All Products</title>
    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css' />">
    <script src="<c:url value='/js/bootstrap.bundle.min.js' />"></script>
</head>
<body>
    <section>
        <div class="container-fluid mt-4 p-3" style="margin-top:3rem;">
            <div class="row" style="margin-top:3rem; ">
                <p class="text-center fs-3 mt-2" style="font-weight:699;color:blue">All Products</p>
                <hr>
                <a href="${pageContext.request.contextPath}/admin/" class="text-decoration-none">
                    <i class="fa-solid fa-arrow-left"></i> Back
                </a>

                <% if (session.getAttribute("succMsg") != null) { %>
                    <p class="text-success fw-bold"><%= session.getAttribute("succMsg") %></p>
                    <% session.removeAttribute("succMsg"); %>
                <% } %>

                <% if (session.getAttribute("errorMsg") != null) { %>
                    <p class="text-danger fw-bold text-center"><%= session.getAttribute("errorMsg") %></p>
                    <% session.removeAttribute("errorMsg"); %>
                <% } %>

                <div class="col-md-4 p-3">
                    <form action="${pageContext.request.contextPath}/admin/products" method="get">
                        <div class="row">
                            <div class="col">
                                <input type="text" class="form-control" name="ch" placeholder="Search product" style="border:1px solid black;">
                            </div>
                            <div class="col">
                                <button class="btn col" style="background-color:blue; color:white;">Search</button>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="p-3">
                    <table class="table">
                        <thead class="table-light">
                            <tr>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Sr No.</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Image</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Title</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Category</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Price</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Discount</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Discount Price</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Status</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Stock</th>
                                <th scope="col" style="background-color: black; color:white;text-align:center; font-size:17px; ">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}" varStatus="c">
                                <tr>
                                    <th scope="row"  style="text-align:center;"><c:out value="${c.index + 1}"/></th>
                                    <td style="text-align:center;"><img src="${pageContext.request.contextPath}/img/product_img/${p.image}" width="70px" height="70px"></td>
                                    <td style="text-align:center;"><c:out value="${p.title}"/></td>
                                    <td style="text-align:center;"><c:out value="${p.category}"/></td>
                                    <td style="text-align:center;"><c:out value="${p.price}"/></td>
                                    <td style="text-align:center;"><c:out value="${p.discount}"/></td>
                                    <td style="text-align:center;"><c:out value="${p.discountPrice}"/></td>
                                    <td style="text-align:center;"><c:out value="${p.isActive}"/></td>
                                    <td style="text-align:center;"><c:out value="${p.stock}"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/editProduct/${p.id}" class="btn" style="background-color:blue;color:white;width:80px;">
                                            <i class="fa-solid fa-pen-to-square"></i> Edit
                                        </a>
                                        <%-- <a href="${pageContext.request.contextPath}/admin/deleteProduct/${p.id}" class="btn" style="background-color:red;color:white;width:80px;">
                                            <i class="fa-solid fa-trash"></i> Delete
                                        </a> --%>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="row">
                        <div class="col-md-4" style="font-weight:bold; text-align:center;">Total Product : <c:out value="${totalElements}"/></div>
                        <div class="col-md-6">
                            <nav aria-label="Page navigation example">
                                <ul class="pagination">
                                    <li class="page-item <c:if test="${isFirst}">disabled</c:if>">
                                        <a class="page-link" href="${pageContext.request.contextPath}/admin/products?pageNo=${pageNo - 1}" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>

                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item <c:if test="${pageNo + 1 == i}">active</c:if>">
                                            <a class="page-link" href="${pageContext.request.contextPath}/admin/products?pageNo=${i - 1}">
                                                <c:out value="${i}"/>
                                            </a>
                                        </li>
                                    </c:forEach>

                                    <li class="page-item <c:if test="${isLast}">disabled</c:if>">
                                        <a class="page-link" href="${pageContext.request.contextPath}/admin/products?pageNo=${pageNo + 1}" aria-label="Next">
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
    </section>
</body>  
</html>