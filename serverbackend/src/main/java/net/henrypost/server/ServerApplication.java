package net.henrypost.server;

import lombok.extern.slf4j.Slf4j;
import net.henrypost.server.enumeration.ServerStatus;
import net.henrypost.server.model.jpa.Server;
import net.henrypost.server.repo.ServerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    CommandLineRunner populateServerRepoWithDummyData(ServerRepo serverRepo) {
        return args -> {

            //stop if we have servers
            if (!serverRepo.findAll().isEmpty()) {
                ServerApplication.log.info("Skipping db dummy data insertion.");
                return;
            }

            serverRepo.saveAndFlush(new Server(null, "192.168.1.116", "Ubuntu Linux", "16 GB", "PC", "http://localhost:8080/images/server1.png", ServerStatus.SERVER_UP));
            serverRepo.saveAndFlush(new Server(null, "192.168.1.117", "Debian Linux", "16 GB", "Server", "http://localhost:8080/images/server2.png", ServerStatus.SERVER_DOWN));
            serverRepo.saveAndFlush(new Server(null, "192.168.1.118", "Windows", "64 GB", "Gaming PC", "http://localhost:8080/images/server3.png", ServerStatus.SERVER_UP));
            serverRepo.saveAndFlush(new Server(null, "192.168.1.1", "SPARC", "256 GB", "IDK lol", "http://localhost:8080/images/server4.png", ServerStatus.SERVER_DOWN));
            serverRepo.saveAndFlush(new Server(null, "127.0.0.1", "localhost", "256 GB", "its your pc bro", "http://localhost:8080/images/server1.png", ServerStatus.SERVER_DOWN));
        };
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method", "Filename", "Access-Control-Request-Headers"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("GET POST PUT PATCH DELETE OPTIONS".split(" ")));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        //TODO allowlist the headers and methods? might be necessary.

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
