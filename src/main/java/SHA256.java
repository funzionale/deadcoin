import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256 {
  public static String hash(String text) throws CryptographicException {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
      return Hex.encodeHexString(hash);
    } catch (Exception e) {
      throw new CryptographicException();
    }
  }
}
