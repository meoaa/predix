package project.predix.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RedirectLoginEntryPoint implements AuthenticationEntryPoint {

    private final MediaTypeRequestMatcher htmlMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
    private static final String LOGIN_PATH = "/login";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        if(htmlMatcher.matches(request)){
            String redirectTarget = URLEncoder.encode(request.getRequestURI(), StandardCharsets.UTF_8);
            String location = LOGIN_PATH + "?redirect=" + redirectTarget;

            log.debug("인증받지 않은 HTML 요청 ->  302 {}", location);
            response.sendRedirect(location);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
                {"code":"NO_AUTH", "message":"로그인이 필요합니다."}""");;

    }
}
