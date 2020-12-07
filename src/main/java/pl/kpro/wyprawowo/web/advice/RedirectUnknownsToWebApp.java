package pl.kpro.wyprawowo.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@ControllerAdvice
@RequestMapping(value="/**")
public class RedirectUnknownsToWebApp
{
    @RequestMapping(value = {"/**/{path:[^\\.]*}", "/{path:^(?!/assets/).*}", "/{path:^(?!/api/).*}"}, method = RequestMethod.GET)
    public String index() {
        return "forward:/";
    }
}
