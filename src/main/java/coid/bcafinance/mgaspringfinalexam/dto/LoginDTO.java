package coid.bcafinance.mgaspringfinalexam.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginDTO {
    @NotNull(message = "Username Tidak Boleh NULL")
    @NotBlank(message = "Username Tidak Boleh Blank")
    @NotEmpty(message = "Username Tidak Boleh Kosong")
    private String username;

    /*
        UNTUK PROSES LOGIN
        VALIDASI PASSWORD NYA PASSWORD KOSONG SAJA
        UNTUK PASSWORD FORMAT NYA TIDAK PERLU, KARENA ITU HANYA ADA DI FORM REGISTRASI
     */
    @NotNull(message = "Password Tidak Boleh NULL")
    @NotBlank(message = "Password Tidak Boleh Blank")
    @NotEmpty(message = "Password Tidak Boleh Kosong")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

/*
Created By IntelliJ IDEA 2023.2.5 (Ultimate Edition)
@Author farha a.k.a. Farkhan Hamzah Firdaus
Java Developer
Crated on 12/03/2024 14:25
@Last Modified 12/03/2024 14:25
Version 1.0
*/
