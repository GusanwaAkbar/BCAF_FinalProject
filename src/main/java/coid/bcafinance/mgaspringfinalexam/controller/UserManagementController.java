package coid.bcafinance.mgaspringfinalexam.controller;

import coid.bcafinance.mgaspringfinalexam.dto.RoleAssignmentDTO;
import coid.bcafinance.mgaspringfinalexam.model.Role;
import coid.bcafinance.mgaspringfinalexam.model.User;
import coid.bcafinance.mgaspringfinalexam.service.RoleService;
import coid.bcafinance.mgaspringfinalexam.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userManagementService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return userManagementService.findUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/assignRole/{roleId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        User user = userManagementService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleService.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userManagementService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/assignRole")
    public ResponseEntity<?> assignRoleToUser(@RequestBody RoleAssignmentDTO roleAssignment) {
        User user = userManagementService.findUserById(roleAssignment.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleService.findById(roleAssignment.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userManagementService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userManagementService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
