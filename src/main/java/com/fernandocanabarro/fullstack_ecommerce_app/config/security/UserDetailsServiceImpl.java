package com.fernandocanabarro.fullstack_ecommerce_app.config.security;

import java.util.List;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Role;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.projections.UserDetailsProjection;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> list = userRepository.searchUserAndRolesByEmail(username);
        if (list.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!list.get(0).getActivated()) {
            throw new DisabledException("User account not activated");
        }
        User user = new User();
        user.setEmail(list.get(0).getUsername());
        user.setPassword(list.get(0).getPassword());
        list.forEach(userDetail -> user.addRole(new Role(userDetail.getRoleId(),userDetail.getAuthority())));
        return user;
    }

}
