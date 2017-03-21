package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import repository.UserRepository;

/**
 * Created by alex on 17.18.3.
 */
@Controller
@RequestMapping("/blacklist")
public class BlacklistController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addToBlacklist(@RequestParam("personalId") String personalId){
        userRepository.addToBlackList(personalId);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeFromBlacklist(@RequestParam("personalId") String personalId){
        userRepository.removeFromBlackList(personalId);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> errorHandler(Throwable t) {
        t.printStackTrace();
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
