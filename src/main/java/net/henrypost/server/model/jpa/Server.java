package net.henrypost.server.model.jpa;

import lombok.*;
import net.henrypost.server.enumeration.ServerStatus;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    @NotEmpty(message = "IP Address cannot be empty")
    String ipAddress;
    String name;
    String memory;
    String type;
    String imageURL;
    ServerStatus serverStatus;
}
