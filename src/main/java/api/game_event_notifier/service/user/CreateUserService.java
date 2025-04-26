package api.game_event_notifier.service.user;

import api.game_event_notifier.model.entity.*;
import api.game_event_notifier.model.reponse.*;
import api.game_event_notifier.model.request.*;
import api.game_event_notifier.security.SecurityService;
import api.game_event_notifier.service.repository.ServiceRepository;
import api.game_event_notifier.service.role.CreateRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;

@Service
public class CreateUserService {

    private final ServiceRepository _serviceRepository;
    private final SecurityService _securityService;
    private final PlatformTransactionManager _transactionManager;
    private static final Logger logger = LoggerFactory.getLogger(CreateUserService.class);

    public CreateUserService(ServiceRepository serviceRepository, SecurityService securityService, PlatformTransactionManager transactionManager) {
        this._serviceRepository = serviceRepository;
        this._securityService = securityService;
        this._transactionManager = transactionManager;
    }

    public UserResponseModel createUser(LoginRequestModel newUser) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("CreateUserTx"); //ชื่อมีประโยชน์ตอน Debug หรือ Log — เวลาอยากรู้ว่า transaction ไหนกำลังทำงานอยู่
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = _transactionManager.getTransaction(def);
        try
        {
            var existUser = _serviceRepository.getUser().findByUsername(newUser.getUsername());
            if (existUser == null)
            {
                var role = _serviceRepository.getRole().findFirstByOrderByRoleIdAsc();

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

                _serviceRepository.getUser().save(user);

                _transactionManager.commit(status);

                return new UserResponseModel(user.getUsername(), user.getEmail());
            }
            else
            {
                throw new RuntimeException("User Already. ");
            }
        }
        catch (Exception ex)
        {
            _transactionManager.rollback(status);
            logger.error(ex.getMessage(),ex);
            throw new RuntimeException("Create User Error. ");
        }
    }
}