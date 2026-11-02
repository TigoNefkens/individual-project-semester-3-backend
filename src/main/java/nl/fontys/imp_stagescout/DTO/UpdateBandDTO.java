package nl.fontys.imp_stagescout.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBandDTO {
    private Long bandId;
    private String Largedescription;
    private String placeOfOperations;
    private String genre;
    private String bandConfig;
}