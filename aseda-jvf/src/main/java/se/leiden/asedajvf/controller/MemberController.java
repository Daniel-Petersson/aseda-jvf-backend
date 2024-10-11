package se.leiden.asedajvf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.leiden.asedajvf.dto.AuthenticationDto;
import se.leiden.asedajvf.dto.MemberDtoForm;
import se.leiden.asedajvf.dto.MemberDtoView;
import se.leiden.asedajvf.dto.MemberUpdateDtoForm;
import se.leiden.asedajvf.exeptions.AuthenticationException;
import se.leiden.asedajvf.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateMember(@RequestBody @Valid AuthenticationDto authenticationDto) {
        try {
            String token = memberService.authenticateMember(authenticationDto.getEmail(), authenticationDto.getPassword());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Update member profile", description = "Updates existing member profile")
    @PutMapping("/{id}")
    public ResponseEntity<MemberDtoView> updateMemberById(@PathVariable int id, @RequestBody @Valid MemberUpdateDtoForm memberUpdateDtoForm) {
        MemberDtoView responseBody = memberService.updateMemberById(id, memberUpdateDtoForm);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    @Operation(summary = "Get member Profile", description = "Gets the member profile by id")
    @GetMapping("/{id}/")
    public ResponseEntity<MemberDtoView> getMemberById(@PathVariable int id){
        MemberDtoView responseBody = memberService.getMember(id);
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "Delete member Profile", description = "Deletes the user profile by id")
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<Void> removeMember(@PathVariable int id) {
        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get all members", description = "Retrieves a list of all members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of members")
    })
    @GetMapping("/")
    public ResponseEntity<List<MemberDtoView>> getAllMembers() {
        List<MemberDtoView> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
}
