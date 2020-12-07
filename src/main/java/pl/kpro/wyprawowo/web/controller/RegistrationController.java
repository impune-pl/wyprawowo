package pl.kpro.wyprawowo.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kpro.wyprawowo.data.service.UserService;
import pl.kpro.wyprawowo.web.payload.request.UserRegistrationRequest;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@RestController
@RequestMapping("/api/register")
public class RegistrationController
{
    private final UserService userService;

    public RegistrationController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest userRegistrationRequest)
    {
        if(userRegistrationRequest.getPassword().equals(userRegistrationRequest.getRepeatPassword()))
        {
            userService.registerUser(userRegistrationRequest);
            return new ResponseEntity<>("Account registered, proceed to login", HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>("Passwords do not match!",HttpStatus.BAD_REQUEST);
    }
}
