package pl.java.lifeorganizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.java.lifeorganizer.model.*;

import javax.persistence.Persistence;
import java.security.Principal;

@Controller
public class PersonalDataController {

    @Autowired
    PersonalDataRepository personalDataRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/personal")
    public String editPersonalData(Model model, Principal principal){

        User user = userRepository.getUserByUsername(principal.getName());

        PersonalData personalDataFromDB = personalDataRepository.getPersonalDataByUser(user);

        if(personalDataFromDB != null){

        model.addAttribute("personalData", personalDataFromDB);

        } else {

        PersonalData personalData = new PersonalData();

        personalData.setUser(user);

        model.addAttribute("personalData", personalData);

        }

        return "personal_data";
    }

    @PostMapping("/personal")
    @ResponseBody
    public String confirmPersonalDataChanges(PersonalData newPersonalData, Principal principal){

        if(newPersonalData.getId() != null){

            PersonalData personalData = personalDataRepository.getOne(newPersonalData.getId());

            personalData.setBirthDate(newPersonalData.getBirthDate());

            personalDataRepository.save(personalData);

        } else {

            personalDataRepository.save(newPersonalData);

            User user = userRepository.getUserByUsername(principal.getName());

            user.setPersonalData(newPersonalData);

            userRepository.save(user);


        }


        return "<script>alert(\"Nowe dane zostały zapisane\"); window.location = \"/profile\"</script>";
    }
}
