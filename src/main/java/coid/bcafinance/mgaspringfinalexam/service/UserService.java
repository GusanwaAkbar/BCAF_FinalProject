package coid.bcafinance.mgaspringfinalexam.service;

import coid.bcafinance.mgaspringfinalexam.configuration.OtherConfig;
import coid.bcafinance.mgaspringfinalexam.core.BcryptImpl;
import coid.bcafinance.mgaspringfinalexam.core.Crypto;
import coid.bcafinance.mgaspringfinalexam.core.IService;
import coid.bcafinance.mgaspringfinalexam.core.security.JwtUtility;
import coid.bcafinance.mgaspringfinalexam.dto.OtpVerificationRequestDTO;
import coid.bcafinance.mgaspringfinalexam.handler.RequestCapture;
import coid.bcafinance.mgaspringfinalexam.handler.ResponseHandler;
import coid.bcafinance.mgaspringfinalexam.model.User;
import coid.bcafinance.mgaspringfinalexam.repo.RoleRepository;
import coid.bcafinance.mgaspringfinalexam.repo.UserRepo;
import coid.bcafinance.mgaspringfinalexam.util.ExecuteSMTP;
import coid.bcafinance.mgaspringfinalexam.util.LoggingFile;
import io.jsonwebtoken.ExpiredJwtException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 *  Company Code - Not Necessery
 *  Modul Code 04
 *  Type of Error -> Validation = FV , Engine Error = FE
 *  ex : FE01001 (Error di Modul GroupMenu Functional Save)
 */
@Service
@Transactional
public class UserService implements IService<User>, UserDetailsService {

    private Map<String,Object> mapz = new HashMap<>();
    private StringBuilder sBuild = new StringBuilder();

