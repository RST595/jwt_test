package com.jwt.store;

import com.jwt.store.model.AppUser;
import com.jwt.store.model.UserRole;
import com.jwt.store.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserService userService){
		return args -> {
			userService.saveRole(new UserRole(null, "ROLE_USER"));
			userService.saveRole(new UserRole(null, "MANAGER"));
			userService.saveRole(new UserRole(null, "ADMIN"));
			userService.saveRole(new UserRole(null, "MAIN_ADMIN"));

			userService.saveUser(new AppUser(null, "mail@mail", "string", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "google@google", "string", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "yandex@yandex", "string", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "yahoo@yahoo", "string", new ArrayList<>()));

			userService.addRoleToUser("mail@mail", "ROLE_USER");
			userService.addRoleToUser("mail@mail", "MANAGER");
			userService.addRoleToUser("google@google", "MANAGER");
			userService.addRoleToUser("yandex@yandex", "ADMIN");
			userService.addRoleToUser("yahoo@yahoo", "ROLE_USER");
			userService.addRoleToUser("yahoo@yahoo", "ADMIN");
			userService.addRoleToUser("yahoo@yahoo", "MAIN_ADMIN");
		};
	}

}
