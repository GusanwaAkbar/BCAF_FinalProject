package coid.bcafinance.mgaspringfinalexam.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OtpDto {
    @NotNull(message = "OTP Tidak Boleh NULL")
    @NotBlank(message = "OTP Tidak Boleh Blank")
    @NotEmpty(message = "OTP Tidak Boleh Kosong")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

/*
Created By IntelliJ IDEA 2023.2.5 (Ultimate Edition)
@Author farha a.k.a. Farkhan Hamzah Firdaus
Java Developer
Crated on 15/03/2024 05:50
@Last Modified 15/03/2024 05:50
Version 1.0
*/
