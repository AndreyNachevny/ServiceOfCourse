package RestApi.Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class RegistrationDto {

    @NotNull
    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy",timezone="Europe/Moscow")
    @NotNull
    private Date dateOfBirth;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private List<String> roles;

}
