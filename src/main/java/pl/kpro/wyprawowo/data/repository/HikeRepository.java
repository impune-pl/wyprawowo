package pl.kpro.wyprawowo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kpro.wyprawowo.data.entity.Hike;

import java.util.Optional;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Repository
public interface HikeRepository extends JpaRepository<Hike,Long>
{
    Optional<Hike> findOneById(Long id);
}
