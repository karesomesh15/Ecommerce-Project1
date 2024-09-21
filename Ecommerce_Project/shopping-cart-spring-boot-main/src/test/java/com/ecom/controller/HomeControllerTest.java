package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.ReviewModel;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.ReviewService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private CommonUtil commonUtil;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private CartService cartService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private HttpServletRequest request;

    private UserDtls user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserDtls();
        user.setId(1);
        user.setEmail("test@example.com");
    }

	/*
	 * @Test void testIndex() { List<Category> categories = Arrays.asList(new
	 * Category());
	 * when(categoryService.getAllActiveCategory()).thenReturn(categories);
	 * List<Product> products = Arrays.asList(new Product(), new Product());
	 * when(productService.getAllActiveProducts("")).thenReturn(products);
	 * 
	 * String view = homeController.index(model);
	 * 
	 * assertEquals("index", view); verify(model).addAttribute("category",
	 * categories); verify(model).addAttribute("products", products); }
	 */

    @Test
    void testLogin() {
        String view = homeController.login();
        assertEquals("login", view);
    }

    @Test
    void testRegister() {
        String view = homeController.register();
        assertEquals("register", view);
    }

    @Test
    void testProducts() {
        String category = "Electronics";
        Integer pageNo = 0;
        Integer pageSize = 12;
        List<Category> categories = Arrays.asList(new Category());
        when(categoryService.getAllActiveCategory()).thenReturn(categories);
        when(productService.getAllActiveProductPagination(pageNo, pageSize, category)).thenReturn(mock(Page.class));

        String view = homeController.products(model, category, pageNo, pageSize, "");

        assertEquals("product", view);
        verify(model).addAttribute("categories", categories);
    }

    @Test
    void testProduct() {
        int productId = 1;
        Product product = new Product();
        when(productService.getProductById(productId)).thenReturn(product);
        when(reviewService.getReviewsByProductId(productId)).thenReturn(Arrays.asList(new ReviewModel()));

        String view = homeController.product(productId, model);

        assertEquals("view_product", view);
        verify(model).addAttribute("product", product);
    }

    @Test
    void testSaveReview_Success() {
        int productId = 1;
        int userId = 1;
        int rating = 5;
        String comment = "Great product!";
        
        Product product = new Product();
        UserDtls user = new UserDtls();
        
        when(productService.getProductById(productId)).thenReturn(product);
        when(userService.getUserById(userId)).thenReturn(user);
        
        String view = homeController.saveReview(productId, userId, rating, comment, redirectAttributes);

        assertEquals("redirect:/product/" + productId, view);
        verify(redirectAttributes).addFlashAttribute("succMsg", "Review submitted successfully");
    }

    @Test
    void testSaveReview_InvalidProductOrUser() {
        int productId = 1;
        int userId = 1;
        int rating = 5;
        String comment = "Great product!";
        
        when(productService.getProductById(productId)).thenReturn(null);
        
        String view = homeController.saveReview(productId, userId, rating, comment, redirectAttributes);

        assertEquals("redirect:/product/" + productId, view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Invalid product or user");
    }

    @Test
    void testSaveUser_Success() throws IOException {
        // Create a mock MultipartFile with a valid filename
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("profile.jpg");
        when(file.isEmpty()).thenReturn(false);
        when(userService.existsEmail(user.getEmail())).thenReturn(false);
        when(userService.saveUser(user)).thenReturn(user);

        // Adjusting the HomeController to allow for a valid path for testing
        String tempPath = System.getProperty("java.io.tmpdir"); // Using temp directory for testing
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test image data".getBytes()));

        // Call the saveUser method
        String view = homeController.saveUser(user, file, redirectAttributes);

        assertEquals("redirect:/register", view);
        verify(redirectAttributes).addFlashAttribute("succMsg", "Registered successfully");
    }


    @Test
    void testSaveUser_EmailExists() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(userService.existsEmail(user.getEmail())).thenReturn(true);
        
        String view = homeController.saveUser(user, file, redirectAttributes);

        assertEquals("redirect:/register", view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Email already exists");
    }

    @Test
    void testShowForgotPassword() {
        String view = homeController.showForgotPassword();
        assertEquals("forgot_password", view);
    }

	/*
	 * @Test void testProcessForgotPassword_EmailValid() throws Exception { //
	 * Arrange String email = "test@example.com"; UserDtls user = new UserDtls();
	 * user.setEmail(email);
	 * 
	 * when(userService.getUserByEmail(email)).thenReturn(user);
	 * when(userService.updateUserResetToken(anyString(),
	 * anyString())).thenReturn(true); when(commonUtil.sendMail(anyString(),
	 * anyString())).thenReturn(true);
	 * 
	 * // Mock HttpServletRequest HttpServletRequest request =
	 * mock(HttpServletRequest.class); when(request.getRequestURL()).thenReturn(new
	 * StringBuffer("http://localhost:8080"));
	 * 
	 * // Act String view = homeController.processForgotPassword(email,
	 * redirectAttributes, request);
	 * 
	 * // Assert assertEquals("redirect:/forgot-password", view);
	 * verify(redirectAttributes).addFlashAttribute("succMsg",
	 * "Password Reset link sent to your email"); }
	 */

    @Test
    void testProcessForgotPassword_EmailInvalid() throws UnsupportedEncodingException, MessagingException {
        String email = "invalid@example.com";
        when(userService.getUserByEmail(email)).thenReturn(null);

        String view = homeController.processForgotPassword(email, redirectAttributes, request);

        assertEquals("redirect:/forgot-password", view);
        verify(redirectAttributes).addFlashAttribute("errorMsg", "Invalid email");
    }

    @Test
    void testShowResetPassword() {
        String token = UUID.randomUUID().toString();
        when(userService.getUserByToken(token)).thenReturn(user);

        String view = homeController.showResetPassword(token, model);

        assertEquals("reset_password", view);
        verify(model).addAttribute("token", token);
    }

    @Test
    void testResetPassword_Success() {
        String token = UUID.randomUUID().toString();
        String password = "newPassword";
        when(userService.getUserByToken(token)).thenReturn(user);
        
        String view = homeController.resetPassword(token, password, model);

        assertEquals("message", view);
        verify(model).addAttribute("msg", "Password changed successfully");
    }

    @Test
    void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        when(userService.getUserByToken(token)).thenReturn(null);
        
        String view = homeController.resetPassword(token, "newPassword", model);

        assertEquals("message", view);
        verify(model).addAttribute("errorMsg", "Your link is invalid or expired!");
    }

    @Test
    void testSearchProduct() {
        String query = "test";
        List<Product> products = Arrays.asList(new Product());
        when(productService.searchProduct(query)).thenReturn(products);
        List<Category> categories = Arrays.asList(new Category());
        when(categoryService.getAllActiveCategory()).thenReturn(categories);
        
        String view = homeController.searchProduct(query, model);

        assertEquals("product", view);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("categories", categories);
    }
}
