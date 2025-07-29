package project.predix.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import project.predix.auth.JwtProvider;

import java.io.IOException;
import java.util.Arrays;


@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        if(token != null){
            try{
                Claims claims = jwtProvider.parse(token);
                UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch(ExpiredJwtException e){
                response.sendError(401, "Access token expired");
                return;
            }catch(JwtException | IllegalArgumentException e){
                response.sendError(401, "Invalid token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest req){
        String bearer = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer")){
            return bearer.substring(7);
        }

        if(req.getCookies() != null){
            return Arrays.stream(req.getCookies())
                    .filter(cookie -> "ACCESS_TOKEN".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }
}
