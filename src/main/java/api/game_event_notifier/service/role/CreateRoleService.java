package api.game_event_notifier.service.role;

import api.game_event_notifier.model.entity.*;
import api.game_event_notifier.model.request.*;
import api.game_event_notifier.service.auth.AuthService;
import api.game_event_notifier.service.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;

@Service
public class CreateRoleService {

    private final ServiceRepository _serviceRepository;
    private final PlatformTransactionManager _transactionManager;
    private static final Logger logger = LoggerFactory.getLogger(CreateRoleService.class);

    public CreateRoleService(ServiceRepository serviceRepository, PlatformTransactionManager transactionManager) {
        this._serviceRepository = serviceRepository;
        this._transactionManager = transactionManager;
    }

    public Role createRole(RoleRequestModel requestModel) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("CreateRoleTx"); //ชื่อมีประโยชน์ตอน Debug หรือ Log — เวลาอยากรู้ว่า transaction ไหนกำลังทำงานอยู่
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = _transactionManager.getTransaction(def);
        try
        {
            var existRole = _serviceRepository.getRole().findByRoleName(requestModel.getRoleName());
            if (existRole == null)
            {
                var role = new Role();
                role.setRoleName(requestModel.getRoleName());
                role.setDescription(requestModel.getDescription());
                role.setCreatedAt(LocalDateTime.now());
                role.setUpdatedAt(LocalDateTime.now());

                _serviceRepository.getRole().save(role);

                _transactionManager.commit(status);

                return role;
            }
            else
            {
                return existRole;
            }
        }
        catch (Exception ex)
        {
            _transactionManager.rollback(status);
            logger.error(ex.getMessage(),ex);
            throw new RuntimeException("Create Role Fail. ");
        }
    }
}