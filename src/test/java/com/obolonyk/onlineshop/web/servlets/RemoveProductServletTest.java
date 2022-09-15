package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

class RemoveProductServletTest {

    @Test
    @DisplayName("test DoPost And Verify Service Work")
    void testDoPostAndVerifyServiceWork() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        when(mockReq.getParameter("id")).thenReturn("1");

        ProductService productService = mock(ProductService.class);
        RemoveProductServlet removeProductServlet = new RemoveProductServlet();
        removeProductServlet.setProductService(productService);
        doNothing().when(productService).remove(isA(Integer.class));

        removeProductServlet.doPost(mockReq, mockResp);
        verify(productService, times(1)).remove(isA(Integer.class));
    }


}