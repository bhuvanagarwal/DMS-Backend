package com.dms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    

    @NotBlank(message = "{username.not.null}")
    private String username;

    @NotBlank(message = "{api.userCreate.pass.notNull}")
    @Size(min = 8, message = "{api.userCreate.pass.length}")
    private String password;

    @NotBlank(message = "{api.userCreate.email.notNull}")
    @Email(message = "{api.userCreate.invalidEmail}")
    private String email;

    private String role;

    @NotBlank(message = "{api.userCreate.firstName.notNull}")
    private String firstName;
    private String middleName;
    private String lastName;
    @NotBlank(message = "{api.userCreate.priCountryCode.notNull}")
    private String priCountryCode;
    @NotBlank(message = "{api.userCreate.priNumber.notNull}")
    private String primaryNumber;
    private String altCountryCode;
    private String altNumber;
    @NotBlank(message = "{api.userCreate.permAddress.notNull}")
    private String permAddress;
    @NotBlank(message = "{api.userCreate.resiAddress.notNull}")
    private String resiAddress;


   

}