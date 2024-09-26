package se.leiden.asedajvf.dto;

import lombok.*;
import se.leiden.asedajvf.enums.Role;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberDtoView {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private Role role;
    private int postalCode;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;
    private LocalDate membershipPaidUntil;
    private boolean active;
}
