package pl.kpro.wyprawowo.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kpro.wyprawowo.data.entity.Video;
import pl.kpro.wyprawowo.data.repository.VideoRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Service
public class VideoService
{
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int BYTE_RANGE = 1024;
    private VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository)
    {
        this.videoRepository = videoRepository;
    }

    public ResponseEntity<byte[]> getVideo(long videoId, String range) throws IOException
    {
        Video video = videoRepository.getOne(videoId);
        long rangeStart = 0;
        long rangeEnd;
        byte[] data;
        Long fileSize;
        String fullFileName = video.getFilename();
        try {
            fileSize = getFileSize(fullFileName);
            if (range == null) {
                return ResponseEntity.status(HttpStatus.OK)
                                     .header(CONTENT_TYPE, VIDEO_CONTENT + "mp4")
                                     .header(CONTENT_LENGTH, String.valueOf(fileSize))
                                     .body(readByteRange(fullFileName, rangeStart, fileSize - 1));
            }
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = fileSize - 1;
            }
            if (fileSize < rangeEnd) {
                rangeEnd = fileSize - 1;
            }
            data = readByteRange(fullFileName, rangeStart, rangeEnd);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                             .header(CONTENT_TYPE, VIDEO_CONTENT + "mp4")
                             .header(ACCEPT_RANGES, BYTES)
                             .header(CONTENT_LENGTH, contentLength)
                             .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                             .body(data);

    }

    public byte[] readByteRange(String filename, long start, long end) throws IOException {
        Path path = Paths.get(filename);
        try (InputStream inputStream = (Files.newInputStream(path));
             ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[BYTE_RANGE];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                bufferedOutputStream.write(data, 0, nRead);
            }
            bufferedOutputStream.flush();
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
            return result;
        }
    }

    public Video saveFile(MultipartFile file)
    {
        try
        {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file.");
            }
            Video lastVideo = videoRepository.findTopByOrderByIdDesc().orElseGet(()->{return null;});
            Long number = lastVideo == null ? 1L : lastVideo.getId();
            number++;
            String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            Video vid = new Video(number+format);
            File localFile = new File(number+format);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, localFile.toPath(),
                           StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println(localFile.getCanonicalPath());
            return videoRepository.save(vid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Long getFileSize(String fileName) {
        return Optional.ofNullable(fileName)
                       .map(file -> Paths.get(file))
                       .map(this::sizeFromFile)
                       .orElse(0L);
    }

    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
