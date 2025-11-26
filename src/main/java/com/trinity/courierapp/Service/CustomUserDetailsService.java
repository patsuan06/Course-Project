package com.trinity.courierapp.Service;


import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + email);
        }
        return  new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
//              for authorization
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType()))
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()));
    }

}
