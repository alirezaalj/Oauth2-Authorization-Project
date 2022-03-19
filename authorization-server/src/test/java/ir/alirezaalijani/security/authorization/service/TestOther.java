package ir.alirezaalijani.security.authorization.service;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class TestOther {
    public static void main(String[] args) {
        PathMatcher matcher = new AntPathMatcher();
        System.out.println(matcher.match("/img/**","/img/default-background.png"));
        System.out.println(matcher.match("/vendor/**","/vendor/java.js"));
        System.out.println(matcher.match("/vendor/**","vendor/java.js"));
    }
}
