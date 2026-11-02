package nl.fontys.imp_stagescout.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    private String username;
    private String password;
    private String phoneNumber;
    private String nationality;
    private String placeOfResidence;
    private String profileConfig;
    private String email;
    private int experience;
    private String description;
}