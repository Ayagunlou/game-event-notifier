package api.game_event_notifier.service.user;

import api.game_event_notifier.model.entity.*;
import api.game_event_notifier.model.reponse.*;
import api.game_event_notifier.model.request.*;
import api.game_event_notifier.repository.*;
import api.game_event_notifier.security.SecurityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateUserService {

    private final UserRepository _userRepository;
    private final RoleRepository _roleRepository;
    private final SecurityService _securityService;

    public CreateUserService(UserRepository userRepository, RoleRepository roleRepository, SecurityService securityService) {
        this._userRepository = userRepository;
        this._roleRepository = roleRepository;
        this._securityService = securityService;
    }

    public UserResponseModel createUser(LoginRequestModel newUser) {
        try
        {
            var existUser = _userRepository.findByUsername(newUser.getUsername());
            if (existUser == null)
            {
                var role = _roleRepository.findFirstByOrderByRoleIdAsc();

                if (role == null){
                    throw new RuntimeException("Role is Empty");
                }

                String hashedPassword = _securityService.encrypt(newUser.getPassword());

                var user = new User();
                user.setUsername(newUser.getUsername());
                user.setEmail(newUser.getEmail());
                user.setPasswordHash(hashedPassword);
                user.setIsActive(true);
                user.setIsVerified(false);
                user.setRole(role);
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());

                _userRepository.save(user);

                return new UserResponseModel(user.getUsername(), user.getEmail());
            }
            else
            {
                return new UserResponseModel(existUser.getUsername(), existUser.getEmail());
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Create User Error. ");
        }
    }
}