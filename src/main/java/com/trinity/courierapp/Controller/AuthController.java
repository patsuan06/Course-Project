package com.trinity.courierapp.Controller;


import com.trinity.courierapp.DTO.AuthRequestDto;
import com.trinity.courierapp.DTO.RegistrationRequestDto;
import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Repository.UserRepository;
import com.trinity.courierapp.Repository.CourierRepository;
import com.trinity.courierapp.Security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourierRepository courierRepository;

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

    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody RegistrationRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())){
            return "Email Already Exists";
        }
         final User newUser = new User(dto.getFullName(), dto.getPhoneNumber(), dto.getPassword(), dto.getEmail(), dto.getUserType());
        userRepository.save(newUser);
        return jwtUtils.generateToken(newUser.getId());
     }
    }
