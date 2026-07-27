package org.example.freighthandler.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 1. Clean find method
    public User findByUserIdNumber(Long userIdNumber) {
        return userRepository.findByUserId(userIdNumber).orElse(null);
    }

    // 2. Simplified save method
    @Transactional
    public User saveUser(User user) {
        // save() handles both Insert and Update automatically
        return userRepository.save(user);
    }
}
