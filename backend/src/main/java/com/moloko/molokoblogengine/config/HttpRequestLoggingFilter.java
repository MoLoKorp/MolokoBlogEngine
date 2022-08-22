package com.moloko.molokoblogengine.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/** Simple HttpFilter which logs path, ip address and user-agent of the incoming request. */
@Component
public class HttpRequestLoggingFilter implements WebFilter {
  private Logger log = LoggerFactory.getLogger(HttpRequestLoggingFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

    ServerHttpRequest request = serverWebExchange.getRequest();
    HttpHeaders headers = request.getHeaders();
    String from = request.getRemoteAddress().toString();
    if (headers.containsKey(HttpHeaders.USER_AGENT)) {
      from += " " + headers.get(HttpHeaders.USER_AGENT);
    }
    log.info("Incoming request to {} from {}", request.getPath(), from);
    return webFilterChain.filter(serverWebExchange);
  }
}
