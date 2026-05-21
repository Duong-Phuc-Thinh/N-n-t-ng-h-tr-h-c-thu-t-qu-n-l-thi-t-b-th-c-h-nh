package com.example.projeck_cuoi_mon.interceptor;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.model.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        SessionUser loginUser = session == null ? null : (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);

        if (loginUser == null) {
            response.sendRedirect("/login");
            return false;
        }

        String uri = request.getRequestURI();
        if (uri.startsWith("/admin") && loginUser.getRole() != UserRole.ADMIN) {
            response.sendRedirect("/403");
            return false;
        }

        if (uri.startsWith("/lecturer") && loginUser.getRole() != UserRole.LECTURER) {
            response.sendRedirect("/403");
            return false;
        }

        if (uri.startsWith("/student") && loginUser.getRole() != UserRole.STUDENT) {
            response.sendRedirect("/403");
            return false;
        }

        return true;
    }
}
