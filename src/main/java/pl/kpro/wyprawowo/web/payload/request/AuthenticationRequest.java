package pl.kpro.wyprawowo.web.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Data
@AllArgsConstructor
public class AuthenticationRequest
{
    private String username;
    private String password;
}
