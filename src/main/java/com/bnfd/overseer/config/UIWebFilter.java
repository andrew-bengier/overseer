package com.bnfd.overseer.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UIWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String path = httpServletRequest.getRequestURI();

//        if (!path.startsWith("/api")) {
//            httpServletRequest.getRequestDispatcher("/index.html").forward(httpServletRequest, httpServletResponse);
//            return;
//        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
