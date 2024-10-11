package se.leiden.asedajvf.service;

import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.dto.MemberUpdateDtoForm;
import se.leiden.asedajvf.exeptions.AuthenticationException;
import java.util.List;
public interface MemberService {
    MemberDtoView registerMember(MemberDtoForm memberDtoForm);
    String authenticateMember(String email, String password) throws AuthenticationException;
    MemberDtoView updateMember(MemberDtoForm memberDtoForm);
    MemberDtoView getMember(int memberId);
    List<MemberDtoView> getAllMembers();
    boolean deleteMember(int memberId);
    public MemberDtoView updateMemberById(int id, MemberUpdateDtoForm memberUpdateDtoForm);
}
