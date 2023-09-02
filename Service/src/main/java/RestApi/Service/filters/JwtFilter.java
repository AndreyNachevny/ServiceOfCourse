package RestApi.Service.filters;

import RestApi.Service.security.JwtService;
import RestApi.Service.services.UserDetailServiceIMPL;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailServiceIMPL userDetailServiceIMPL;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = extractJwt(request);
        if(jwt != null && jwtService.validateAccessToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null){
            Claims claims = jwtService.getAccessClaims(jwt);
            UserDetails userDetails = userDetailServiceIMPL.loadUserByUsername(claims.getSubject());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities())
            );
        }
        filterChain.doFilter(request,response);
    }

    private String extractJwt(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }
}
