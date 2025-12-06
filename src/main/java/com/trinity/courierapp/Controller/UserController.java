package com.trinity.courierapp.Controller;


import com.trinity.courierapp.DTO.UserInfoDto;
import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/get_info")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        return new UserInfoDto(user);
    }

}
