package net.henrypost.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.henrypost.server.enumeration.ServerStatus;
import net.henrypost.server.model.jpa.Server;
import net.henrypost.server.repo.ServerRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@RequiredArgsConstructor //for dependency injection
@Service
@Transactional
@Slf4j
public class ServerService implements IServerService {

    private final ServerRepo serverRepo;

    @Override
    public Server create(Server server) {
        log.info("Saving server: {}", (server.getName()));
        server.setImageURL(setServerImageURL());
        return this.serverRepo.saveAndFlush(server);
    }

    private String setServerImageURL() {
        String[] imgnames = "server1.png,server2.png,server3.png,server4.png".split(",");

        String name = imgnames[new Random().nextInt(imgnames.length)];

        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/%s".formatted(name)).toUriString();
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP {}", ipAddress);
        Server server = this.serverRepo.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(server.getIpAddress());

        //edit server status
        if (address.isReachable(10000)) {
            server.setServerStatus(ServerStatus.SERVER_UP);
        } else {
            server.setServerStatus(ServerStatus.SERVER_DOWN);
        }

        //save to db, return
        return this.serverRepo.saveAndFlush(server);
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("fetch all servers");
        return this
                .serverRepo
                .findAll(PageRequest.of(0, limit))
                .toList();
    }

    @Override
    public Server get(Long id) {
        log.info("fetch server id={}", id);
        return this.serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("update server {}", server.getName());
        return this.serverRepo.saveAndFlush(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("delete server {}", id);
        this.serverRepo.deleteById(id);
        return true;
    }

    public boolean isIPTaken(String ipAddress) {
        return this.serverRepo.findByIpAddress(ipAddress) != null;
    }

    public boolean doesIPExist(String ipAddress){
        return this.isIPTaken(ipAddress);
    }
}
