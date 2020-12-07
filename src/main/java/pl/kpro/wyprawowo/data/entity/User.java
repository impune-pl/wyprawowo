package pl.kpro.wyprawowo.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "username", columnList = "username", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String role="USER";

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    private List<Hike> hikes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return this.password;
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password=password;
    }

    public void addHike(Hike hike)
    {
        this.hikes.add(hike);
    }
}
