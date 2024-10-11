package se.leiden.asedajvf.memberTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll(); // Ensure a clean state before each test
    }

    private Member createValidMember(String firstName, String lastName, String email) {
        Member member = new Member();
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setPassword("securePassword123"); // Set a valid password
        member.setPostalCode(12345); // Set a valid postal code
        member.setCity("Sample City"); // Set a valid city
        member.setAddress("123 Sample Street"); // Set a valid address
        return member;
    }

    @Test
    public void testFindByFirstNameIgnoreCase() {
        Member member = createValidMember("John", "Doe", "john.doe@example.com");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByFirstNameIgnoreCase("john");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testFindByLastNameIgnoreCase() {
        Member member = createValidMember("Jane", "Smith", "jane.smith@example.com");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByLastNameIgnoreCase("smith");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getLastName()).isEqualTo("Smith");
    }

    @Test
    public void testFindByEmailIgnoreCase() {
        Member member = createValidMember("Alice", "Johnson", "alice.johnson@example.com");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByEmailIgnoreCase("ALICE.JOHNSON@EXAMPLE.COM");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("alice.johnson@example.com");
    }

    @Test
    public void testFindByFirstNameIgnoreCaseAndLastNameIgnoreCase() {
        Member member = createValidMember("Bob", "Brown", "bob.brown@example.com");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("bob", "BROWN");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getFirstName()).isEqualTo("Bob");
        assertThat(foundMember.get().getLastName()).isEqualTo("Brown");
    }
}
