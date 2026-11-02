package nl.fontys.imp_stagescout.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDTO {
    private String description;
    private String placeOfResidence;
    private Integer experience;
    private String profileConfig;
}