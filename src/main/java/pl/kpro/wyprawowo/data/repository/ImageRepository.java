package pl.kpro.wyprawowo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kpro.wyprawowo.data.entity.Image;

import java.util.Optional;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Repository
public interface ImageRepository extends JpaRepository<Image,Long>
{
    Optional<Image> getImageByFilename(String filename);
    Optional<Image> findTopByOrderByIdDesc();

    Optional<Image> findOneById(long id);
}
