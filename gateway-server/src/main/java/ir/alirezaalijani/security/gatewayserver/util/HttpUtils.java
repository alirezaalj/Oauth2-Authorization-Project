package ir.alirezaalijani.security.gatewayserver.util;

import org.springframework.web.server.ServerWebExchange;

public class HttpUtils {

    public static String getRemoteIp(ServerWebExchange exchange){
        var remoteArr =exchange.getRequest().getRemoteAddress();
        if (remoteArr!=null){
            return remoteArr.getAddress().getHostAddress();
        }else {
            return null;
        }
    }

    public static String getHost(ServerWebExchange exchange){
        return exchange.getRequest().getURI().getHost();
    }

    private HttpUtils() {}
}
