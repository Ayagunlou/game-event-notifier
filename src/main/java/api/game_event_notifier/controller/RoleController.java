package api.game_event_notifier.controller;

import api.game_event_notifier.model.entity.*;
import api.game_event_notifier.model.request.*;
import api.game_event_notifier.service.role.CreateRoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final CreateRoleService _createRoleService;

    public RoleController(CreateRoleService createRoleService) {
        this._createRoleService = createRoleService;
    }

    @PostMapping("/create")
    public Role CreateRole(@RequestBody RoleRequestModel roleRequestModel) {
        return _createRoleService.createRole(roleRequestModel);
    }
}