package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductServletTest {

    @Test
    @DisplayName("testDoGetAndCheckResultIsNotEmpty")
    void testDoGetAndCheckResultIsNotEmpty() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        when(mockReq.getParameter("id")).thenReturn("1");

        ProductService productService = mock(ProductService.class);
        PageGenerator pageGenerator = PageGenerator.instance();
        UpdateProductServlet updateProductServlet = new UpdateProductServlet();
        updateProductServlet.setProductService(productService);
        updateProductServlet.setPageGenerator(pageGenerator);

        Product product = Product.builder()
                .id(1)
                .name("A")
                .price(10.0)
                .description("AA")
                .creationDate(LocalDateTime.now())
                .build();

        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        updateProductServlet.doGet(mockReq, mockResp);
        assertNotNull(stringWriter);
        verify(productService, times(1)).getProductById(isA(Integer.class));
    }

    @Test
    @DisplayName("test DoPost And Verify Service Work")
    void testDoPostAndVerifyServiceWork() {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        when(mockReq.getParameter("name")).thenReturn("A");
        when(mockReq.getParameter("description")).thenReturn("AA");
        when(mockReq.getParameter("price")).thenReturn("10.0");
        when(mockReq.getParameter("id")).thenReturn("1");

        ProductService productService = mock(ProductService.class);
        UpdateProductServlet updateProductServlet = new UpdateProductServlet();
        updateProductServlet.setProductService(productService);

        doNothing().when(productService).update(isA(Product.class));

        updateProductServlet.doPost(mockReq, mockResp);
        verify(productService, times(1)).update(isA(Product.class));
    }

}