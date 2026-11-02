package nl.fontys.imp_stagescout.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBandDTO {
    private String bandName;
    private Long creatorUserId;
    private String placeOfOperations;
    private String genre;
    private String description;

}
