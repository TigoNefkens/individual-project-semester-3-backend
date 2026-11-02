package nl.fontys.imp_stagescout.business;
import nl.fontys.imp_stagescout.DTO.BandDTO;
import nl.fontys.imp_stagescout.DTO.CreateBandDTO;
import nl.fontys.imp_stagescout.DTO.UpdateBandDTO;

import java.util.List;

public interface BandServiceInterface {
    void addBand(CreateBandDTO bandDTO);
    List<BandDTO> getAllBands();
    List<BandDTO> getBandsByCreatorId(long userId);
    BandDTO updateBand(String userEmail, UpdateBandDTO dto);
    List<BandDTO> getBandsForUser(Long userId);
}
