package com.ecom.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

public class AdminControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private CommonUtil commonUtil;

    @Mock
    private HttpSession session;

    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadAddProduct() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryService.getAllCategory()).thenReturn(categories);

        String viewName = adminController.loadAddProduct(model);

        assertEquals("admin/add_product", viewName);
        verify(model).addAttribute("categories", categories);
    }

    @Test
    public void testSaveCategory_Success() throws IOException {
        Category category = new Category();
        category.setName("Electronics");

        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "some-image".getBytes());

        when(categoryService.existCategory(category.getName())).thenReturn(false);
        when(categoryService.saveCategory(category)).thenReturn(category);
        when(categoryService.getCategoryById(anyInt())).thenReturn(category);

        String viewName = adminController.saveCategory(category, file, session);

        assertEquals("redirect:/admin/category", viewName);
        verify(session).setAttribute("succMsg", "Saved successfully");
    }

    @Test
    public void testSaveCategory_CategoryExists() throws IOException {
        Category category = new Category();
        category.setName("Electronics");

        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "some-image".getBytes());

        when(categoryService.existCategory(category.getName())).thenReturn(true);

        String viewName = adminController.saveCategory(category, file, session);

        assertEquals("redirect:/admin/category", viewName);
        verify(session).setAttribute("errorMsg", "Category Name already exists");
    }

    @Test
    public void testDeleteCategory() {
        int categoryId = 1;
        when(categoryService.deleteCategory(categoryId)).thenReturn(true);

        String viewName = adminController.deleteCategory(categoryId, session);

        assertEquals("redirect:/admin/category", viewName);
        verify(session).setAttribute("succMsg", "category delete success");
    }

    @Test
    public void testSaveProduct_Success() throws IOException {
        Product product = new Product();
        //product.setName("Laptop");
        product.setPrice(1000.0);

        MockMultipartFile image = new MockMultipartFile("file", "image.jpg", "image/jpeg", "some-image".getBytes());

        when(productService.saveProduct(product)).thenReturn(product);

        String viewName = adminController.saveProduct(product, image, session);

        assertEquals("redirect:/admin/loadAddProduct", viewName);
        verify(session).setAttribute("succMsg", "Product Saved Success");
    }

	/*
	 * @Test public void testUpdateOrderStatus() throws Exception { int orderId = 1;
	 * int statusId = 1; // Let's assume this is a valid status ProductOrder order =
	 * new ProductOrder();
	 * 
	 * when(orderService.updateOrderStatus(orderId,
	 * "PROCESSING")).thenReturn(order);
	 * 
	 * // Adjusted based on the method signature
	 * //when(commonUtil.sendMailForProductOrder(order,
	 * "PROCESSING")).thenReturn("Email Sent");
	 * 
	 * String viewName = adminController.updateOrderStatus(orderId, statusId,
	 * session);
	 * 
	 * assertEquals("redirect:/admin/orders", viewName);
	 * verify(session).setAttribute("succMsg", "Status Updated"); }
	 */

	/*
	 * @Test public void testChangePassword_Success() { UserDtls user = new
	 * UserDtls(); user.setPassword("encodedOldPassword");
	 * 
	 * when(commonUtil.getLoggedInUserDetails(principal)).thenReturn(user);
	 * //when(passwordEncoder.matches("currentPassword",
	 * user.getPassword())).thenReturn(true);
	 * //when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword"
	 * ); when(userService.updateUser(user)).thenReturn(user);
	 * 
	 * String viewName = adminController.changePassword("newPassword",
	 * "currentPassword", principal, session);
	 * 
	 * assertEquals("redirect:/admin/profile", viewName);
	 * verify(session).setAttribute("succMsg", "Password Updated sucessfully"); }
	 */

	/*
	 * @Test public void testChangePassword_InvalidCurrentPassword() { UserDtls user
	 * = new UserDtls(); user.setPassword("encodedOldPassword");
	 * 
	 * when(commonUtil.getLoggedInUserDetails(principal)).thenReturn(user);
	 * //when(passwordEncoder.matches("currentPassword",
	 * user.getPassword())).thenReturn(false);
	 * 
	 * String viewName = adminController.changePassword("newPassword",
	 * "currentPassword", principal, session);
	 * 
	 * assertEquals("redirect:/admin/profile", viewName);
	 * verify(session).setAttribute("errorMsg", "Current Password incorrect"); }
	 */
}
