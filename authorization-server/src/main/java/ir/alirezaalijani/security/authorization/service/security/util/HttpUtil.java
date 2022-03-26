package ir.alirezaalijani.security.authorization.service.security.util;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
    private HttpUtil() {
    }

    public static String getForwardedHost(HttpServletRequest request){
        var host=request.getHeader("x-forwarded-host");
        var proto= request.getHeader("x-forwarded-proto");
        if (host!=null){
            if (proto==null) proto="http";
            host=proto.concat("://"+host);
            return host;
        }
        return "";
    }

    public static void printRequestInfo(HttpServletRequest request) {
        System.out.println("request uri:" + request.getRequestURI());
        System.out.println("Client ip:" + getClientIP(request));
        var headerNames = request.getHeaderNames();
        if (headerNames != null) {
            System.out.println("Headers:");
            while (headerNames.hasMoreElements()) {
                var header = headerNames.nextElement();
                var value = request.getHeader(header);
                System.out.println("\t" + header + ":" + value);
            }
        }
//        var prams = request.getParameterNames();
//        if (prams != null) {
//            System.out.println("Params");
//            while (prams.hasMoreElements()) {
//                var pram = prams.nextElement();
//                var value = request.getParameter(pram);
//                System.out.println("\t" + pram + ":" + value);
//            }
//        }
    }

    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
