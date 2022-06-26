package net.henrypost.server.repo;

import net.henrypost.server.model.jpa.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepo extends JpaRepository<Server, Long> {
    //how do these work? Spring JPA uses reflection to parse the name of these methods to automatically generate filters. Pretty cool :D
    Server findByIpAddress(String ipAddress);

    List<Server> findByName(String name);
}
