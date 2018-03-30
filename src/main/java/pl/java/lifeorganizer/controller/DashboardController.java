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

        User user = userRepository.getUserByUsername(principal.getName());
        PersonalData personalData = personalDataRepository.getPersonalDataByUser(user);

        LocalDate birthDate = personalData.getBirthDate();

        model.addAttribute("birthDate", birthDate);

        LocalDate present = LocalDate.now();



        int thisYear = LocalDate.now().getYear();

        LocalDate birthDayThisYear = birthDate.withYear(thisYear);

        int compared = birthDayThisYear.compareTo(present);

        if(compared < 0){

            birthDayThisYear = birthDate.withYear(thisYear+1);

        }

        Long period = ChronoUnit.DAYS.between(present, birthDayThisYear);

        model.addAttribute("period", period);


        return "dashboard";
    }

}
