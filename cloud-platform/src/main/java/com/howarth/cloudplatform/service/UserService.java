package com.howarth.cloudplatform.service;

import com.howarth.cloudplatform.dao.ApplicationUserDao;
import com.howarth.cloudplatform.model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class UserService implements UserDetailsService {

    private final ApplicationUserDao applicationUserDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(ApplicationUserDao applicationUserDao,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserDao = applicationUserDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ApplicationUser createApplicationUser(ApplicationUser user) {
        if (applicationUserDao.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserDao.save(user);

        user.setPassword(null);
        return user;
    }

    public List<ApplicationUser> getAllApplicationUsers() {
        return applicationUserDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserDao.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
