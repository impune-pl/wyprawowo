package pl.kpro.wyprawowo.data.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hike
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate StartDate;

    private Integer lengthInKm;

    private String startCoordinates;

    private String endCoordinates;

    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private User author;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="hike_id")
    private Set<Image> images= new HashSet<>();

    @OneToOne
    private Video video;

    public Hike(LocalDate startDate, Integer lengthInKm, String startCoordinates, String endCoordinates, Video video)
    {
        StartDate = startDate;
        this.lengthInKm = lengthInKm;
        this.startCoordinates = startCoordinates;
        this.endCoordinates = endCoordinates;
        this.video = video;
    }
}
