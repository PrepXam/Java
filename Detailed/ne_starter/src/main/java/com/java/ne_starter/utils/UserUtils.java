package com.java.ne_starter.utils;



import com.java.ne_starter.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static UserPrincipal getLoggedInUser() {
        // Retrieve the currently authenticated user from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }
}
