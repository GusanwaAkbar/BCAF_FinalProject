package coid.bcafinance.mgaspringfinalexam.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {
    private static final long serialVersionUID = 234234523523L;
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;//untuk t menit 5 * 60 * 1000
    @Value("${jwt.secret}")
    private String secretKey;
    private String strArrAuth [] = null;

    /**
     * KONFIGURASI UNTUK CUSTOMISASI DIMULAI DISINI
     */

    /**
     * Ini untuk JWT filtering saja tanpa memperdulikan otorisasi user nya
     * saat ini dinonaktifkan saja karena tidak digunakan
     */
//    public Map<String,Object> getApiAuthorization(String token,
//                                                  Map<String,Object> mapz){
//        Claims claims = getAllClaimsFromToken(token);
//        mapz.put("uid",claims.get("uid"));
//        mapz.put("un",claims.getSubject());//untuk subject / username sudah ada di claims token JWT
//        mapz.put("ml",claims.get("mail"));
//        mapz.put("ph",claims.get("phone"));
//        mapz.put("mn",claims.get("menu"));
//        mapz.put("isValid",true);
//        return mapz;
//    }

    /**
     * Ini untuk JWT filter dan otorisasi based on Menu saja
     * Function disini hanya menerima token JWT yang sudah di decrypt
     */
    public Map<String,Object> getApiAuthorization(String token,
                                                  Map<String,Object> mapz,
                                                  String modulCode){
        /** claims adalah data payload yang ada di token */
        Claims claims = getAllClaimsFromToken(token);

        /** value dari key isValid menentukan user tersebut berhak atau tidaknya mengakses menu itu
         * default nya adalah false
         */
        mapz.put("isValid",false);

        /** ln adalah informasi otorisasi dari user tersebut
         Format nya ada di class User Service method authManager
         Logic ini adalah pengecekan apakah user tersebut berhak atau tidak mengakses menu tersebut
         true = berhak, false = tidak berhak
         modulCode adalah kode dari modul yang sudah di set final di masing-masing services
         */
        if(claims.get("ln")!=null){
            strArrAuth = claims.get("ln").toString().split("-");
            int intArr = strArrAuth.length;
            for (int i=0;i<intArr;i++)
            {
                if(modulCode.equals(strArrAuth[i]))
                {
                    mapz.put("isValid",true);
                    break;
                }
            }
        }
        mapz.put("uid",claims.get("uid"));
        mapz.put("un",claims.getSubject());//untuk subject / username sudah ada di claims token JWT
        mapz.put("ml",claims.get("ml"));
        mapz.put("pn",claims.get("pn"));

        return mapz;
    }

    /**
     * Ini untuk JWT filter dan otorisasi per API di dalam class
     * saat ini dinonaktifkan saja karena tidak digunakan !!
     */
//    public Map<String,Object> getApiAuthorization(String token,String modul,
//                                                  Integer intPrivilage,
//                                                  Map<String,Object> mapz){
//        mapz.put("isValid",false);//inisialisasi map untuk key isValid adalah false, akan true jika sudah melewati kondisi dibawah
//        Claims claims = getAllClaimsFromToken(token);
//        String strModul = claims.get(modul)==null?"":claims.get(modul).toString();
//        if(strModul!=null && !strModul.equals(""))
//        {
//            String strArrPrivilage = strModul.split("-")[intPrivilage].toString();
//            if(strArrPrivilage.equals("1"))
//            {
//                /*
//                    Transfer data dari JWT ke Object Java Map
//                 */
//                mapz.put("userId",claims.get("uid"));
//                mapz.put("userName",claims.getSubject());//untuk subject / username sudah ada di claims token JWT
//                mapz.put("email",claims.get("mail"));
//                mapz.put("noHp",claims.get("phone"));
//                mapz.put("isValid",true);
//            }
//        }
//        return mapz;
//    }

    /**
     * KONFIGURASI CUSTOMISASI BERAKHIR DISINI
     * KONFIGURASI UNTUK JWT DIMULAI DARI SINI
     */

//    username dari token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    //parameter token habis waktu nya
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //kita dapat mengambil informasi dari token dengan menggunakan secret key
    //disini juga validasi dari expired token dan lihat signature  dilakukan
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token untuk user
    public String generateToken(UserDetails userDetails,Map<String,Object> claims) {
        claims = (claims==null)?new HashMap<String,Object>():claims;
        return doGenerateToken(claims, userDetails.getUsername());
    }
    /** proses yang dilakukan saat membuat token adalah :
     mendefinisikan claim token seperti penerbit (Issuer) , waktu expired , subject dan ID
     generate signature dengan menggunakan secret key dan algoritma HS512 (HMAC - SHA),
     */

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public Boolean validateToken(String token) {
        /** Sudah otomatis tervalidaasi jika expired date masih aktif */
        String username = getUsernameFromToken(token);
        return (username!=null && !isTokenExpired(token));
    }
    /**
     * KONFIGURASI UNTUK JWT BERAKHIR DI SINI
     */
}