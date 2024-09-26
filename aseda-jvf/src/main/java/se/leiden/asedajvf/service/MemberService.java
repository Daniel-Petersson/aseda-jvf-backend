package se.leiden.asedajvf.service;

import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;

@Service
public interface MemberService {
    MemberDtoView registerMember(MemberDtoForm memberDtoForm);
    boolean authenticateMember(String email, String password);
    MemberDtoView updateMember(MemberDtoForm memberDtoForm);
    MemberDtoView getMember(Long memberId);
    boolean deleteMember(Long memberId);
}
