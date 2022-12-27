package hello.login.web.login;

import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
