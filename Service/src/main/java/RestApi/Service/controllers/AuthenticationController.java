package RestApi.Service.controllers;

import RestApi.Service.dto.JwtResponse;
import RestApi.Service.dto.LoginRequest;
import RestApi.Service.dto.RegistrationDto;
import RestApi.Service.exception.AuthenticationException;
import RestApi.Service.exception.ErrorResponse;
import RestApi.Service.exception.NotCreatedException;
import RestApi.Service.models.User;
import RestApi.Service.security.AuthService;
import RestApi.Service.services.UserService;
import RestApi.Service.exception.CheckException;
import RestApi.Service.util.RegistrationUserValidate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final ModelMapper modelMapper;
    private final RegistrationUserValidate userValidate;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<JwtResponse> registration(@RequestBody @Valid RegistrationDto registrationDto,
                                                    BindingResult bindingResult){
        User user = modelMapper.map(registrationDto,User.class);
        userValidate.validate(user,bindingResult);
        CheckException.check(bindingResult);
        userService.registration(registrationDto);
        JwtResponse jwtResponse = authService.registration(user);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                             BindingResult bindingResult){
        CheckException.check(bindingResult);
        JwtResponse tokens = authService.login(loginRequest);
        return ResponseEntity.ok((tokens));
    }

    @ExceptionHandler(NotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleNotCreatedException(NotCreatedException e){
        RestApi.Service.exception.ErrorResponse error = new RestApi.Service.exception.ErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException e){
        ErrorResponse error = new ErrorResponse(
                System.currentTimeMillis(),
                e.getMessage()
        );
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
