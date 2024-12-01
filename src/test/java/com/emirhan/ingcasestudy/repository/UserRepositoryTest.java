package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.User;
import com.emirhan.ingcasestudy.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testExistsByEmail_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.CUSTOMER);
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    void testFindByEmail_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.CUSTOMER);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }
}
