package nl.fontys.imp_stagescout.business;

import nl.fontys.imp_stagescout.DTO.BandDTO;
import nl.fontys.imp_stagescout.DTO.CreateBandDTO;
import nl.fontys.imp_stagescout.DTO.UpdateBandDTO;
import nl.fontys.imp_stagescout.domain.User;
import nl.fontys.imp_stagescout.persistence.UserRepo;
import org.springframework.stereotype.Service;
import nl.fontys.imp_stagescout.domain.Band;
import nl.fontys.imp_stagescout.persistence.BandRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class BandService implements BandServiceInterface {
    private final BandRepo bandRepo;
    private final UserRepo userRepo;

    public BandService(BandRepo bandRepo, UserRepo userRepo) {
        this.bandRepo = bandRepo;
        this.userRepo = userRepo;
    }

    public void addBand(CreateBandDTO bandDTO) {

        if (bandDTO.getBandName() == null || bandDTO.getBandName().isBlank()) {
            throw new IllegalArgumentException("Band name is required");
        }

        Band band = new Band();
        band.setBandName(bandDTO.getBandName());
        band.setCreatorUserId(bandDTO.getCreatorUserId());
        band.setPlaceOfOperations(bandDTO.getPlaceOfOperations());
        band.setGenre(bandDTO.getGenre());
        band.setDescription(bandDTO.getDescription());

        bandRepo.save(band);
    }


    public List<BandDTO> getAllBands() {
        List<Band> bands = bandRepo.findAll();
        List<BandDTO> dtoList = new ArrayList<>();

        for (Band band : bands) {
            BandDTO dto = new BandDTO(
                    band.getBandId(),
                    band.getBandName(),
                    band.getCreatorUserId(),
                    band.getBandConfig(),
                    band.getPlaceOfOperations(),
                    band.getGenre(),
                    band.getDescription(),
                    band.getLargedescription()
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public List<BandDTO> getBandsByCreatorId(long userId) {
        List<Band> bands = bandRepo.findByCreatorUserId(userId);
        List<BandDTO> dtoList = new ArrayList<>();

        for (Band band : bands) {
            BandDTO dto = new BandDTO(
                    band.getBandId(),
                    band.getBandName(),
                    band.getCreatorUserId(),
                    band.getBandConfig(),
                    band.getPlaceOfOperations(),
                    band.getGenre(),
                    band.getDescription(),
                    band.getLargedescription()
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public List<BandDTO> getBandsForUser(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        List<BandDTO> dtoList = new ArrayList<>();
        for (Band band : user.getBands()) {
            BandDTO dto = new BandDTO(
                    band.getBandId(),
                    band.getBandName(),
                    band.getCreatorUserId(),
                    band.getBandConfig(),
                    band.getPlaceOfOperations(),
                    band.getGenre(),
                    band.getDescription(),
                    band.getLargedescription()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    public BandDTO updateBand(String userEmail, UpdateBandDTO dto) {
        System.out.println(dto);

        Band band = bandRepo.findById(dto.getBandId())
                .orElseThrow(() -> new RuntimeException("Band not found"));


        if (dto.getLargedescription() != null) band.setLargedescription(dto.getLargedescription());
        if (dto.getPlaceOfOperations() != null) band.setPlaceOfOperations(dto.getPlaceOfOperations());
        if (dto.getGenre() != null) band.setGenre(dto.getGenre());
        if (dto.getBandConfig() != null) band.setBandConfig(dto.getBandConfig());
        System.out.println(band);
        Band savedBand = bandRepo.save(band);

        return new BandDTO(
                savedBand.getBandId(),
                savedBand.getBandName(),
                savedBand.getCreatorUserId(),
                savedBand.getBandConfig(),
                savedBand.getPlaceOfOperations(),
                savedBand.getGenre(),
                savedBand.getDescription(),
                savedBand.getLargedescription()
        );
    }

}
