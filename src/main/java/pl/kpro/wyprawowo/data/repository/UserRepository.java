package pl.kpro.wyprawowo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kpro.wyprawowo.data.entity.User;

import java.util.Optional;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    public Optional<User> findOneByUsername(String username);
}
