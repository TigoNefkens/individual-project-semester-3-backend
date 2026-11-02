package nl.fontys.imp_stagescout.controller;

import nl.fontys.imp_stagescout.DTO.AddApplicationResponseDTO;
import nl.fontys.imp_stagescout.DTO.ApplicationActionDTO;
import nl.fontys.imp_stagescout.DTO.ApplicationDTO;
import nl.fontys.imp_stagescout.business.ApplicationServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationServiceInterface applicationService;

    public ApplicationController(ApplicationServiceInterface applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<AddApplicationResponseDTO> addApplication(
            @RequestBody ApplicationDTO dto) {

        AddApplicationResponseDTO response = applicationService.createApplication(dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/byBand")
    public List<ApplicationDTO> getApplicationsByBand(@RequestParam Long bandId) {
        return applicationService.getApplicationsByBand(bandId);
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> accept(@RequestBody ApplicationActionDTO dto) {
        try {
            applicationService.acceptApplication(dto.getApplicationId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deny(@PathVariable Long applicationId) {
        applicationService.denyApplication(applicationId);
        return ResponseEntity.ok().build();
    }
}