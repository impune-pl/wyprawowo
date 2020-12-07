package pl.kpro.wyprawowo.web.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Data
@AllArgsConstructor
public class AddHikeRequest
{
    private Integer lengthInKm;

    private Date StartDate;

    private String startCoordinates;

    private String endCoordinates;

    private Set<Long> imageIds;

    private Long videoId;
}
