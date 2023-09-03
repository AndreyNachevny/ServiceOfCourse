package RestApi.Service.services;

import RestApi.Service.models.Role;
import RestApi.Service.models.User;
import RestApi.Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailServiceIMPL implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' dont found ", username)
                )
        );
        Hibernate.initialize(user.getRoles());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
               getGrantedAuthorities(user.getRoles())
        );
    }

    private List<GrantedAuthority> getGrantedAuthorities(java.util.Collection<Role> roles){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role:roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

}
