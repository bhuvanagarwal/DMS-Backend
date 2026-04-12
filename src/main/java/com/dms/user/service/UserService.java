package com.dms.user.service;

import com.dms.util.MessageHelper;

import jakarta.validation.Valid;

import java.time.Instant;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.dto.request.LoginRequest;
import com.dms.dto.request.UserRequest;
import com.dms.dto.response.ApiResponse;
import com.dms.dto.response.UserResponse;
import com.dms.user.entity.User;
import com.dms.user.entity.UserProfile;
import com.dms.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MessageHelper messageHelper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public ApiResponse<UserResponse> createUser(UserRequest request) {

    if(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(request.getUsername(), request.getEmail()).isPresent()){

                return new ApiResponse<>(messageHelper.getMessage("user.email.already.exists", null, LocaleContextHolder.getLocale()), null);
    }


    User user = new User();
    UserProfile userProfile =  new UserProfile();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setEmail(request.getEmail());
    user.setRole(request.getRole());
    user.setStatus("Active");
    user.setCreatedAt(Instant.now());

    userProfile.setFirstName(request.getFirstName());
    userProfile.setMiddleName(request.getMiddleName());
    userProfile.setLastName(request.getLastName());
    userProfile.setPriCountryCode(request.getPriCountryCode());
    userProfile.setPrimaryNumber(request.getPrimaryNumber());
    userProfile.setAltCountryCode(request.getAltCountryCode());
    userProfile.setAltNumber(request.getAltNumber());
    userProfile.setEmail(request.getEmail());
    userProfile.setPermAddress(request.getPermAddress());
    userProfile.setResiAddress(request.getResiAddress());
    userProfile.setUser(user);

    User savedUser = userRepository.save(user);

    user.setUserProfile(userProfile);

    UserResponse userResponse = new UserResponse(
        savedUser.getUsername(),
        savedUser.getEmail(),
        savedUser.getStatus(),
        savedUser.getCreatedAt()

    );

    return new ApiResponse<UserResponse>(messageHelper.getMessage("user.created", null, LocaleContextHolder.getLocale()), userResponse);
}



    public ApiResponse<UserResponse> loginUser(LoginRequest loginRequest) {

        if(userRepository.findByUsernameIgnoreCase(loginRequest.getUsername()).isPresent()){
            User user = userRepository.getUserObjectByUsernameIgnoreCase(loginRequest.getUsername());
            boolean isPassCorrect = passwordEncoder.matches(loginRequest.getPassword(),user.getPassword());
            
            if(!isPassCorrect){
                

                return new ApiResponse<>(messageHelper.getMessage("user.login.password.incorrect", null, LocaleContextHolder.getLocale()), null);
    
            }
            else{
                user.setLastLoginTimestamp(Instant.now());
               userRepository.save(user);
                return new ApiResponse<>(messageHelper.getMessage("user.login.password.correct", null, LocaleContextHolder.getLocale()), null);
    
            }
        }
        else{
            return new ApiResponse<>(messageHelper.getMessage("user.login.invalid.username", null, LocaleContextHolder.getLocale()), null);
    
        }
        
        // throw new UnsupportedOperationException("Unimplemented method 'loginUser'");
    }
}