    @Autowired
    private RoleRepository roleRepository; // Ensure this is added

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;




    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public ResponseEntity<Object> save(User user, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> update(Long id, User user, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> find(Pageable pageable, String columFirst, String valueFirst, HttpServletRequest request) {
        return null;
    }

    public ResponseEntity<Object> checkRegis(@RequestBody User user, HttpServletRequest request) {
    try{

        if (user == null) {
            return new ResponseHandler().generateResponse(
                    "Data tidak Valid",
                    HttpStatus.BAD_REQUEST,
                    user,
                    "FVRGS001",
                    request
            );
        }

        Optional<User> opUserResult = userRepo.findTop1ByUsernameOrNoHpOrEmail(user.getUsername(), user.getNoHp(), user.getEmail());
        if (!opUserResult.isEmpty()) {
            User existingUser = opUserResult.get();

            if (existingUser.getRegistered() != null && existingUser.getRegistered()) {
                return checkExistingUser(existingUser, user, request);
            } else {
                updateUserInfo(existingUser, user);
            }
        } else {
            createUser(user);
        }

        return sendOtp(user, request);

        } catch (Exception e) {
        logException("checkRegis", e, request);
            return new ResponseHandler().generateResponse("Registration error", HttpStatus.INTERNAL_SERVER_ERROR, null, null, request);
        }

    }

    private ResponseEntity<Object> checkExistingUser(User existingUser, User newUser, HttpServletRequest request) {
        try {
            if (existingUser.getUsername().equals(newUser.getUsername())) {
                return new ResponseHandler().generateResponse("USERNAME SUDAH TERDAFTAR", HttpStatus.NOT_ACCEPTABLE, null, "FVRGS004", request);
            }
            // This block should not be reachable if your DB constraints are set correctly; consider removing it.
            return new ResponseHandler().generateResponse("Unhandled registration case", HttpStatus.CONFLICT, null, "FVRGS005", request);
            // Rest of your existing method code...
        } catch (Exception e) {
            logException("checkExistingUser", e, request);
            return new ResponseHandler().generateResponse("Registration error", HttpStatus.INTERNAL_SERVER_ERROR, null, null, request);
        }
    }

    private void updateUserInfo(User existingUser, User newUser) {
        existingUser.setPassword(BcryptImpl.hash(newUser.getPassword() + newUser.getUsername()));
        existingUser.setRegistered(false);
        existingUser.setModifiedBy(existingUser.getIdUser());
        existingUser.setModifiedDate(new Date());
        userRepo.save(existingUser);
    }

    private void createUser(User user) {
        user.setPassword(BcryptImpl.hash(user.getPassword() + user.getUsername()));
        user.setRegistered(false);
        userRepo.save(user);
    }

    private ResponseEntity<Object> sendOtp(User user, HttpServletRequest request) {
        if (canSendOtp(Optional.ofNullable(user))) {
            int otp = new Random().nextInt(100000, 999999);
            String[] message = {"Verifikasi Email", user.getNamaLengkap(), String.valueOf(otp)};
            new Thread(() -> new ExecuteSMTP().sendSMTPToken(user.getEmail(), "TOKEN Verifikasi Email", message, "ver_regis.html")).start();
            user.setOtp(otp);
            user.setLastOtpSentTime(new Date());
            userRepo.save(user);
            return new ResponseHandler().generateResponse("OTP sent successfully", HttpStatus.OK, user, null, request);
        } else {

            return new ResponseHandler().generateResponse("Please wait before requesting another OTP", HttpStatus.TOO_MANY_REQUESTS, null, null, request);
        }
    }

    private boolean canSendOtp(Optional<User> user) {
        if (user.get().getLastOtpSentTime() != null) {
            long elapsed = new Date().getTime() - user.get().getLastOtpSentTime().getTime();
            return elapsed >= 10000; // 10 seconds cooldown
        }
        return true;
    }













    public ResponseEntity<Object> doLogin(User userz, HttpServletRequest request) {
        /**
         *  KITA TIDAK TAHU KALAU USER MEMASUKKAN EMAIL, USERNAME ATAUPUN NO HP
         *  JADI APAPUN INPUTAN USER KITA MAPPING KE 3 FIELD DI DALAM OBJECT USER
         */
        userz.setEmail(userz.getUsername());
        userz.setNoHp(userz.getUsername());
        Optional<User> opUserResult = userRepo.findTop1ByUsernameOrNoHpOrEmail(userz.getEmail(),userz.getNoHp(),userz.getEmail());//DATANYA PASTI HANYA 1
        User nextUser = null;
        try {
            if(!opUserResult.isEmpty()) {
                nextUser = opUserResult.get();
                if(!BcryptImpl.verifyHash(userz.getPassword()+nextUser.getUsername(),nextUser.getPassword()))//dicombo dengan userName
                {
                    return new ResponseHandler().generateResponse("Password salah!",
                            HttpStatus.NOT_ACCEPTABLE,null,"FV01007",request);
                } else if (!nextUser.getRegistered().equals(true)) {
                    return new ResponseHandler().generateResponse("Akun belum terverifikasi!",
                            HttpStatus.TEMPORARY_REDIRECT,null,"FV01007",request);
                }

                /**
                 * Ketiga Informasi ini kalau butuh dibuatan saja di Model User nya
                 * kalau digunakan pastikan flow nya di check lagi !!
                 */
//                nextUser.setLastLoginDate(new Date());
//                nextUser.setTokenCounter(0);//SETIAP KALI LOGIN BERHASIL , BERAPA KALIPUN UJI COBA REQUEST TOKEN YANG SEBELUMNYA GAGAL AKAN SECARA OTOMATIS DIRESET MENJADI 0
//                nextUser.setPasswordCounter(0);//SETIAP KALI LOGIN BERHASIL , BERAPA KALIPUN UJI COBA YANG SEBELUMNYA GAGAL AKAN SECARA OTOMATIS DIRESET MENJADI 0
                nextUser.setModifiedBy(nextUser.getIdUser());
                nextUser.setModifiedDate(new Date());

            } else {
                return new ResponseHandler().generateResponse("User Tidak Terdaftar",
                        HttpStatus.NOT_ACCEPTABLE,null,"FV01008",request);
            }

        } catch (Exception e) {
//            strExceptionArr[1]="doLogin(Userz userz,WebRequest request)  --- LINE 132";
//            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLoging());
            System.out.println(e);
            return new ResponseHandler().generateResponse("GAGAL DIPROSES",HttpStatus.INTERNAL_SERVER_ERROR,nextUser,"FERGS001",request);
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",authManager(nextUser,request));
        return new ResponseHandler().generateResponse("Login Berhasil",
                HttpStatus.CREATED,map,null,request);
    }






    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


    /**
     * Method untuk merangkai Claims yang kita modifikasi untuk dijadikan informasi di dalam TOKEN
     */
    //RANGE 006-010
    public String authManager(User user, HttpServletRequest request) {
        /* Untuk memasukkan user ke dalam */
        sBuild.setLength(0);
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        if(userDetails==null) {
            return "FAILED";
        }

        /* Isi apapun yang perlu diisi ke dalam object mapz !! */
        mapz.put("uid",user.getIdUser());
        mapz.put("ml",user.getEmail());//5-6-10
        mapz.put("pn",user.getNoHp());//5-6-10

        String strAppendMenu = "";
        sBuild.setLength(0);

        String token = jwtUtility.generateToken(userDetails,mapz);
        token = Crypto.performEncrypt(token);

        return token;
    }


    public ResponseEntity<Object> verifyOtp(OtpVerificationRequestDTO otpData, HttpServletRequest request) {

        Optional<User> user = userRepo.findByUsername(otpData.getUsername());

        if (user == null || user.get().getUsername() == null || user.get().getOtp() == null) {
            return new ResponseHandler().generateResponse("Invalid request", HttpStatus.BAD_REQUEST, user, "MISSING_USERNAME_OR_OTP", request);
        }

        User existingUser = userRepo.findByUsername(user.get().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (existingUser.getOtp().equals(user.get().getOtp())) {
            existingUser.setRegistered(true);
            existingUser.setOtp(null); // Clear the OTP as it's no longer needed
            userRepo.save(existingUser);
            return new ResponseHandler().generateResponse("OTP verified successfully", HttpStatus.OK, user, null, request);
        } else {
            return new ResponseHandler().generateResponse("Invalid OTP", HttpStatus.UNAUTHORIZED, user, "INVALID_OTP", request);
        }
    }

    public ResponseEntity<Object> resendOtp(String username, HttpServletRequest request) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!canSendOtp(user)) {
            return new ResponseHandler().generateResponse("Please wait before requesting another OTP", HttpStatus.TOO_MANY_REQUESTS, null, "OTP_WAIT", request);
        }

        if (sendOtp(user)) {
            return new ResponseHandler().generateResponse("OTP resent successfully", HttpStatus.OK, user, null, request);
        } else {
            return new ResponseHandler().generateResponse("Failed to send OTP", HttpStatus.INTERNAL_SERVER_ERROR, null, "OTP_FAIL", request);
        }
    }

    private boolean canSendOtp(User user) {
        if (user.getLastOtpSentTime() != null) {
            long elapsed = new Date().getTime() - user.getLastOtpSentTime().getTime();
            return elapsed >= 10000; // 10 seconds cooldown
        }
        return true;
    }

    private boolean sendOtp(User user) {
        int otp = new Random().nextInt(100000, 999999);
        user.setOtp(otp); // Save the new OTP
        user.setLastOtpSentTime(new Date()); // Update the OTP sent time
        userRepo.save(user);

        String[] message = {"Verifikasi Email", user.getNamaLengkap(), String.valueOf(otp)};
        new Thread(() -> new ExecuteSMTP().sendSMTPToken(user.getEmail(), "TOKEN Verifikasi Email", message, "ver_regis.html")).start();
        return true;
    }


    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    private void logException(String methodName, Exception e, HttpServletRequest request) {
        String[] strExceptionArr = new String[2];
        strExceptionArr[0] = "Error in " + methodName + " at " + new Date();
        strExceptionArr[1] = methodName + "(...) LINE " + new Throwable().getStackTrace()[0].getLineNumber() + " " + RequestCapture.allRequest(request);
        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
    }

}

