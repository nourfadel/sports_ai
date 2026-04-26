package ADAII.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ErrorResponse {
    private String status;
    private String message;
    Map<String,String> errors;

    public ErrorResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(String status,String message,Map<String, String> errors) {
        this.errors = errors;
        this.message = message;
        this.status = status;
    }

}
