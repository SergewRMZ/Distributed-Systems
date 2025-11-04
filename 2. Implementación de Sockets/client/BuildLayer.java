package client;

import java.nio.ByteBuffer;
import java.security.PublicKey;
import javax.crypto.SecretKey;

public class BuildLayer {
  private static byte[] buildLayer (byte[] data, PublicKey mixPublicKey) throws Exception {
    byte[] iv = CryptoUtils.generateIV();
    SecretKey aesKey = CryptoUtils.generateAESKey();
    byte[] encryptedData = CryptoUtils.aesGCMEncryption(data, aesKey, iv);
    byte[] encryptedKey = CryptoUtils.encryptKey(aesKey.getEncoded(), mixPublicKey);
    ByteBuffer buf = ByteBuffer.allocate(4 + encryptedKey.length + iv.length + encryptedData.length);
    buf.putInt(encryptedKey.length);
    buf.put(encryptedKey);
    buf.put(iv);
    buf.put(encryptedData);
    return buf.array();
  }

  public static byte[] onionEncrypt(byte[] message, PublicKey[] publicKeys) throws Exception {
    byte[] layer = message;
    for (int i = publicKeys.length - 1; i >= 0; i--) {
      layer = buildLayer(layer, publicKeys[i]);
    }
    return layer;
  }
}
