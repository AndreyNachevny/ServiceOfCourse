package RestApi.Service.util;

import RestApi.Service.models.User;
import RestApi.Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegistrationUserValidate implements Validator {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
            Optional<User> userCheckEmail = userRepository.findByEmail(user.getEmail());
            ArrayList<String> passwords = (ArrayList<String>) userRepository.getAllPassword();

            if (userCheckEmail.isPresent()) {
                if (encoder.matches(user.getPassword(), userCheckEmail.get().getPassword())) {
                    errors.rejectValue("password", "", "A user with the same email and password already exists");
                    return;
                }
                errors.rejectValue("email", "", "A user with  email already exists");
                return;
            }
            for (String password : passwords) {
                if (encoder.matches(user.getPassword(), password)) {
                    errors.rejectValue("password", "", "This password already using");
                }
            }
    }
}
