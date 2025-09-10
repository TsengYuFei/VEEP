package com.example.api.Service;

import com.example.api.Entity.User;
import com.example.api.Entity.UserRole;
import com.example.api.Exception.NotFoundException;
import com.example.api.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private final UserHelperService userHelperService;
    private final UserRoleService userRoleService;



    // 回傳Spring Security認得的格式
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userHelperService.getUserByAccount(account);

        UserRole userRole = userRoleService.getUserRoleByAccount(account);
        String roleName = userRole.getRole().getName();

        return org.springframework.security.core.userdetails.User
                .withUsername(account)
                .password(user.getPassword())
                .authorities("ROLE_"+roleName)
                .build();
    }
}
