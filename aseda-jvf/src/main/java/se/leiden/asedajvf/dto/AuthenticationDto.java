package se.leiden.asedajvf.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationDto {
    @NotBlank(message = "Email cannot be empty")
    @Email(regexp="^[A-Za-z0-9+_.-]+@(.+)\\.[a-z]+$", message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}