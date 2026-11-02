package nl.fontys.imp_stagescout.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String nationality;
    private String placeOfResidence;
    private String profileConfig;
    private int experience;
    private String description;
}
