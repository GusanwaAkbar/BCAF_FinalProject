package coid.bcafinance.mgaspringfinalexam.service;

import coid.bcafinance.mgaspringfinalexam.model.Privilege;
import coid.bcafinance.mgaspringfinalexam.repo.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public Privilege createPrivilege(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }

    public void deletePrivilege(Long privilegeId) {
        privilegeRepository.deleteById(privilegeId);
    }
}
