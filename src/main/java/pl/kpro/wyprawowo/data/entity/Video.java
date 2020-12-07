package pl.kpro.wyprawowo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Video(String filename)
    {
        this.filename = filename;
    }

    private String filename;
}
