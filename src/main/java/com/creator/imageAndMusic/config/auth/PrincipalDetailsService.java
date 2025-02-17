package com.creator.imageAndMusic.config.auth;


import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[PrincipalDetailsService] loadUserByUsername() username :" + username);
        Optional<User> userOptional = userRepository.findById(username);
        if(userOptional.isEmpty())
            return null;

        //Entity -> Dto
        UserDto dto = new UserDto();
        dto.setUsername(userOptional.get().getUsername());
        dto.setPassword(userOptional.get().getPassword());
        dto.setRole(userOptional.get().getRole());
        dto.setNickname(userOptional.get().getNickname());
        dto.setPhone(userOptional.get().getPhone());
        dto.setProvider(userOptional.get().getProvider());
        dto.setZipcode(userOptional.get().getZipcode());
        dto.setAddr1(userOptional.get().getAddr1());
        dto.setAddr2(userOptional.get().getAddr2());
        dto.setBankname(userOptional.get().getBankname());
        dto.setAccount(userOptional.get().getAccount());
        dto.setProfileImage(userOptional.get().getProfileImage());

        //

        return new PrincipalDetails(dto);
    }

}
