package com.dms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dms.user.entity.*;
import java.util.*;

public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    User getUserObjectByUsernameIgnoreCase(String username);


}
