package com.moloko.molokoblogengine.config;

import java.util.List;
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
  private final Logger log = LoggerFactory.getLogger(HttpRequestLoggingFilter.class);
  private static final String ELB_USER_AGENT = "ELB-HealthChecker/2.0";
  private static final String X_FORWARDED_FOR = "X-Forwarded-For";
  private static final List<String> EXCLUDES = List.of(".js", ".css", ".ico");

  @Override
  public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

    ServerHttpRequest request = serverWebExchange.getRequest();
    HttpHeaders headers = request.getHeaders();
    String path = request.getPath().toString();
    String realIp = "IP is not detected";
    List<String> userAgents = List.of();

    if (headers.containsKey(X_FORWARDED_FOR) && !headers.get(X_FORWARDED_FOR).isEmpty()) {
      realIp = headers.get(X_FORWARDED_FOR).get(0);
    }
    if (headers.containsKey(HttpHeaders.USER_AGENT)) {
      userAgents = headers.get(HttpHeaders.USER_AGENT);
    }
    if (!userAgents.contains(ELB_USER_AGENT) && !EXCLUDES.stream().anyMatch(path::contains)) {
      log.info(
          "Incoming {} request to {} from {} user-agent: {}",
          request.getMethodValue(),
          path,
          realIp,
          userAgents);
    }
    return webFilterChain.filter(serverWebExchange);
  }
}
