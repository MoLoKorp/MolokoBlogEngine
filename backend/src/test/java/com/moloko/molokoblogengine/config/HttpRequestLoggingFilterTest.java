package com.moloko.molokoblogengine.config;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class HttpRequestLoggingFilterTest {

  @Mock ServerWebExchange serverWebExchange;

  @Mock WebFilterChain webFilterChain;

  @Mock ServerHttpRequest serverHttpRequest;

  @Mock InetSocketAddress inetSocketAddress;

  @Mock RequestPath requestPath;

  @Test
  void testFilter(CapturedOutput log) {
    when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
    when(serverHttpRequest.getPath()).thenReturn(requestPath);
    when(requestPath.toString()).thenReturn("/testPath");
    when(serverHttpRequest.getRemoteAddress()).thenReturn(inetSocketAddress);
    when(inetSocketAddress.toString()).thenReturn("1.1.1.1");
    when(serverHttpRequest.getHeaders())
        .thenReturn(
            new HttpHeaders(
                new LinkedMultiValueMap<>(
                    Map.of(HttpHeaders.USER_AGENT, List.of("testUserAgent")))));
    HttpRequestLoggingFilter httpRequestLoggingFilter = new HttpRequestLoggingFilter();

    httpRequestLoggingFilter.filter(serverWebExchange, webFilterChain);

    assertTrue(log.getOut().contains("Incoming request to /testPath from 1.1.1.1 [testUserAgent]"));
    verify(webFilterChain).filter(serverWebExchange);
  }
}
