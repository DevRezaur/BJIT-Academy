package com.devrezaur.security.service;

import com.devrezaur.model.User;
import com.devrezaur.security.models.UserDetailsImplementation;
import com.devrezaur.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username.toLowerCase());

        if (user == null)
            throw new UsernameNotFoundException("User Not Found");

        return UserDetailsImplementation.build(user);
    }
}
