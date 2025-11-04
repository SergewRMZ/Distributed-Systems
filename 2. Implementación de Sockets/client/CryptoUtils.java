package client;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class CryptoUtils {
  public static PublicKey loadPublicKey(String filename) throws Exception {
    String base64Key = new String(Files.readAllBytes(Paths.get(filename)));
    byte[] encoded = Base64.getDecoder().decode(base64Key.trim());
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    return keyFactory.generatePublic(keySpec);
  }

  public static byte[] encryptKey(byte[] key, PublicKey publicKeyServer) throws Exception {
    Cipher cipher = javax.crypto.Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKeyServer);
    byte[] encryptedBytes = cipher.doFinal(key);
    return encryptedBytes;
  }

  public static byte[] aesGCMEncryption(byte[] data, SecretKey aesKey, byte[] iv) throws Exception {
    Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(128, iv);
    aes.init(Cipher.ENCRYPT_MODE, aesKey, spec);
    return aes.doFinal(data);
  }

  public static SecretKey generateAESKey() throws Exception {
   KeyGenerator keyGen = KeyGenerator.getInstance("AES");
   keyGen.init(128);
   SecretKey aesKey = keyGen.generateKey(); 
   return aesKey;
  }

  public static byte[] generateIV() {
    byte[] iv = new byte[12];
    SecureRandom random = new SecureRandom();
    random.nextBytes(iv);
    return iv;
  }
}
