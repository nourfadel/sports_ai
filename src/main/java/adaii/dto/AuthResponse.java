package adaii.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private int status;
    private String message;
    private String token;
}
