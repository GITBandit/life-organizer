package pl.java.lifeorganizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.java.lifeorganizer.model.PersonalData;
import pl.java.lifeorganizer.model.PersonalDataRepository;
import pl.java.lifeorganizer.model.User;
import pl.java.lifeorganizer.model.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class DashboardController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonalDataRepository personalDataRepository;

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

        return "dashboard";
    }


    private PersonalData getPersonalDataForUserLoggedIn(Principal principal){

        User user = userRepository.getUserByUsername(principal.getName());

        PersonalData personalData = personalDataRepository.getPersonalDataByUser(user);

        return personalData;
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

}
