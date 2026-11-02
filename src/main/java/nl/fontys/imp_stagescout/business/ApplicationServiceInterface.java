package nl.fontys.imp_stagescout.business;

import nl.fontys.imp_stagescout.DTO.AddApplicationResponseDTO;
import nl.fontys.imp_stagescout.DTO.ApplicationDTO;

import java.util.List;

public interface ApplicationServiceInterface {
    AddApplicationResponseDTO createApplication(ApplicationDTO dto);
    List<ApplicationDTO> getApplicationsByBand(Long bandId);
    void acceptApplication(Long applicationId);
    void denyApplication(Long applicationId);
}
