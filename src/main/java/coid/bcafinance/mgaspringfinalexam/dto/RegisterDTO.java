package coid.bcafinance.mgaspringfinalexam.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


public class RegisterDTO {
    @NotNull(message = "Email Tidak Boleh NULL")
    @NotBlank(message = "Email Tidak Boleh Blank")
    @NotEmpty(message = "Email Tidak Boleh Kosong")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Format email tidak valid")
    private String email;

    @NotNull(message = "No Telp Tidak Boleh NULL")
    @NotBlank(message = "No Telp Tidak Boleh Blank")
    @NotEmpty(message = "No Telp Tidak Boleh Kosong")
    @Pattern(regexp = "^(0|62|\\+62)\\d{9,15}$", message = "Nomor telepon harus dimulai dengan 0, 62, atau +62, dan terdiri dari 12-18 digit tanpa spesial karakter")
    private String noHp;

    @NotNull(message = "Username Tidak Boleh NULL")
    @NotBlank(message = "Username Tidak Boleh Blank")
    @NotEmpty(message = "Username Tidak Boleh Kosong")
    @Pattern(regexp = "^[a-z]{7,15}$", message = "Username harus terdiri dari 7-15 huruf kecil saja")
    private String username;

    @NotNull(message = "Password Tidak Boleh NULL")
    @NotBlank(message = "Password Tidak Boleh Blank")
    @NotEmpty(message = "Password Tidak Boleh Kosong")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#\\-$])(?!.*?[^A-Za-z0-9_#\\-$]).{8,}$", message = "Password harus memiliki minimal satu huruf besar, huruf kecil, angka, dan hanya satu digit spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\")")
    private String password;

    @NotNull(message = "Nama Tidak Boleh NULL")
    @NotBlank(message = "Nama Tidak Boleh Blank")
    @NotEmpty(message = "Nama Tidak Boleh Kosong")
    @Pattern(regexp = "^[a-z ]{6,15}$", message = "Nama harus terdiri dari 6-15 huruf kecil dan spasi saja")
    private String namaLengkap;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    private LocalDate tanggalLahir;

    @NotNull(message = "Alamat Tidak Boleh NULL")
    @NotBlank(message = "Alamat Tidak Boleh Blank")
    @NotEmpty(message = "Alamat Tidak Boleh Kosong")
    @Pattern(regexp = "^[a-zA-Z0-9 ]{30,255}$", message = "Alamat harus terdiri dari 30-255 karakter alfanumerik dan spasi saja")
    private String alamat;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

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

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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
