package nl.fontys.imp_stagescout.business;

import jakarta.persistence.EntityNotFoundException;
import nl.fontys.imp_stagescout.DTO.AddApplicationResponseDTO;
import nl.fontys.imp_stagescout.DTO.ApplicationDTO;
import nl.fontys.imp_stagescout.domain.Application;
import nl.fontys.imp_stagescout.domain.Band;
import nl.fontys.imp_stagescout.domain.User;
import nl.fontys.imp_stagescout.exception.ConflictException;
import nl.fontys.imp_stagescout.persistence.ApplicationRepo;
import nl.fontys.imp_stagescout.persistence.BandRepo;
import nl.fontys.imp_stagescout.persistence.UserRepo;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService implements ApplicationServiceInterface{

    private final ApplicationRepo applicationRepository;
    private final UserRepo userRepository;
    private final BandRepo bandRepository;

    public ApplicationService(ApplicationRepo applicationRepository,
                              UserRepo userRepository,
                              BandRepo bandRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.bandRepository = bandRepository;
    }

    public AddApplicationResponseDTO createApplication(ApplicationDTO dto) {

        Band band = bandRepository.findById(dto.getBandId())
                .orElseThrow(() -> new EntityNotFoundException("Band not found"));

        if (band.getCreatorUserId().equals(dto.getApplicantUserId())) {
            throw new IllegalArgumentException("Band creator cannot apply to their own band");
        }

        if (userRepository.isUserMemberOfBand(dto.getApplicantUserId(), dto.getBandId())) {
            throw new ConflictException("User is already a member of this band");
        }

        if (applicationRepository.existsByBandIdAndApplicantUserId(
                dto.getBandId(), dto.getApplicantUserId())) {
            throw new ConflictException("User already applied to this band");
        }

        Application app = new Application();
        app.setBandId(dto.getBandId());
        app.setApplicantUserId(dto.getApplicantUserId());
        app.setContent(dto.getContent());

        Application saved = applicationRepository.save(app);

        return AddApplicationResponseDTO.success(
                new ApplicationDTO(
                        saved.getApplicationId(),
                        saved.getBandId(),
                        saved.getApplicantUserId(),
                        saved.getContent()
                )
        );
    }


    public List<ApplicationDTO> getApplicationsByBand(Long bandId) {
        return applicationRepository.findByBandId(bandId)
                .stream()
                .map(app -> new ApplicationDTO(
                        app.getApplicationId(),
                        app.getBandId(),
                        app.getApplicantUserId(),
                        app.getContent()
                ))
                .collect(Collectors.toList());
    }


    public void denyApplication(Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        applicationRepository.delete(app);
    }

    public void acceptApplication(Long applicationId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));

        User user = userRepository.findById(app.getApplicantUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Band band = bandRepository.findById(app.getBandId())
                .orElseThrow(() -> new EntityNotFoundException("Band not found"));

        if (!userRepository.isUserMemberOfBand(user.getUserId(), band.getBandId())) {
            user.getBands().add(band);
            userRepository.save(user);
        }

        applicationRepository.deleteById(applicationId);
    }
}
