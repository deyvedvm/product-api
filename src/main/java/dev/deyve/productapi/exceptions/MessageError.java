package dev.deyve.productapi.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class MessageError {

    @JsonProperty("status_code")
    Integer statusCode;

    @JsonProperty("message")
    String message;
}
