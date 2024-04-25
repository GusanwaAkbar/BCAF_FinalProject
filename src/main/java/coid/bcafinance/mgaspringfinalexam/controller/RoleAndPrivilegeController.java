package coid.bcafinance.mgaspringfinalexam.controller;

import coid.bcafinance.mgaspringfinalexam.model.Privilege;
import coid.bcafinance.mgaspringfinalexam.model.Role;
import coid.bcafinance.mgaspringfinalexam.service.PrivilegeService;
import coid.bcafinance.mgaspringfinalexam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleAndPrivilegeController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @PostMapping("/createRole")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role newRole = roleService.createRole(role);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteRole/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createPrivilege")
    public ResponseEntity<Privilege> createPrivilege(@RequestBody Privilege privilege) {
        Privilege newPrivilege = privilegeService.createPrivilege(privilege);
        return new ResponseEntity<>(newPrivilege, HttpStatus.CREATED);
    }

    @DeleteMapping("/deletePrivilege/{privilegeId}")
    public ResponseEntity<Void> deletePrivilege(@PathVariable Long privilegeId) {
        privilegeService.deletePrivilege(privilegeId);
        return ResponseEntity.ok().build();
    }
}
