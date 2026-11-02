package nl.fontys.imp_stagescout.persistence;
import nl.fontys.imp_stagescout.domain.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandRepo extends JpaRepository<Band,Long> {
    List<Band> findByCreatorUserId(long creatorUserId);
}
