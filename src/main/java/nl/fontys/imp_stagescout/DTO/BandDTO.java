package nl.fontys.imp_stagescout.DTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BandDTO {
    private Long BandId;
    private String BandName;
    private Long CreatorUserId;
    private String BandConfig;
    private String PlaceOfOperations;
    private String Genre;
    private String Description;
    private String Largedescription;
}
