package RestApi.Service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
}
