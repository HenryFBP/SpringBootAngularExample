package net.henrypost.server;

import net.henrypost.server.enumeration.ServerStatus;
import net.henrypost.server.model.jpa.Server;
import net.henrypost.server.repo.ServerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ServerRepo serverRepo) {
        return args -> {

            if (!serverRepo.findAll().isEmpty()) {
                return;
            }

            serverRepo.save(new Server(null, "192.168.1.116", "Ubuntu Linux", "16 GB", "PC", "http://localhost:8080/server/image/server1.png", ServerStatus.SERVER_UP));
        };
    }

}
