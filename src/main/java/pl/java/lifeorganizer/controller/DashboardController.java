package pl.java.lifeorganizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.java.lifeorganizer.model.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonalDataRepository personalDataRepository;
    @Autowired
    EventRepository eventRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal){

        PersonalData personalData = getPersonalDataForUserLoggedIn(principal);

        try {
            LocalDate birthDate = personalData.getBirthDate();

            model.addAttribute("birthDate", birthDate);  //to be removed

            Long periodToNextBirthDay = calculatePeriodBetweenNextBirthDayAndNow(birthDate);

            model.addAttribute("period", periodToNextBirthDay);
        } catch (NullPointerException e){
            return "no_events";
        }

        try {

            List<Event> eventList = getEventListForUserLoggedIn(principal);

            model.addAttribute("eventList", eventList);

            ArrayList<Long> periods = new ArrayList<>();

            for (Event event : eventList) {
                LocalDate eventDate = event.getEventDate();
                Long periodBetweenEventAndNow = calculatePeriodBetweenEventAndNow(eventDate);
                periods.add(periodBetweenEventAndNow);
//                periods.add(calculatePeriodBetweenEventAndNow(event.getEventDate()));
            }

            model.addAttribute("periods", periods);

        } catch (NullPointerException e){
            return "no_events";
        }



        return "dashboard";
    }


    private PersonalData getPersonalDataForUserLoggedIn(Principal principal){

        User user = userRepository.getUserByUsername(principal.getName());

        PersonalData personalData = personalDataRepository.getPersonalDataByUser(user);

        return personalData;
    }

    private List<Event> getEventListForUserLoggedIn(Principal principal){

        User user = userRepository.getUserByUsername(principal.getName());

        List<Event> eventList = eventRepository.getEventsByUser(user);

        return eventList;
    }


    private Long calculatePeriodBetweenNextBirthDayAndNow(LocalDate birthDate){

        LocalDate present = LocalDate.now();

        int thisYear = present.getYear();

        LocalDate birthDayThisYear = birthDate.withYear(thisYear);


        int compared = birthDayThisYear.compareTo(present);

        if(compared < 0){

            birthDayThisYear = birthDate.withYear(thisYear+1);

        }

        Long period = ChronoUnit.DAYS.between(present, birthDayThisYear);

        return period;
    }

    private Long calculatePeriodBetweenEventAndNow(LocalDate eventDate){

        LocalDate present = LocalDate.now();

        int thisYear = present.getYear();

        LocalDate eventThisYear = eventDate.withYear(thisYear);


        int compared = eventThisYear.compareTo(present);

        if(compared < 0){

            eventThisYear = eventDate.withYear(thisYear+1);

        }

        Long period = ChronoUnit.DAYS.between(present, eventThisYear);

        return period;
    }

}
