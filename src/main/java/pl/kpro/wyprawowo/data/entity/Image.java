package pl.kpro.wyprawowo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Image
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    public Image(String filename)
    {
        this.filename=filename;
    }
}
