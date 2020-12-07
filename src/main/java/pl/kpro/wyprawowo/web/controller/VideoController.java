package pl.kpro.wyprawowo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kpro.wyprawowo.data.entity.Video;
import pl.kpro.wyprawowo.data.service.VideoService;

import java.io.IOException;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Controller
@RequestMapping("/api/video")
public class VideoController
{
    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService)
    {
        this.videoService = videoService;
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<byte[]> getVideo(@PathVariable long videoId, @RequestHeader(value = "Range", required = false) String range)
    {
        try
        {
            return videoService.getVideo(videoId,range);
        } catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<?> postVideo(@RequestParam("video") MultipartFile file)
    {
        Video video = videoService.saveFile(file);
        if(video != null)
        {
            return new ResponseEntity<>(video.getId(), HttpStatus.ACCEPTED);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
