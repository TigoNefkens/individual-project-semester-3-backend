package nl.fontys.imp_stagescout.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GetUserBandsDTO {
    private Long userId;

    public GetUserBandsDTO() {

    }
}
