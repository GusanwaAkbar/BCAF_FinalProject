package coid.bcafinance.mgaspringfinalexam.controller;

import coid.bcafinance.mgaspringfinalexam.dto.LoginDTO;
import coid.bcafinance.mgaspringfinalexam.dto.OtpDto;
import coid.bcafinance.mgaspringfinalexam.dto.OtpVerificationRequestDTO;
import coid.bcafinance.mgaspringfinalexam.dto.RegisterDTO;
import coid.bcafinance.mgaspringfinalexam.model.User;
import coid.bcafinance.mgaspringfinalexam.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth0")
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    BindingResult bindingResult;


    @RequestMapping("/v1/regis")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> doRegis(@RequestBody RegisterDTO regisDTO, HttpServletRequest request) {
        User user = modelMapper.map(regisDTO, new TypeToken<User>() {}.getType());

        return userService.checkRegis(user,request);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login/v1")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        User user = modelMapper.map(loginDTO,new TypeToken<User>(){}.getType());
        return userService.doLogin(user,request);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/otp/v1")
    public ResponseEntity<Object> otp(@RequestBody OtpVerificationRequestDTO otpData, HttpServletRequest request) {

        return userService.verifyOtp(otpData, request);
    }

    @CrossOrigin(origins = "http://localhost:4200")@PostMapping("/otp/resend")
    public ResponseEntity<Object> resendOtp(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        return userService.resendOtp(username, request);
    }



//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        Map<String, Object> errors = new HashMap<>();
//        String errorMessage = "";
//
//        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
////            if ("NotNull".equals(fieldError.getCode())) {
////                errorMessage = fieldError.getDefaultMessage();
////                break;
////            } else if ("NotBlank".equals(fieldError.getCode()) && errorMessage.isBlank()) {
////                errorMessage = fieldError.getDefaultMessage();
////                break;
////            } else if ("NotNull".equals(fieldError.getCode()) && errorMessage.isEmpty()){
////                errorMessage = fieldError.getDefaultMessage();
////            }
//
//            if ("NotBlank".equals(fieldError.getCode()) && errorMessage.isBlank()) {
//                errorMessage = fieldError.getDefaultMessage();
//            } else if ("NotNull".equals(fieldError.getCode()) && errorMessage.isEmpty()) {
//                errorMessage = fieldError.getDefaultMessage();
//            } else if ("NotNull".equals(fieldError.getCode())){
//                errorMessage = fieldError.getDefaultMessage();
//            }
//        }
//
//        if (errorMessage.isEmpty()) {
//            errorMessage = "Validation failed";
//        }
//
//        errors.put("message", errorMessage);
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
}

/*
Created By IntelliJ IDEA 2023.2.5 (Ultimate Edition)
@Author farha a.k.a. Farkhan Hamzah Firdaus
Java Developer
Crated on 12/03/2024 14:28
@Last Modified 12/03/2024 14:28
Version 1.0
*/
