package hello;

import hello.model.CustomUserDetails;
import hello.model.StringResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping(method = RequestMethod.GET, path = "/greeting", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }


    @RequestMapping(method = RequestMethod.GET, path = "/user/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<StringResponse> updateDescription(@PathVariable("userId") int userId, @AuthenticationPrincipal CustomUserDetails user) throws URISyntaxException {
        // code for updating the description for the specified user
        HttpHeaders responseHeaders = new HttpHeaders();
        URI location = new URI("http://www.google.de");
        responseHeaders.setLocation(location);
        responseHeaders.set("h1", "h1Value");

        if (user.getUserId() == userId) {
            return new ResponseEntity<>(new StringResponse("Du bist der richtige"), responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StringResponse("Du bist der falsche"), responseHeaders, HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(method = RequestMethod.POST, path = "/v1/greeting", consumes = {"application/json"}, produces = {"application/json"})
    public List<Greeting> createGreeting(@RequestBody Greeting greeting) {
        String format = String.format("I got greeting with id %d and message %s ", greeting.getId(), greeting.getContent());
        greeting.setContent(format);

        if (greeting.getId() == 0) {
            throw new IllegalArgumentException("Invalid argument");
        }
        return Arrays.asList(greeting);
    }

//    @RequestMapping(method = RequestMethod.POST, path = "/test", consumes = {"application/json"}, produces = MediaType.APPLICATION_XML_VALUE)
//    public Greeting createTestGreeting(@RequestBody Greeting payLoad){
//        return new Greeting(10, "TestGreeting with " + payLoad);
//    }

    @ExceptionHandler
    void handleIllegalArgumentException(Exception e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }


}
