package nl.fontys.imp_stagescout.persistence;
import nl.fontys.imp_stagescout.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByEmail(String email);
    List<User> findByBands_BandId(Long bandId);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.bands b WHERE u.UserId = :userId AND b.bandId = :bandId")
    boolean isUserMemberOfBand(@Param("userId") Long userId, @Param("bandId") Long bandId);
}
