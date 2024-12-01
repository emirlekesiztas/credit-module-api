package com.emirhan.ingcasestudy.util;

import com.emirhan.ingcasestudy.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static Long getCurrentUserId() {
        return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static String getCurrentUserRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalStateException("No role assigned to the user"));
    }
}
