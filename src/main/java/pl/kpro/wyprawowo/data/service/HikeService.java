package pl.kpro.wyprawowo.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.kpro.wyprawowo.data.entity.Hike;
import pl.kpro.wyprawowo.data.entity.Image;
import pl.kpro.wyprawowo.data.entity.User;
import pl.kpro.wyprawowo.data.entity.Video;
import pl.kpro.wyprawowo.data.repository.HikeRepository;
import pl.kpro.wyprawowo.data.repository.ImageRepository;
import pl.kpro.wyprawowo.data.repository.VideoRepository;
import pl.kpro.wyprawowo.web.payload.request.AddHikeRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Service
public class HikeService
{
    private final HikeRepository hikeRepository;
    private final VideoRepository videoRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    @Autowired
    public HikeService(HikeRepository hikeRepository, VideoRepository videoRepository, ImageRepository imageRepository, UserService userService)
    {
        this.hikeRepository = hikeRepository;
        this.videoRepository = videoRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public Optional<Hike> getHike(Long hikeId)
    {
        return hikeRepository.findOneById(hikeId);
    }

    public Hike addHike(AddHikeRequest addHikeRequest, UserDetails userDetails)
    {
        User user = userService.getUserByUsername(userDetails.getUsername());
        Video video = videoRepository.getOne(addHikeRequest.getVideoId());
        Hike hike = new Hike(addHikeRequest.getStartDate(), addHikeRequest.getLengthInKm(), addHikeRequest.getStartCoordinates(),addHikeRequest.getEndCoordinates(),video);
        hike.setAuthor(user);
        HashSet<Image> images = new HashSet<>();
        for (Long id : addHikeRequest.getImageIds())
        {
            Image image = imageRepository.getOne(id);
            images.add(image);
        }
        hike.getImages().addAll(images);
        hikeRepository.save(hike);
        return hike;
    }

    public List<Hike> getHikesOf(UserDetails userDetails)
    {
        return userService.getUserByUsername(userDetails.getUsername()).getHikes();
    }

    public List<Hike> getHikesOf(UserDetails userDetails, LocalDate date)
    {
        return userService.getUserByUsername(userDetails.getUsername()).getHikes().stream().filter((Hike h)->h.getStartDate().equals(date)).collect(
                Collectors.toList());
    }
}
