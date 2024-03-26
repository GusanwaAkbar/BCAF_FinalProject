package coid.bcafinance.mgaspringfinalexam.repo;

import coid.bcafinance.mgaspringfinalexam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    /**
     Query untuk proses registrasi
     */
    Optional<User> findTop1ByUsernameOrNoHpOrEmail(String usr, String noHp, String mail);

    /**
     Query untuk proses Login
     Karena hanya login yang sesuai
     */
    Optional<User> findTop1ByUsernameOrNoHpOrEmailAndIsRegistered(String usr, String noHp, String mail, Boolean isRegis);

    Optional<User> findByUsername(String usr);

    /**
     JPQL untuk Query ke Akses
     */
//    @Query("SELECT x FROM User x WHERE  x.idUser = ?1 AND x.post.id = ?2")
//    Optional<User> findByIdUserAndPost(Long idUSer, Long id);
}

/*
Created By IntelliJ IDEA 2023.2.5 (Ultimate Edition)
@Author farha a.k.a. Farkhan Hamzah Firdaus
Java Developer
Crated on 12/03/2024 14:39
@Last Modified 12/03/2024 14:39
Version 1.0
*/