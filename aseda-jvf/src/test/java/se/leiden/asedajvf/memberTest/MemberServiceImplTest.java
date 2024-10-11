package se.leiden.asedajvf.memberTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.enums.Role;
import se.leiden.asedajvf.exeptions.DataNotFoundException;
import se.leiden.asedajvf.exeptions.EmailAlreadyExistsException;
import se.leiden.asedajvf.exeptions.AuthenticationException;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.service.MemberServiceImpl;
import se.leiden.asedajvf.util.CustomPasswordEncoder;
import se.leiden.asedajvf.service.JwtService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CustomPasswordEncoder customPasswordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerMember_Success() {
        MemberDtoForm form = new MemberDtoForm();
        form.setEmail("test@example.com");
        form.setPassword("password");

        when(memberRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(customPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(new Member());

        MemberDtoView result = memberService.registerMember(form);

        assertNotNull(result);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void registerMember_EmailAlreadyExists() {
        MemberDtoForm form = new MemberDtoForm();
        form.setEmail("existing@example.com");

        when(memberRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(new Member()));

        assertThrows(EmailAlreadyExistsException.class, () -> memberService.registerMember(form));
    }

    @Test
    void authenticateMember_Success() throws AuthenticationException {
        String email = "test@example.com";
        String password = "password";
        Member member = new Member();
        member.setPassword("encodedPassword");
        member.setFirstName("John");
        member.setId(1);
        member.setRole(Role.USER); // Assuming you have a Role enum

        when(memberRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(member));
        when(customPasswordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        when(jwtService.createToken(eq("John"), eq(1), eq(Role.USER))).thenReturn("token");

        String token = memberService.authenticateMember(email, password);
        System.out.println("Generated token: " + token); // Debug print
        assertNotNull(token);
        assertEquals("token", token);
    }

    @Test
    void authenticateMember_InvalidCredentials() {
        String email = "test@example.com";
        String password = "wrongPassword";
        Member member = new Member();
        member.setPassword("encodedPassword");

        when(memberRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(member));
        when(customPasswordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> memberService.authenticateMember(email, password));
    }

    @Test
    void updateMember_Success() {
        int memberId = 1;
        MemberDtoForm form = new MemberDtoForm();
        form.setEmail("updated@example.com");
        Member existingMember = new Member();
        existingMember.setId(memberId);

        when(memberRepository.findByEmailIgnoreCase(form.getEmail())).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(any(Member.class))).thenReturn(existingMember);

        MemberDtoView result = memberService.updateMember(form);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void updateMember_MemberNotFound() {
        MemberDtoForm form = new MemberDtoForm();
        form.setEmail("nonexistent@example.com");

        when(memberRepository.findByEmailIgnoreCase(form.getEmail())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> memberService.updateMember(form));
    }

    @Test
    void getMember_Success() {
        int memberId = 1;
        Member member = new Member();
        member.setId(memberId);
        member.setEmail("test@example.com");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        MemberDtoView result = memberService.getMember(memberId);

        assertNotNull(result);
        assertEquals(memberId, result.getId());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getMember_MemberNotFound() {
        int memberId = 1;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> memberService.getMember(memberId));
    }

    @Test
    void deleteMember_Success() {
        int memberId = 1;

        when(memberRepository.existsById(memberId)).thenReturn(true);

        assertTrue(memberService.deleteMember(memberId));
        verify(memberRepository).deleteById(memberId);
    }

    @Test
    void deleteMember_MemberNotFound() {
        int memberId = 1;

        when(memberRepository.existsById(memberId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> memberService.deleteMember(memberId));
    }
}