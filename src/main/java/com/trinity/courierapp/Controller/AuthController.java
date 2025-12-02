package com.trinity.courierapp.Controller;


import com.trinity.courierapp.DTO.AuthRequestDto;
import com.trinity.courierapp.DTO.ClientRegistrationRequestDto;
import com.trinity.courierapp.DTO.CourierRegistrationRequestDto;
import com.trinity.courierapp.Entity.Courier;
import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Repository.UserRepository;
import com.trinity.courierapp.Repository.CourierRepository;
import com.trinity.courierapp.Security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/login")
    public String authenticateUser(@Valid @RequestBody AuthRequestDto authRequestDto, BindingResult bindingResult) {
        Authentication authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        authRequestDto.getEmail(),
                        authRequestDto.getPassword()
                )
        );

//        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(Integer.valueOf(userDetails.getUsername()));
    }

    @PostMapping("/client_signup")
    public String registerClient(@Valid @RequestBody ClientRegistrationRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())){
            return "Email Already Exists";
        }
        final User newUser = new User(dto.getFullName(), dto.getPhoneNumber(), dto.getPassword(), dto.getEmail(), dto.getUserType());
        userRepository.save(newUser);
        return "Registration Successful";
    }


    @PostMapping("/courier_signup")
    public String registerCourier(@Valid @RequestBody CourierRegistrationRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())){
            return "Email Already Exists";
        }
        final User newUser = new User(dto.getFullName(), dto.getPhoneNumber(), dto.getPassword(), dto.getEmail(), dto.getUserType());
        userRepository.save(newUser);
        final Courier courier = new Courier(dto.getCourierGps(),dto.getVehicleType(), dto.getCourierStatus(), dto.getVehicleNumber());
        courier.setCourierUser(newUser);
        courierRepository.save(courier);
        return "Registration Successful";
    }
}





