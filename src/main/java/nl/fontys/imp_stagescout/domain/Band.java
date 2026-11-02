package nl.fontys.imp_stagescout.domain;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="Bands")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BandId")
    private Long bandId;

    @Column(name ="BandName")
    private String BandName;

    @Column(name = "CreatorUserId")
    private Long creatorUserId;

    @Column(name = "BandConfig")
    private String BandConfig;

    @Column(name = "PlaceOfOperations")
    private String PlaceOfOperations;

    @Column(name = "Genre")
    private String Genre;

    @Column(name = "Description")
    private String Description;

    @Column(name = "Largedescription")
    private String Largedescription;

    @ManyToMany(mappedBy = "bands")
    private List<User> users;
}
