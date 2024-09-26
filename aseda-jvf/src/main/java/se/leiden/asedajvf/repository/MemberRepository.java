package se.leiden.asedajvf.repository;

import se.leiden.asedajvf.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByFirstNameIgnoreCase(String username);
    Optional<Member> findByLastNameIgnoreCase(String username);
    Optional<Member> findByEmailIgnoreCase(String email);
    Optional<Member> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
}
