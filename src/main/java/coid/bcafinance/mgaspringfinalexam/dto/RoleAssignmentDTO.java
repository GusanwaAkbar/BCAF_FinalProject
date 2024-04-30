package coid.bcafinance.mgaspringfinalexam.dto;

public class RoleAssignmentDTO {
    private Long userId;
    private Long roleId;

    // Constructors
    public RoleAssignmentDTO() {}

    public RoleAssignmentDTO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
