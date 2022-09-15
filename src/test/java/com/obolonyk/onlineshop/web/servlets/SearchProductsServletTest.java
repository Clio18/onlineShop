package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class SearchProductsServletTest {

    @Test
    @DisplayName("testDoPostAndCheckServiceWork")
    void testDoPostAndCheckServiceWork() throws IOException, ServletException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        when(mockReq.getParameter("search")).thenReturn("An");

        ProductService productService = mock(ProductService.class);
        SearchProductsServlet searchProductsServlet = new SearchProductsServlet();
        searchProductsServlet.setProductService(productService);

        Product product1 = Product.builder()
                .id(1)
                .name("A")
                .price(10.0)
                .description("An")
                .creationDate(LocalDateTime.now())
                .build();
        List<Product> products = List.of(product1);

        when(productService.getBySearch("An")).thenReturn(products);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        searchProductsServlet.doPost(mockReq, mockResp);
        assertNotNull(stringWriter);
        assertTrue(stringWriter.toString().contains("An"));
        verify(productService, times(1)).getBySearch(isA(String.class));
    }


}