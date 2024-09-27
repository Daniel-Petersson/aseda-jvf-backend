package se.leiden.asedajvf.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.service.MemberService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity<MemberDtoView> doRegister(@RequestBody @Valid MemberDtoForm memberDtoForm) {
        MemberDtoView responseBody = memberService.registerMember(memberDtoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticateMember(@RequestBody @Valid MemberDtoForm memberDtoForm){
        boolean responseBody = memberService.authenticateMember(memberDtoForm.getEmail(), memberDtoForm.getPassword());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @PutMapping("/updateMember")
    public ResponseEntity<MemberDtoView> updateMember(@RequestBody @Valid MemberDtoForm memberDtoForm){
        MemberDtoView responseBody = memberService.updateMember(memberDtoForm);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<MemberDtoView> getMemberById(@PathVariable Long id){
        MemberDtoView responseBody = memberService.getMember(id);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<MemberDtoView> removeMember(@PathVariable Long id){
        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
