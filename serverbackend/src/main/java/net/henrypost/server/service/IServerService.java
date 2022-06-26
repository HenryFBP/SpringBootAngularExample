package net.henrypost.server.service;

import net.henrypost.server.model.jpa.Server;

import java.io.IOException;
import java.util.Collection;

public interface IServerService {

    Server create(Server server);
    Server ping(String ipAddress) throws IOException;
    Collection<Server> list(int limit);
    Server get(Long id);
    Server update(Server server);
    Boolean delete(Long id);
}
