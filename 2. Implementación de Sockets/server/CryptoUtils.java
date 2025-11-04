package server;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class CryptoUtils {
  public static PrivateKey loadPrivateKey(String filename) throws Exception{
    String base64Key = new String(Files.readAllBytes(Paths.get(filename)));
    byte[] encoded = Base64.getDecoder().decode(base64Key.trim());
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    return keyFactory.generatePrivate(keySpec);
  }

  public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedBytes = cipher.doFinal(encryptedData);
    return decryptedBytes;
  }

  public static byte[] aesGCMDecryption(byte[] encryptedData, SecretKey aesKey, byte[] iv) throws Exception {
    try {
      Cipher aes = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(128, iv);
      aes.init(Cipher.DECRYPT_MODE, aesKey, spec);
      return aes.doFinal(encryptedData);
    } catch (AEADBadTagException e) {
      System.err.println("El mensaje fue alterado o la clave no coincide");
      return null;
    }
  }
}
