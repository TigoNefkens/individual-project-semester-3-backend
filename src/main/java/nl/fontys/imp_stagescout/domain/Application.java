package nl.fontys.imp_stagescout.domain;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BandApplications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApplicationID")
    private Long applicationId;

    @Column(name = "BandId")
    private Long bandId;

    @Column(name = "ApplicantUserId")
    private Long applicantUserId;

    @Column(name = "Content")
    private String content;
}