package pl.kpro.wyprawowo.data.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kpro.wyprawowo.data.entity.Image;
import pl.kpro.wyprawowo.data.repository.ImageRepository;
import java.nio.file.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Service
public class ImageService
{
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository)
    {
        this.imageRepository = imageRepository;
    }

    public Image saveFile(MultipartFile file)
    {
        try
        {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file.");
            }
            Image lastImage = imageRepository.findTopByOrderByIdDesc().orElseGet(()->{return null;});
            Long number = lastImage == null ? 0L : lastImage.getId();
            number++;
            String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            Image img = new Image(number+format);
            File localFile = new File(number+format);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, localFile.toPath(),
                           StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println(localFile.getCanonicalPath());
            return imageRepository.save(img);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getImage(long id)
    {
        String imageName = imageRepository.findOneById(id).get().getFilename();
        try(FileInputStream fis = new FileInputStream(imageName)) {
            return fis.readAllBytes();
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
