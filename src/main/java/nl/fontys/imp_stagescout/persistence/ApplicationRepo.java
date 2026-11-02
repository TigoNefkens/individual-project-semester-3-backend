package nl.fontys.imp_stagescout.persistence;
import nl.fontys.imp_stagescout.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepo extends JpaRepository<Application, Long> {
    List<Application> findByBandId(Long bandId);
    boolean existsByBandIdAndApplicantUserId(Long bandId, Long applicantUserId);
}

