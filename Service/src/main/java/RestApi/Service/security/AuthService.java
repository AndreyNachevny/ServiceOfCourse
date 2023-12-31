package RestApi.Service.security;

import RestApi.Service.dto.JwtResponse;
import RestApi.Service.dto.LoginRequest;
import RestApi.Service.exception.AuthenticationException;
import RestApi.Service.models.User;
import RestApi.Service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public JwtResponse registration(User user){
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new JwtResponse(accessToken,refreshToken);
    }

    public JwtResponse login(LoginRequest request){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Wrong login or password");
        }
        User user = userService.getByEmail(request.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new JwtResponse(accessToken,refreshToken);
    }
}
