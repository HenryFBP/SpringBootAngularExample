package net.henrypost.server.controller;

import lombok.RequiredArgsConstructor;
import net.henrypost.server.model.jpa.Server;
import net.henrypost.server.model.rest.GenericRESTResponse;
import net.henrypost.server.service.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static net.henrypost.server.enumeration.ServerStatus.SERVER_UP;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {
    private final ServerService serverService;

    @GetMapping("/list")
    public ResponseEntity<GenericRESTResponse> getServers() {
        return ResponseEntity
                .ok(
                        GenericRESTResponse
                                .builder()
                                .timestamp(now())
                                .data(
                                        of("servers", serverService.list(30))
                                )
                                .userMessage("servers retrieved")
                                .httpStatus(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<GenericRESTResponse> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {

        //make sure ip exists
        if (!this.serverService.doesIPExist(ipAddress)) {
            return ResponseEntity
                    .badRequest()
                    .body(
                            GenericRESTResponse
                                    .builder()
                                    .timestamp(now())
                                    .userMessage("ip %s does not exist".formatted(ipAddress))
                                    .httpStatus(BAD_REQUEST)
                                    .statusCode(BAD_REQUEST.value())
                                    .build()
                    );
        }

        Server server = serverService.ping(ipAddress);

        return ResponseEntity
                .ok(
                        GenericRESTResponse
                                .builder()
                                .timestamp(now())
                                .data(
                                        of("server", server)
                                )
                                .userMessage((server.getServerStatus() == SERVER_UP) ? "ping success" : "ping failed")
                                .httpStatus(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }

    @PostMapping("/save")
    public ResponseEntity<GenericRESTResponse> saveServer(@RequestBody @Valid Server server) {

        //check if the IP is taken
        if (serverService.isIPTaken(server.getIpAddress())) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(
                            GenericRESTResponse
                                    .builder()
                                    .timestamp(now())
                                    .userMessage("conflicting ip %s".formatted(server.getIpAddress()))
                                    .httpStatus(UNPROCESSABLE_ENTITY)
                                    .statusCode(UNPROCESSABLE_ENTITY.value())
                                    .build()
                    );
        }

        this.serverService.create(server);

        return ResponseEntity
                .ok(
                        GenericRESTResponse
                                .builder()
                                .timestamp(now())
                                .data(
                                        of("server", server)
                                )
                                .userMessage("created server %s successfully".formatted(server.getName()))
                                .developerMessage(server.toString())
                                .httpStatus(CREATED)
                                .statusCode(CREATED.value())
                                .build()
                );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GenericRESTResponse> getServer(@PathVariable Long id) {
        return ResponseEntity
                .ok(
                        GenericRESTResponse
                                .builder()
                                .timestamp(now())
                                .data(
                                        of("server", serverService.get(id))
                                )
                                .userMessage("server retrieved")
                                .httpStatus(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericRESTResponse> deleteServer(@PathVariable Long id) {
        return ResponseEntity
                .ok(
                        GenericRESTResponse
                                .builder()
                                .timestamp(now())
                                .data(
                                        of("deleted", serverService.delete(id),
                                                "id", id)
                                )
                                .userMessage("server deleted")
                                .httpStatus(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }

    @GetMapping(path = "/image/{name}", produces = IMAGE_PNG_VALUE)
    //TODO wow this is terrible. frontend should handle this.
    public byte[] getServerImage(@PathVariable String name) throws IOException {
        return Files.readAllBytes(Paths.get("%s/Downloads/images/%s".formatted(System.getProperty("user.home"), name)));//TODO path manipulation vuln? fixme
    }

}
