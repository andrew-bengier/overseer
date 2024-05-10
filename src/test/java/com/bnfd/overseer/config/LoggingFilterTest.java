package com.bnfd.overseer.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.io.*;

@ExtendWith(MockitoExtension.class)
public class LoggingFilterTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private LoggingFilter loggingFilter;

    @Test
    public void testDoFilter() throws  ServletException, IOException {
        Mockito.doNothing().when(filterChain).doFilter(Mockito.eq(httpServletRequest), Mockito.eq(httpServletResponse));

        loggingFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        Mockito.verify(filterChain, Mockito.times(1)).doFilter(Mockito.eq(httpServletRequest), Mockito.eq(httpServletResponse));
    }
}
