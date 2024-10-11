package se.leiden.asedajvf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.enums.Role;
import se.leiden.asedajvf.exeptions.AuthenticationException;
import se.leiden.asedajvf.exeptions.DataNotFoundException;
import se.leiden.asedajvf.exeptions.EmailAlreadyExistsException;
import se.leiden.asedajvf.mapper.MemberMapper;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.util.CustomPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@Validated
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final CustomPasswordEncoder customPasswordEncoder;
	private final JwtService jwtService;

	public MemberServiceImpl(MemberRepository memberRepository, CustomPasswordEncoder customPasswordEncoder, JwtService jwtService) {
		this.memberRepository = memberRepository;
		this.customPasswordEncoder = customPasswordEncoder;
		this.jwtService = jwtService;
	}

	@Override
	public MemberDtoView registerMember(MemberDtoForm memberDtoForm) {
		if (memberDtoForm == null) {throw new IllegalArgumentException("MemberDtoForm is null");}
		Optional<Member> memberOptional = memberRepository.findByEmailIgnoreCase(memberDtoForm.getEmail());
		if (memberOptional.isPresent()) {throw new EmailAlreadyExistsException("Email already exist");}
		//Hash the password
		String encodedPassword = customPasswordEncoder.encode(memberDtoForm.getPassword());
		//convert MemberDtoForm to Member
		Member member = MemberMapper.toMember(memberDtoForm);
		// Set password
		member.setPassword(encodedPassword);
		// Always set role to USER for new registrations
		member.setRole(Role.USER);
		member.setDateCreated(LocalDate.now());

		//save the user
		memberRepository.save(member);

		//Todo email verification
		//HttpStatusCode emailStatus = emailService.sendRegistrationEmail(userDTOForm.getEmail());

		//4. Validate Response
		// if (!emailStatus.is2xxSuccessful()) {
		//    System.out.println("was not 200!");
		//    throw new EmailServiceFailedException("Email was not sent successfully.");
		//}
		//convert Member to MemberDTOView and return
		return MemberMapper.toDto(member);
	}

	@Override
	public String authenticateMember(String email, String password) throws AuthenticationException {
		Optional<Member> memberOptional = memberRepository.findByEmailIgnoreCase(email);
		if (memberOptional.isPresent()) {
			Member member = memberOptional.get();
			if (customPasswordEncoder.matches(password, member.getPassword())) {
				String token = jwtService.createToken(member.getFirstName(), member.getId(), member.getRole());
				if (token != null && !token.isEmpty()) {
					return token;
				}
			}
		}
		throw new AuthenticationException("Invalid email or password");
	}

	@Transactional
	@Override
	public MemberDtoView updateMember(MemberDtoForm memberDtoForm) {
		if (memberDtoForm == null) {
			throw new IllegalArgumentException("MemberDtoForm is null");
		}
		Member member = memberRepository.findByEmailIgnoreCase(memberDtoForm.getEmail())
			.orElseThrow(() -> new DataNotFoundException("Member not found"));

		Member updatedMember = Member.builder()
			.id(member.getId())
			.email(memberDtoForm.getEmail())
			.firstName(memberDtoForm.getFirstName())
			.lastName(memberDtoForm.getLastName())
			.address(memberDtoForm.getAddress())
			.city(memberDtoForm.getCity())
			.postalCode(memberDtoForm.getPostalCode())
			.phone(memberDtoForm.getPhone())
			.password(customPasswordEncoder.encode(memberDtoForm.getPassword()))
			.role(member.getRole() != null ? memberDtoForm.getRole() : member.getRole())
			.dateCreated(member.getDateCreated())
			.dateUpdated(LocalDate.now())
			.membershipPaidUntil(member.getMembershipPaidUntil())
			.active(member.isActive())
			.build();

		memberRepository.save(updatedMember);
		return MemberMapper.toDto(updatedMember);
	}

	@Override
	public MemberDtoView getMember(int memberId) {
		return memberRepository.findById(memberId)
			.map(MemberMapper::toDto)
			.orElseThrow(() -> new DataNotFoundException("Member with id: " + memberId + " does not exist"));
	}

	@Override
	@Transactional
	public boolean deleteMember(int memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw new DataNotFoundException("Member with id: " + memberId + " does not exist");
		}
		memberRepository.deleteById(memberId);
		return true;
	}
}
