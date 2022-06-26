package net.henrypost.server.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@SuperBuilder
//@JsonInclude(JsonInclude.Include.NON_NULL)//if a field is null, dont include the field...
public class GenericRESTResponse { //TODO dont do this, we should have unique POJOs...
    LocalDateTime timestamp;
    int statusCode;
    HttpStatus httpStatus;
    String errorReason;
    String userMessage;
    String developerMessage;
    Map<?, ?> data;
}
