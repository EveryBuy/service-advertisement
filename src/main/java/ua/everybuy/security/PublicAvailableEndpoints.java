package ua.everybuy.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

public class PublicAvailableEndpoints {
    private static final Set<RequestMatcher> PUBLIC_ENDPOINTS = Set.of(
            new AntPathRequestMatcher("/ad/category/**"),
            new AntPathRequestMatcher("/ad/subcategory/**"),
            new AntPathRequestMatcher("/ad/city/**"),
            new AntPathRequestMatcher("/ad/region/**"),
            new AntPathRequestMatcher("/ad/keep-alive"),
            new AntPathRequestMatcher("/ad/*/active"),
            new AntPathRequestMatcher("/ad/*/info"),
            new AntPathRequestMatcher("/ad/filter"),
            new AntPathRequestMatcher("/ad/user/*/advertisements/**")
    );

    public static Set<RequestMatcher> getPublicEndpoints() {
        return PUBLIC_ENDPOINTS;
    }
}
