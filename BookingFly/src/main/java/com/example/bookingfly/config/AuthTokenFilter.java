package com.example.bookingfly.config;

import com.example.bookingfly.contant.ApplicationConstant;
import com.example.bookingfly.service.UserDetailsServiceImpl;
import com.example.bookingfly.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * <div>
 *      Mỗi request của người dùng gọi lên sẽ đi qua filter này.
 *      Hàm doFilterInternal sẽ bỏ qua một số link không cần check quyền được cấu hình từ file SecurityTokenConfig.</div>
 * <br>
 * <div>
 *     Những link còn lại sẽ bóc tách token lấy trong cookie người dùng, verify lại jwt token này để đảm bảo
 *     đây là token do hệ thống mình sinh ra bằng hàm verify trong lớp MetaJwtUtil sử dụng secret key (Hiểu đơn giản thì
 *     một token không được hệ thống sinh ra hoặc đã bị chỉnh sửa sẽ không được verify.)
 * </div>
 * <br>
 * <div>
 *     Token sau khi verify sẽ dùng để lấy thông tin user id và role, phần này để phân quyền tiếp theo.
 *     Thông tin này được lưu vào context của spring và có thể lấy ra ở tất cả các controller khác cùng request.
 * </div>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    final JwtUtils jwtUtils;
    final UserDetailsServiceImpl userDetailsIpmpl;

    public static final Logger logger = LoggerFactory.getLogger(com.example.bookingfly.config.AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsIpmpl.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception e){
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Lấy thông tin token trong tất cả tình huống từ client.
     * Không welcome cách này nhưng tạm thời chưa chuyển vì không kiểm soát được số chức năng liên quan.
     */
    private String getToken(HttpServletRequest request) {
        if (request.getParameter("app.base.cookie_name") != null) {
            return request.getParameter("app.base.cookie_name");
        }
        if (request.getHeader("app.base.cookie_name") != null) {
            return request.getHeader("app.base.cookie_name");
        }
        // Dùng trong api call lên.
        if (request.getHeader(ApplicationConstant.AUTHORIZATION_NAME) != null) {
            // Lấy thông tin token trong header Authentication: Bearer _token.
            return request.getHeader(ApplicationConstant.AUTHORIZATION_NAME)
                    .replaceAll(ApplicationConstant.AUTHORIZATION_TOKEN_TYPE, ApplicationConstant.EMPTY_STRING)
                    .trim();
        }
        if (WebUtils.getCookie(request, "app.base.cookie_name") != null) {
            return Objects.requireNonNull(WebUtils.getCookie(request, "app.base.cookie_name")).getValue();
        }
        return null;
    }

    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    public String currentToken(HttpServletRequest request) {
        return getToken(request);
    }
}