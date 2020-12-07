package pl.kpro.wyprawowo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kpro.wyprawowo.data.entity.Image;
import pl.kpro.wyprawowo.data.entity.Video;

import java.util.Optional;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Repository
public interface VideoRepository extends JpaRepository<Video,Long>
{
    Optional<Video> getPictureByFilename(String filename);
    Optional<Video> findTopByOrderByIdDesc();

    Optional<Video> findOneById(long id);
}
