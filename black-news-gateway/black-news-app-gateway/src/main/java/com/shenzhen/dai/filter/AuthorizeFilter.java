package com.shenzhen.dai.filter;

import com.shuwei.dai.AppJwtUtil;
import com.shuwei.dai.ObjectService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @description: 网关登录过滤器
 * @author: daiyifan
 * @create: 2024-10-09 11:00
 */
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered, ObjectService {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 2.判断是否是登录
        if (request.getURI().getPath().contains("/login")) {
            return chain.filter(exchange);
        }

        // 3.获取token
        String token = request.getHeaders().getFirst("token");

        // 4.判断token是否存在
        if (isBlank(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 5.判断token是否有效
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 6.放行
        return chain.filter(exchange);
    }

    /**
     * 值越小，优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

}
