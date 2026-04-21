package com.bank;

import com.bank.entity.User;
import com.bank.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingSystemApplication.class, args);
	}

    @Bean
    public CommandLineRunner testUserRepo(UserRepository userRepository) {
        return args -> {
            User user = new User();
            user.setUsername("tarun");
            user.setEmail("tarun@gmail.com");
            user.setPassword("1234");
            user.setFullName("Tarun Yendu");
            user.setPhoneNumber("9876543210"); // optional but good to add
            user.setIsActive(true);            // required
            user.setCreatedAt(java.time.LocalDateTime.now()); // required

            userRepository.save(user);

            System.out.println("User saved in DB");
        };
    }
}
