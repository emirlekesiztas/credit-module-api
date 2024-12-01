package com.emirhan.ingcasestudy.config;

import com.emirhan.ingcasestudy.entity.User;
import com.emirhan.ingcasestudy.entity.UserRole;
import com.emirhan.ingcasestudy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("dev")
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("emirhan.lekesiztas@gmail.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("emirhan.lekesiztas@gmail.com");
                admin.setPassword(passwordEncoder.encode("test1234"));
                admin.setRole(UserRole.ADMIN);
                admin.setFirstName("Emirhan");
                admin.setLastName("Lekesiztaş");
                admin.setCreditLimit(new BigDecimal("860000"));
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("ahmet.aktan@gmail.com").isEmpty()) {
                User user = new User();
                user.setEmail("ahmet.aktan@gmail.com");
                user.setPassword(passwordEncoder.encode("test1234"));
                user.setFirstName("Ahmet");
                user.setLastName("Aktan");
                user.setRole(UserRole.CUSTOMER);
                user.setCreditLimit(new BigDecimal("500000"));
                userRepository.save(user);
            }
        };
    }
}
