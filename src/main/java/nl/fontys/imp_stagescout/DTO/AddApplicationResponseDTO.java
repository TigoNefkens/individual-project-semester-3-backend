package nl.fontys.imp_stagescout.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddApplicationResponseDTO {
    private boolean success;
    private String errorCode;
    private String message;
    private ApplicationDTO application;

    public static AddApplicationResponseDTO success(ApplicationDTO application) {
        return new AddApplicationResponseDTO(true, null, null, application);
    }

    public static AddApplicationResponseDTO error(String errorCode, String message) {
        return new AddApplicationResponseDTO(false, errorCode, message, null);
    }
}
