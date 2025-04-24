package api.game_event_notifier.service.role;

import api.game_event_notifier.model.entity.*;
import api.game_event_notifier.model.request.*;
import api.game_event_notifier.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateRoleService {

    private final RoleRepository _roleRepository;

    public CreateRoleService(RoleRepository roleRepository) {
        this._roleRepository = roleRepository;
    }

    public Role createRole(RoleRequestModel requestModel) {
        try
        {
            var existRole = _roleRepository.findByRoleName(requestModel.getRoleName());
            if (existRole == null)
            {
                var role = new Role();
                role.setRoleName(requestModel.getRoleName());
                role.setDescription(requestModel.getDescription());
                role.setCreatedAt(LocalDateTime.now());
                role.setUpdatedAt(LocalDateTime.now());

                _roleRepository.save(role);

                return role;
            }
            else
            {
                return existRole;
            }
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}