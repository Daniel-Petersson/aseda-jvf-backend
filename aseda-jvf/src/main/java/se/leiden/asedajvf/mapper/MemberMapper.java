package se.leiden.asedajvf.mapper;

import org.springframework.stereotype.Component;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.model.Member;
@Component
public class MemberMapper {

    public static MemberDtoView toDto(Member member) {
        return MemberDtoView.builder()
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .city(member.getCity())
                .postalCode(member.getPostalCode())
                .dateCreated(member.getDateCreated())
                .dateUpdated(member.getDateUpdated())
                .membershipPaidUntil(member.getMembershipPaidUntil())
                .active(member.isActive())
                .role(member.getRole())
                .build();
    }

    public static Member toMember(MemberDtoForm memberDtoForm) {
        return Member.builder()
                .firstName(memberDtoForm.getFirstName())
                .lastName(memberDtoForm.getLastName())
                .email(memberDtoForm.getEmail())
                .phone(memberDtoForm.getPhone())
                .address(memberDtoForm.getAddress())
                .city(memberDtoForm.getCity())
                .postalCode(memberDtoForm.getPostalCode())
                .build();
    }
}
