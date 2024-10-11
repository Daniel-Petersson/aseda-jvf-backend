package se.leiden.asedajvf.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import se.leiden.asedajvf.enums.Role;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberUpdateDtoForm {
    private String firstName;
    private String lastName;
    @Email(regexp="^[A-Za-z0-9+_.-]+@(.+)\\.[a-z]+$", message = "Invalid email format")
    private String email;
    private String phone;
    private String address;
    private String city;
    private Role role;
    private Integer postalCode;
    private String password; // Optional, only update if provided
    private LocalDate membershipPaidUntil;
    private boolean active;
}