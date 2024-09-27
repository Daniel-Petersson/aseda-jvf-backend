package se.leiden.asedajvf.repository;

import se.leiden.asedajvf.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByFirstNameIgnoreCase(String firstName);
    Optional<Member> findByLastNameIgnoreCase(String lastName);
    Optional<Member> findByEmailIgnoreCase(String email);
    Optional<Member> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
