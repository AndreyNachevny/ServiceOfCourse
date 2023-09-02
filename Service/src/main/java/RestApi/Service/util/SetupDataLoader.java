package RestApi.Service.util;

import RestApi.Service.models.Role;
import RestApi.Service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private RoleRepository roleRepository;

    @Autowired
    public SetupDataLoader(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup){
            return;
        }
        createRoleNotFound("ROLE_USER");
        createRoleNotFound("ROLE_ADMIN");
        createRoleNotFound("ROLE_CREATOR");
    }

    @Transactional
    public void createRoleNotFound(String name){
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()){
            Role roleToSave = new Role();
            roleToSave.setName(name);
            roleRepository.save(roleToSave);
        }
    }
}
