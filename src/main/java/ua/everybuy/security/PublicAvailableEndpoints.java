package ua.everybuy.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

public class PublicAvailableEndpoints {
    private static final Set<RequestMatcher> PUBLIC_ENDPOINTS = Set.of(
            new AntPathRequestMatcher("/product/category/**"),
            new AntPathRequestMatcher("/product/subcategory/**"),
            new AntPathRequestMatcher("/product/city/**"),
            new AntPathRequestMatcher("/product/region/**"),
            new AntPathRequestMatcher("/product/keep-alive"),
            new AntPathRequestMatcher("/product/*/active"),
            new AntPathRequestMatcher("/product/*/info"),
            new AntPathRequestMatcher("/product/filter"),
            new AntPathRequestMatcher("/product/user/*/ads/**"),
            new AntPathRequestMatcher("/product/search/**"),
            new AntPathRequestMatcher("/ad/keep-alive"),
            new AntPathRequestMatcher("/actuator/health"));

    public static Set<RequestMatcher> getPublicEndpoints() {
        return PUBLIC_ENDPOINTS;
    }
}
