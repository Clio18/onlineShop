package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddProductServletTest {

    @Test
    @DisplayName("testDoGetAndCheckResultIsNotEmpty")
    void testDoGetAndCheckResultIsNotEmpty() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        AddProductServlet addProductServlet = new AddProductServlet();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        addProductServlet.doGet(mockReq, mockResp);
        assertNotNull(stringWriter);
    }

    @Test
    @DisplayName("test DoPost And Verify Service Work")
    void testDoPostAndVerifyServiceWork() {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        when(mockReq.getParameter("name")).thenReturn("A");
        when(mockReq.getParameter("description")).thenReturn("AA");
        when(mockReq.getParameter("price")).thenReturn("1.0");

        ProductService productService = mock(ProductService.class);
        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(productService);

        doNothing().when(productService).save(isA(Product.class));

        addProductServlet.doPost(mockReq, mockResp);
        verify(productService, times(1)).save(isA(Product.class));
    }

}