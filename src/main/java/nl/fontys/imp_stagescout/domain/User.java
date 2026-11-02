package nl.fontys.imp_stagescout.domain;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="Users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long UserId;

    @Column(name ="Username")
    private String username;

    @Column(name = "HashedPassword")
    private String HashedPassword;

    @Column(name = "PhoneNumber")
    private String PhoneNumber;

    @Column(name = "Nationality")
    private String Nationality;

    @Column(name = "PlaceOfResidence")
    private String PlaceOfResidence;

    @Column(name = "ProfileConfig")
    private String ProfileConfig;

    @Column(name = "Email")
    private String email;

    @Column(name = "Experience")
    private int Experience;

    @Column(name = "Description")
    private String Description;

    @ManyToMany
    @JoinTable(
            name = "UserBands",
            joinColumns = @JoinColumn(name = "UserId"),
            inverseJoinColumns = @JoinColumn(name = "BandId")
    )
    private List<Band> bands;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + UserId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
