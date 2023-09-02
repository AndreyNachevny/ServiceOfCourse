package RestApi.Service.services;

import RestApi.Service.dto.RegistrationDto;
import RestApi.Service.exception.NotFoundException;
import RestApi.Service.models.Role;
import RestApi.Service.models.User;
import RestApi.Service.repository.RoleRepository;
import RestApi.Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private  final RoleRepository roleRepository;

    @Transactional
    public void registration(RegistrationDto registrationDto){
        User userToSave = modelMapper.map(registrationDto, User.class);
        List<Role> roles = new ArrayList<>();

        for (String role:registrationDto.getRoles()){
            roles.add(roleRepository.findByName(role).get());
        }
        userToSave.setPassword(encoder.encode(registrationDto.getPassword()));
        userToSave.setRoles(roles);
        userRepository.save(userToSave);
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User with this email does not exist")
        );
    }
}
