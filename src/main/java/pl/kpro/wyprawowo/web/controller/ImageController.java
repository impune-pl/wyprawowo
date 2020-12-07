package pl.kpro.wyprawowo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kpro.wyprawowo.data.entity.Image;
import pl.kpro.wyprawowo.data.service.ImageService;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Controller
@RequestMapping("/api/image")
public class ImageController
{
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService)
    {
        this.imageService = imageService;
    }

    @GetMapping(path="/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable long imageId)
    {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageService.getImage(imageId));
    }

    @PostMapping
    public ResponseEntity<?> postImage(@RequestParam("image")  MultipartFile file)
    {
        Image image = imageService.saveFile(file);
        if(image != null)
            return new ResponseEntity<>(image.getId(),HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
