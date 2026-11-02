package nl.fontys.imp_stagescout.controller;
import nl.fontys.imp_stagescout.DTO.*;
import nl.fontys.imp_stagescout.security.JwtUtil;
import nl.fontys.imp_stagescout.business.UserServiceInterface;
import nl.fontys.imp_stagescout.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceInterface userService;
    private final JwtUtil jwtUtil;

    public UserController(UserServiceInterface userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody RegisterUserDTO registerUserDTO) {
        try {
            userService.addUser(registerUserDTO);
            return ResponseEntity.ok("Your account has been created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/band-members")
    public ResponseEntity<List<MemberDTO>> getMembers(@RequestBody BandDTO bandDTO) {
        try {
            List<MemberDTO> members = userService.getBandMembers(bandDTO);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO) {
        User user = userService.login(loginUserDTO.getEmail(), loginUserDTO.getPassword());
        if (user == null) return ResponseEntity.status(401).body("Invalid credentials");

        String token = jwtUtil.generateToken(user.getEmail());
        String confirmation = "Login successful!";
        return ResponseEntity.ok(new LoginReponseDTO(token, confirmation, user.getUsername(), user.getUserId()));
    }

    @GetMapping("/details")
    public ResponseEntity<MemberDTO> getUser(@RequestParam Long userId) {
        try {
            MemberDTO creator = userService.findCreatorByID(userId);
            return ResponseEntity.ok(creator);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<String> checkCreateAccess(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok("Access granted");
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateProfileDTO updateProfileDTO
    ) {
        System.out.println("Update DTO: " + updateProfileDTO);
        try {
            String tokenValue = token.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(tokenValue);
            userService.updateProfile(email, updateProfileDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}