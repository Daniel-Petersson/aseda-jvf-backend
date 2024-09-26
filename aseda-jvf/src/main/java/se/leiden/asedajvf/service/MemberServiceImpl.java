package se.leiden.asedajvf.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.exeptions.EmailAlreadyExistsException;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.util.CustomPasswordEncoder;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
private final MemberRepository memberRepository;
private final CustomPasswordEncoder customPasswordEncoder;

    @Override
    public MemberDtoView registerMember(MemberDtoForm memberDtoForm) {
        if (memberDtoForm == null) {throw new IllegalArgumentException("MemberDtoForm is null");}
        Optional<Member> memberOptional = memberRepository.findByEmailIgnoreCase(memberDtoForm.getEmail());
        if (memberOptional.isPresent()) {throw new EmailAlreadyExistsException("Email already exist");}
        //Hash the password
        String encodedPassword = customPasswordEncoder.encode(memberDtoForm.getPassword());
        //convert MemberDtoForm to Member
        Member member = MemberMapper.toMember(memberDtoForm);
        member.setPassword(encodedPassword);
        //save the user
        memberRepository.save(member);

        //HttpStatusCode emailStatus = emailService.sendRegistrationEmail(userDTOForm.getEmail());

        //4. Validate Response
       // if (!emailStatus.is2xxSuccessful()) {
        //    System.out.println("was not 200!");
          //  throw new EmailServiceFailedException("Email was not sent successfully.");
        //}
        //convert Member to MemberDTOView and return
        return MemberMapper.toUserDTOView(member);
    }

    @Override
    public boolean authenticateMember(String email, String password) {
        return false;
    }

    @Override
    public MemberDtoView updateMember(MemberDtoForm memberDtoForm) {
        return null;
    }

    @Override
    public MemberDtoView getMember(Long memberId) {
        return null;
    }

    @Override
    public boolean deleteMember(Long memberId) {
        return null;
    }
}
