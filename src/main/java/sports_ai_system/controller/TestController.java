package sports_ai_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // endpoint for Admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(){
        return "Hello Admin:)";
    }

    // endpoint for user
    @PreAuthorize("hasAnyRole('ADMIN','USER','PLAYER')")
    @GetMapping("/user")
    public String user(){
        return "Hello user:)";
    }

}
