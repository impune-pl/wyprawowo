package pl.kpro.wyprawowo.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kpro.wyprawowo.data.entity.User;
import pl.kpro.wyprawowo.data.repository.UserRepository;
import pl.kpro.wyprawowo.web.payload.request.UserRegistrationRequest;

import java.util.Optional;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Service
public class UserService implements UserDetailsService
{
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        return userRepository.findOneByUsername(s).orElseThrow(()->new UsernameNotFoundException("User with this username does not exist. Username: " + s));
    }

    public User getUserByUsername(String s) throws UsernameNotFoundException
    {
        return userRepository.findOneByUsername(s).orElseThrow(()->new UsernameNotFoundException("User with this username does not exist. Username: " + s));
    }

    public User registerUser(UserRegistrationRequest request)
    {
        String saltedPasswordHash = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(),saltedPasswordHash);
        return userRepository.save(user);
    }

    public User save(User user)
    {
        return userRepository.save(user);
    }
}
