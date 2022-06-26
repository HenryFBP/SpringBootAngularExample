package net.henrypost.server.model;

import lombok.*;
import net.henrypost.server.enumeration.ServerStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String ip;
    String name;
    String memory;
    String type;
    String imageURL;
    ServerStatus serverStatus;

}
