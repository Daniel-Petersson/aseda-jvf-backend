package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Registers a new user", description = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "409", description = "Conflict, user already exists"),
    })
    @PostMapping("/")
    public ResponseEntity<MemberDtoView> doRegister(@RequestBody @Valid MemberDtoForm memberDtoForm) {
        MemberDtoView responseBody = memberService.registerMember(memberDtoForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates a user")
    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticateMember(@RequestBody @Valid MemberDtoForm memberDtoForm){
        boolean responseBody = memberService.authenticateMember(memberDtoForm.getEmail(), memberDtoForm.getPassword());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @Operation(summary = "Update member profile", description = "Updates existing member profile")
    @PutMapping("/updateMember")
    public ResponseEntity<MemberDtoView> updateMember(@RequestBody @Valid MemberDtoForm memberDtoForm){
        MemberDtoView responseBody = memberService.updateMember(memberDtoForm);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @Operation(summary = "Get member Profile", description = "Gets the member profile by id")
    @GetMapping("/{id}/")
    public ResponseEntity<MemberDtoView> getMemberById(@PathVariable Long id){
        MemberDtoView responseBody = memberService.getMember(id);
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Delete member Profile", description = "Deletes the user profile by id")
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<MemberDtoView> removeMember(@PathVariable Long id){
        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
