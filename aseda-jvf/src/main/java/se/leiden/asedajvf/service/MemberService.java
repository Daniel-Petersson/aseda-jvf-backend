package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.exeptions.AuthenticationException;

public interface MemberService {
    MemberDtoView registerMember(MemberDtoForm memberDtoForm);
    String authenticateMember(String email, String password) throws AuthenticationException;
    MemberDtoView updateMember(MemberDtoForm memberDtoForm);
    MemberDtoView getMember(int memberId);
    boolean deleteMember(int memberId);
}
