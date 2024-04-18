package coid.bcafinance.mgaspringfinalexam.core;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

public class Crypto {

//    private static final String defaultKey = "aafd12f438cae52538b479e3199ddec2f06cb58faafd12f6";
//    private static final String defaultKey = "7117b5ce75785cf379a972232a6b4562f2769ca2fc98d5c3e2508d792955fc10";
    private static final String defaultKey = "1e3c7d624fe822a421f208968c83a1fe9992102ff13ddcf02dd43e09ce16d29c";
    public static String performEncrypt(String keyText, String plainText) {
        try{
            byte[] key = Hex.decode(keyText.getBytes());
            byte[] ptBytes = plainText.getBytes();
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESLightEngine()));
            cipher.init(true, new KeyParameter(key));
            byte[] rv = new byte[cipher.getOutputSize(ptBytes.length)];
            int oLen = cipher.processBytes(ptBytes, 0, ptBytes.length, rv, 0);
            cipher.doFinal(rv, oLen);
            return new String(Hex.encode(rv));
        } catch(Exception e) {
            return "Error";
        }
    }

    public static String performEncrypt(String cryptoText) {
        return performEncrypt(defaultKey, cryptoText);
    }

    public static String performDecrypt(String keyText, String cryptoText) {
        try {
            byte[] key = Hex.decode(keyText.getBytes());
            byte[] cipherText = Hex.decode(cryptoText.getBytes());
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESLightEngine()));
            cipher.init(false, new KeyParameter(key));
            byte[] rv = new byte[cipher.getOutputSize(cipherText.length)];
            int oLen = cipher.processBytes(cipherText, 0, cipherText.length, rv, 0);
            cipher.doFinal(rv, oLen);
            return new String(rv).trim();
        } catch(Exception e) {
            return "Error";
        }
    }

    public static String performDecrypt(String cryptoText) {
        return performDecrypt(defaultKey, cryptoText);
    }



    public static void main(String[] args) {

        String strToEncrypt = ".,zzzzzA123";//put text to encrypt in here
        String encryptionResult = new Crypto().performEncrypt(strToEncrypt);
        System.out.println("Encryption Result : "+encryptionResult);
        System.out.println("hello");
        // KEY -> aafd12f438cae52538b479e3199ddec2f06cb58faafd12f6
//        System.out.println("KEY PENTING BANGET !!! "+defaultKey);;
        //ENCRYPT -> 528b01943544a1dcef7a692a0628e46b ->

        //ENCRYPT -> bdcc9507be280e3e5489a5dce01b42ea
        //KEY -> aafd12f438cae52538b479e2089ddec2f06cb58faafd12f6

        String strToDecrypt = "5eb279dd4ab4b51e4c8a70d8728ddb20437eaaa1dd2321d98de012117e1a45bb4f0c7ff312a67582dd46fa157f857cd96a9cc45f2959db75ae857780fc697a0a028fc555029d1a450f98cf900aacb0a0d79deeaf1a61adc09e713e16574fee3978fe1cea0eeb5fede06fd0935943df9cb16a96a51f094aaef66642323d33ae51566390acd40ee5e7a43dd2ae68bc6b885fcbfc475cd944f40f7f7973f7cfa99e1d40a6238460bf8c9f412b2e7494191502d96772b996d53ebafbac7449d26f9f5023425e163f3b5b6a90511f823686e5089966ed19a967d23317d26eed8a32e21d7080ef256e285d98345af138ef487c47dc47985aeaf3fbca882b5b0b3faacb";//put text to decrypt in here
        String decriptionResult = new Crypto().performDecrypt(strToDecrypt);
        System.out.println("Decryption Result : "+decriptionResult);
        System.out.println("Untuk VIVO X5 DEFAULT AJA BELUM DI SET ".length());
        //585107f50fa1e0649bd32da95d5cf41c
        //585107f50fa1e0649bd32da95d5cf41c
        //585107f50fa1e0649bd32da95d5cf41c
    }
}