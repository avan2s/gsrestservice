package hello.service;

import hello.model.Role;
import hello.model.User;
import hello.repository.RoleRepository;
import hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("roleRepository")
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user, Role role) {
        // hash the password for a new user
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        // make user active
        user.setActive(1);
        // set the roles
        Role defaultRole = this.roleRepository.findByRole("USER");

        Role userRole = role == null ? defaultRole : role;
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        // save the user
        this.userRepository.save(user);
    }

    @PostConstruct
    public void init() {
        User userInDb = this.userRepository.findByEmail("test@web.de");
        if (userInDb == null) {
            Role role = new Role();
            role.setRole("TESTROLE");
            role = this.roleRepository.save(role);

            User user = new User();
            user.setName("test");
            user.setPassword("secure");
            user.setLastName("haha");
            user.setEmail("test@web.de");
            this.saveUser(user, role);
        }
    }
}
