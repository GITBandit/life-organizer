package pl.java.lifeorganizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.java.lifeorganizer.model.Event;
import pl.java.lifeorganizer.model.EventRepository;
import pl.java.lifeorganizer.model.User;
import pl.java.lifeorganizer.model.UserRepository;

import java.security.Principal;

@Controller
public class EventController {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/event/add")
    public String addEvent(Model model){

        Event event = new Event();

        model.addAttribute("event", event);

        return "event_add";
    }

    @PostMapping("/event/add")
    @ResponseBody
    public String confirmEventAdded(Event event, Principal principal){

        User user = userRepository.getUserByUsername(principal.getName());

        event.setUser(user);

        eventRepository.save(event);

        return "<script>alert(\"Wydarzenie zostało dodane\"); window.location = \"/dashboard\"</script>";
    }

}
