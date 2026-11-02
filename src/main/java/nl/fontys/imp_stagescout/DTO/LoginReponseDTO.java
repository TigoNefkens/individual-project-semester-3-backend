package nl.fontys.imp_stagescout.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginReponseDTO {
    private String token;
    private String message;
    private String username;
    private Long Id;
}
