package pl.java.lifeorganizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.java.lifeorganizer.model.*;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    @GetMapping("/register")
    public String addUser(Model model){

        User newUser = new User();

        model.addAttribute("user", newUser);

        return "user_add";
    }

    @PostMapping("/register")
    @ResponseBody
    public String confirmNewUser(User newUser){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(userRepository.getUserByUsername(newUser.getUsername()) != null){
            return "<script>alert(\"Taki użytkownik już istnieje! \\n \\n Podaj inną nazwę użytkownika\"); window.location = \"/register\"</script>";
        }

        String hashedPassword = passwordEncoder.encode(newUser.getPassword());

        newUser.setPassword(hashedPassword);
        newUser.setEnabled(true);

        System.out.println(newUser); // to be removed

        userRepository.save(newUser);

        UserRole newUserRole = new UserRole();

        newUserRole.setUsername(newUser.getUsername());
        newUserRole.setRole("ROLE_USER");

        userRoleRepository.save(newUserRole);

        return "<script>alert(\"Użytkownik został dodany\"); window.location = \"/\"</script>";
    }

    @GetMapping("/profile")
    public String editProfile(Model model, Principal principal){

        User userToEdit = userRepository.getUserByUsername(principal.getName());

        model.addAttribute("user", userToEdit);

        return "user_profile";
    }

    @PostMapping("/profile")
    @ResponseBody
    public String confirmChanges(User editedUser){

        User user = userRepository.getOne(editedUser.getId());

        String oldUsername = user.getUsername();

        boolean usernameUpdated = !oldUsername.equals(editedUser.getUsername());


        if (usernameUpdated){

            if(userRepository.getUserByUsername(editedUser.getUsername()) != null){
                return "<script>alert(\"Taki użytkownik już istnieje! \\n \\n Podaj inną nazwę użytkownika\"); window.location = \"/profile\"</script>";
            }

            UserRole userRole = userRoleRepository.getUserRoleByUsername(oldUsername);
            userRole.setUsername(editedUser.getUsername());

            userRoleRepository.save(userRole);
        }

        user.setUsername(editedUser.getUsername());
        user.setFirstName(editedUser.getFirstName());
        user.setLastName(editedUser.getLastName());

        userRepository.save(user);

        if(usernameUpdated){
            SecurityContextHolder.clearContext();

            return "<script>alert(\"Profil zaktualizowany \\n \\n Zaloguj się za pomocą nowej nazwy użytkownika\"); window.location = \"/profile\"</script>";
        }

        return "<script>alert(\"Dane użytkownika zostały zaktualizowane\"); window.location = \"/\"</script>";
    }

    @GetMapping("/password")
    public String changePassword(Model model){

        Password password = new Password();

        model.addAttribute("password", password);

        return "password_change";
    }

    @PostMapping("/password")
    @ResponseBody
    public String confirmChangedPassword(Password password,
                                         Principal principal){

        if(password.getPassword1().equals(password.getPassword2()) && password != null && !password.getPassword1().equals("")){

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            User user = userRepository.getUserByUsername(principal.getName());

            String newPassword = passwordEncoder.encode(password.getPassword1());

            user.setPassword(newPassword);

            userRepository.save(user);

            SecurityContextHolder.clearContext();

                return "<script>alert(\"Hasło zmienione \\n \\n Zaloguj się za pomocą nowego hasła\"); window.location = \"/profile\"</script>";
        }

        return "<script>alert(\"Hasła nie są takie same / nie podano hasła \\n \\n Spróbój ponownie\"); window.location = \"/password\"</script>";

    }
}
