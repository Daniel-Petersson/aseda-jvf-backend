package se.leiden.asedajvf.memberTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.exeptions.DataNotFoundException;
import se.leiden.asedajvf.exeptions.EmailAlreadyExistsException;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.MemberRepository;
import se.leiden.asedajvf.service.MemberServiceImpl;
import se.leiden.asedajvf.util.CustomPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CustomPasswordEncoder customPasswordEncoder;

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
    void authenticateMember_Success() {
        String email = "test@example.com";
        String password = "password";
        Member member = new Member();
        member.setPassword("encodedPassword");

        when(memberRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(member));
        when(customPasswordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        assertTrue(memberService.authenticateMember(email, password));
    }

    @Test
    void authenticateMember_InvalidCredentials() {
        String email = "test@example.com";
        String password = "wrongPassword";
        Member member = new Member();
        member.setPassword("encodedPassword");

        when(memberRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(member));
        when(customPasswordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> memberService.authenticateMember(email, password));
    }

    @Test
    void updateMember_Success() {
        MemberDtoForm form = new MemberDtoForm();
        form.setEmail("test@example.com");
        form.setFirstName("John");
        form.setLastName("Doe");

        Member existingMember = new Member();
        existingMember.setId(1L);

        when(memberRepository.findByEmailIgnoreCase(form.getEmail())).thenReturn(Optional.of(existingMember));
        when(customPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(existingMember);

        MemberDtoView result = memberService.updateMember(form);

        assertNotNull(result);
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
        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);
        member.setEmail("test@example.com");
        member.setFirstName("John");
        member.setLastName("Doe");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        MemberDtoView result = memberService.getMember(memberId);

        assertNotNull(result, "Result should not be null");
        assertEquals(memberId, result.getId(), "ID should match");
        assertEquals("test@example.com", result.getEmail(), "Email should match");
        assertEquals("John", result.getFirstName(), "First name should match");
        assertEquals("Doe", result.getLastName(), "Last name should match");
    }

    @Test
    void getMember_MemberNotFound() {
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> memberService.getMember(memberId));
    }

    @Test
    void deleteMember_Success() {
        Long memberId = 1L;

        when(memberRepository.existsById(memberId)).thenReturn(true);

        assertTrue(memberService.deleteMember(memberId));
        verify(memberRepository).deleteById(memberId);
    }

    @Test
    void deleteMember_MemberNotFound() {
        Long memberId = 1L;

        when(memberRepository.existsById(memberId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> memberService.deleteMember(memberId));
    }
}