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
    CommandLineRunner populateServerRepoWithDummyData(ServerRepo serverRepo) {
        return args -> {

            //stop if we have servers
            if (!serverRepo.findAll().isEmpty()) {
                return;
            }

            serverRepo.saveAndFlush(new Server(null, "192.168.1.116", "Ubuntu Linux", "16 GB", "PC", "http://localhost:8080/server/image/server1.png", ServerStatus.SERVER_UP));
            serverRepo.saveAndFlush(new Server(null, "192.168.1.117", "Debian Linux", "16 GB", "Server", "http://localhost:8080/server/image/server2.png", ServerStatus.SERVER_DOWN));
            serverRepo.saveAndFlush(new Server(null, "192.168.1.118", "Windows", "64 GB", "Gaming PC", "http://localhost:8080/server/image/server4.png", ServerStatus.SERVER_UP));
            serverRepo.saveAndFlush(new Server(null, "192.168.1.1", "SPARC", "256 GB", "IDK lol", "http://localhost:8080/server/image/server3.png", ServerStatus.SERVER_DOWN));
        };
    }

}
