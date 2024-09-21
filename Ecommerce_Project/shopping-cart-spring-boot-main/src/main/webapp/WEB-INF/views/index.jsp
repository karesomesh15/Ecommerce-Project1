<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="base.jsp" %>
<!DOCTYPE html>
<html>
<head> 
    <meta charset="ISO-8859-1">
    <title>Insert title here</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
    	.img1{
    		width:100%;
    		height:380px;
    	}
    	.img2{
    		width:200px;
    		height:150px;
    		border-radius:100%;
    	}
    	.container {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
}

.col-md-3 {
    flex: 0 0 25%;
    padding: 10px;
}

.card {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    
    padding: 20px;
    margin-bottom: 30px;
    background-color: #f8f9fa;
    border-radius: 10px;
    cursor:pointer;
    transition:all ease-in 0.4s;
  	background-color: rgba(0, 0, 255, 0.719);

}

.card-body {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.card-body img {
    width: 95%;
    height: 150px;
    object-fit: cover;
}
.card:hover{
	transform:scale(1.05);
	}

.card-body p {
	font-size:20px;
	font-weight:599;
    margin-top: 50px;
}

.card-body a {
    text-decoration: none;
    color: #000;
}
    	
    	
    	
    </style>
</head>
<body>
    <section>
    <!-- Start Category Module -->
        <div class="container">
            <div class="row">
                <p class="text-center fs-4" style="margin-top:5rem; margin-bottom:1rem;font-weight:699;"></p>
                
                <c:forEach var="category" items="${category}">
                    <div class="col-md-2" style="height:150px; gap:3rem !important;margin-top:0rem;">
                        <div class="card  shadow-sm p-3 mb-5 bg-body-tertiary" style="width:180px; height:120px;gap:3rem; margin-left:10px; border:none !important;">
                            <div class="card-body text-center" style="">
                                <img src="${pageContext.request.contextPath}/img/category_img/${category.imageName}" style="width:100%; height:50%;margin-top:3.5rem;"><br>
                                <a href="${pageContext.request.contextPath}/products?category=${category.name}" style="margin-top:0px;" class="text-decoration-none">${category.name}</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <!-- End Category Module -->
        <!-- Start Slider  -->
        <div id="carouselExample" class="carousel slide">
            <div class="carousel-inner">
                <div class="carousel-item active" style="margin-top:2rem;">
                    <img class="img1" src="https://static.vecteezy.com/system/resources/previews/004/299/835/non_2x/online-shopping-on-phone-buy-sell-business-digital-web-banner-application-money-advertising-payment-ecommerce-illustration-search-free-vector.jpg" alt="..." height="350px">
                </div>
                <div class="carousel-item">
                    <img class="img1" src="https://mindstacktechnologies.com/wordpress/wp-content/uploads/2018/01/ecommerce-banner.jpg" class="d-block w-100" alt="..." height="350px">
                </div>
                <div class="carousel-item">
                    <img class="img1" src="https://e0.pxfuel.com/wallpapers/606/84/desktop-wallpaper-ecommerce-website-design-company-noida-e-commerce-banner-design-e-commerce.jpg" class="d-block w-100" alt="..." height="350px">
                </div>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
        <!-- End Slider  -->

        

        <!-- Start Latest Product Module -->
        <div class="container-fluid bg-light p-3">
            <div class="row">
                <p class="text-center fs-4" style="margin-top:1rem;font-weight:699;">Latest Product</p>

                <c:forEach var="product" items="${products}">
                    <div class="col-md-3" style="">
                        <div class="card shadow-sm p-3 mb-5 bg-body-tertiary rounded" style="box-shadow:5px 5px 5px black !important; width:80%;margin-left:1rem;">
                            <div class="card-body text-center" style="height:250px;" >
                                <img alt="" src="${pageContext.request.contextPath}/img/product_img/${product.image}" class="" style=" width:95%; height=:100px;">
                                <p class="text-center">
                                    <a href="${pageContext.request.contextPath}/product/${product.id}" class="text-decoration-none">${product.title}</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <!-- End Latest Product Module -->
    </section>
    <script src="${pageContext.request.contextPath}/js/script.js"></script> 
</body>
</html>