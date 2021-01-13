package pl.kpro.wyprawowo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kpro.wyprawowo.data.entity.Hike;
import pl.kpro.wyprawowo.data.entity.User;
import pl.kpro.wyprawowo.data.service.HikeService;
import pl.kpro.wyprawowo.data.service.UserService;
import pl.kpro.wyprawowo.web.payload.request.AddHikeRequest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Controller
@RequestMapping("/api/hike")
public class HikeController
{
    private final HikeService hikeService;
    private final UserService userService;

    @Autowired
    public HikeController(HikeService hikeService, UserService userService)
    {
        this.hikeService = hikeService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Hike>> getHikes(@RequestParam(value="date") @DateTimeFormat(pattern="yyyy-MM-dd") Optional<LocalDate> date,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();
        return date.map(value -> new ResponseEntity<>(hikeService.getHikesOf(userDetails, value), HttpStatus.ACCEPTED))
                   .orElseGet(() -> new ResponseEntity<>(hikeService.getHikesOf(userDetails), HttpStatus.ACCEPTED));
    }

    @GetMapping("/{hikeId}")
    public ResponseEntity<?> getHike(@PathVariable Long hikeId)
    {
        Optional<Hike> hike = hikeService.getHike(hikeId);
        if(hike.isPresent())
        {
            return new ResponseEntity<>(hike.get(), HttpStatus.ACCEPTED);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> postHike(@RequestBody AddHikeRequest addHikeRequest, @AuthenticationPrincipal UserDetails userDetails)
    {
        Hike hike = hikeService.addHike(addHikeRequest, userDetails);
        if(hike != null)
        {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
