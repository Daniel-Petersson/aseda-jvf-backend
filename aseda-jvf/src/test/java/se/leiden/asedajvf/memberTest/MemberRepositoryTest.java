package se.leiden.asedajvf.memberTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import se.leiden.asedajvf.model.Member;
import se.leiden.asedajvf.enums.Role;
import se.leiden.asedajvf.repository.MemberRepository;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .address("123 Main St")
                .city("Anytown")
                .role(Role.ADMIN)
                .postalCode(12345)
                .password("password123")
                .active(true)
                .build();
        testMember = entityManager.persistAndFlush(testMember);
        System.out.println("Persisted member: " + testMember);

    }

    @Test
    void findByFirstNameIgnoreCase() {
        Optional<Member> found = memberRepository.findByFirstNameIgnoreCase("john");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualToIgnoringCase("John");
    }

    @Test
    void findByLastNameIgnoreCase() {
        Optional<Member> found = memberRepository.findByLastNameIgnoreCase("DOE");
        assertThat(found).isPresent();
        assertThat(found.get().getLastName()).isEqualToIgnoringCase("Doe");
    }

    @Test
    void findByEmailIgnoreCase() {
        Optional<Member> found = memberRepository.findByEmailIgnoreCase("JOHN.DOE@EXAMPLE.COM");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualToIgnoringCase("john.doe@example.com");
    }

    @Test
    void findByFirstNameIgnoreCaseAndLastNameIgnoreCase() {
        // Debug: Print all members in the database
        List<Member> allMembers = memberRepository.findAll();
        System.out.println("All members in database: " + allMembers);

        Optional<Member> found = memberRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("JOHN", "doe");
        
        // Debug: Print the result of the query
        System.out.println("Query result: " + found);

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualToIgnoringCase("John");
        assertThat(found.get().getLastName()).isEqualToIgnoringCase("Doe");
    }

    @Test
    void findByFirstNameIgnoreCase_NotFound() {
        Optional<Member> notFound = memberRepository.findByFirstNameIgnoreCase("Jane");
        assertThat(notFound).isEmpty();
    }

    @Test
    void findByLastNameIgnoreCase_NotFound() {
        Optional<Member> notFound = memberRepository.findByLastNameIgnoreCase("Smith");
        assertThat(notFound).isEmpty();
    }

    @Test
    void findByEmailIgnoreCase_NotFound() {
        Optional<Member> notFound = memberRepository.findByEmailIgnoreCase("jane.doe@example.com");
        assertThat(notFound).isEmpty();
    }

    @Test
    void findByFirstNameAndLastNameIgnoreCase_NotFound() {
        Optional<Member> notFound = memberRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("Jane", "Smith");
        assertThat(notFound).isEmpty();
    }
}