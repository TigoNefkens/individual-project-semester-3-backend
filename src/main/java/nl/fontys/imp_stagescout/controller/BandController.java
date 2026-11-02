package nl.fontys.imp_stagescout.controller;
import nl.fontys.imp_stagescout.DTO.*;
import nl.fontys.imp_stagescout.business.BandServiceInterface;
import nl.fontys.imp_stagescout.security.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/bands")
public class BandController {
    private final BandServiceInterface bandService;
    private final JwtUtil jwtUtil;

    public BandController(BandServiceInterface bandService, JwtUtil jwtUtil) {
        this.bandService = bandService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public ResponseEntity <List<BandDTO>> getAllBands() {
        List<BandDTO> bands = bandService.getAllBands();
        return ResponseEntity.ok(bands);
    }

    @PostMapping
    public ResponseEntity<String> addBand(@RequestBody CreateBandDTO bandDTO) {
        bandService.addBand(bandDTO);
        return ResponseEntity.ok("Successfully created band! , you can view it on your profile");
    }

    @GetMapping("/permissions")
    public ResponseEntity<String> checkCreateAccess(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok("Access granted");
    }

    @PostMapping("/owned")
    public ResponseEntity<List<BandDTO>> getBandsByCreator(
            @RequestBody GetBandsByCreatorDTO dto
    ) {
        List<BandDTO> bands = bandService.getBandsByCreatorId(dto.getUserId());
        return ResponseEntity.ok(bands);
    }

    @PostMapping("/member-of")
    public ResponseEntity<List<BandDTO>> getBandsForUser(@RequestBody GetUserBandsDTO dto) {
        Long userId = dto.getUserId();
        if (userId == null) return ResponseEntity.badRequest().build();

        List<BandDTO> bands = bandService.getBandsForUser(userId);
        return ResponseEntity.ok(bands);
    }

    @PutMapping
    public ResponseEntity<BandDTO> updateBand(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateBandDTO updateBandDTO
    ) {
        String tokenValue = token.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(tokenValue);

        BandDTO updatedBand = bandService.updateBand(email, updateBandDTO);
        return ResponseEntity.ok(updatedBand);
    }
}
