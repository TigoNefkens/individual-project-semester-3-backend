package nl.fontys.imp_stagescout.exception;

import jakarta.persistence.EntityNotFoundException;
import nl.fontys.imp_stagescout.DTO.AddApplicationResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AddApplicationResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.ok(
                AddApplicationResponseDTO.error("ILLEGAL_ARGUMENT", ex.getMessage())
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<AddApplicationResponseDTO> handleConflict(ConflictException ex) {
        return ResponseEntity.ok(
                AddApplicationResponseDTO.error("CONFLICT", ex.getMessage())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AddApplicationResponseDTO> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.ok(
                AddApplicationResponseDTO.error("NOT_FOUND", ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AddApplicationResponseDTO> handleOther(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.ok(
                AddApplicationResponseDTO.error("SERVER_ERROR", "Something went wrong")
        );
    }
}