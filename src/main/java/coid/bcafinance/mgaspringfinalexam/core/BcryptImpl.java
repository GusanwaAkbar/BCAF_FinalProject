package coid.bcafinance.mgaspringfinalexam.core;

import java.util.function.Function;

public class BcryptImpl {

    private static final BcryptCustom bcrypt = new BcryptCustom(11);

    public static String hash(String password) {
        return bcrypt.hash(password);
    }

    public static boolean verifyAndUpdateHash(String password,
                                              String hash,
                                              Function<String, Boolean> updateFunc) {
        return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
    }

    public static boolean verifyHash(String password , String hash)
    {
        return bcrypt.verifyHash(password,hash);
    }
    
    public static void main(String[] args) {
        String strUserName = "Agam123";
        String strPwd = "Bima321"+strUserName;
        String strAfterEncrypt1 = hash("Paul123");
        System.out.println(verifyHash("kmzway87aaA$","$11$5nKgFUE/ZE/dAIhZy2Ddpe4MBAdmfs5v.SktRYDpImMrutmeLGdAm"));
//        System.out.println(verifyHash("921795", "$11$pJYgWstrFwV7nGiR89cRPuJtnol.mX7.3QvHvIK2y5jOf9ul1/8UC"));
//        String strAfterEncrypt2 = hash(strPwd);
//        System.out.println("strAfterEncrypt1 => "+strAfterEncrypt1);
//        System.out.println("strAfterEncrypt2 => "+strAfterEncrypt2);
//        System.out.println(verifyHash("Paul123Agam123","$2a$11$mq2Q0OKSO5PnaH6uKdPT0uRxgaq/Pjn7HTTOOGRgjSx.19xjdad.y"));
    }
}