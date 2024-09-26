package se.leiden.asedajvf.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import se.leiden.asedajvf.enums.Role;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberDtoForm {
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;
    @NotBlank(message = "Email cannot be empty")
    @Email(regexp="^[A-Za-z0-9+_.-]+@(.+)\\.[a-z]+$", message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    private String phone;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotBlank(message = "City cannot be empty")
    private String city;
    private Role role;
    @NotBlank(message = "Postal code cannot be empty")
    private int postalCode;
}
