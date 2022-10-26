package me.iqpizza.config.security.filter;

import lombok.RequiredArgsConstructor;
import me.iqpizza.config.jwt.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = jwtProvider.getTokenFromHeader(request.getHeader("Authorization"));
            if (bearerToken != null) {
                UserDetails userDetails = jwtProvider.parseAuthenticateUser(bearerToken);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterException(e, response);
        }
    }

    private void filterException(Exception exception, HttpServletResponse response)
            throws IOException {
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(exception.getMessage());
    }
}
