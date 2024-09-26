package se.leiden.asedajvf.model;

import jakarta.persistence.*;
import lombok.*;
import se.leiden.asedajvf.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int postalCode;
    private String password;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;
    private LocalDate membershipPaidUntil;
    private boolean active;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentLog> paymentLogs = new ArrayList<>();
}
