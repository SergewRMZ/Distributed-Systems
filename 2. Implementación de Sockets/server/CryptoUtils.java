package server;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class CryptoUtils {
  public static PrivateKey loadPrivateKey(String filename) throws Exception{
    String base64Key = new String(Files.readAllBytes(Paths.get(filename)));
    byte[] encoded = Base64.getDecoder().decode(base64Key.trim());
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    return keyFactory.generatePrivate(keySpec);
  }

  public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedBytes = cipher.doFinal(encryptedData);
    return decryptedBytes;
  }
}
