import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

public class DSA {
  public static KeyPair buildKeyPair() throws CryptographicException {
    try {
      KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
      keyGenerator.initialize(1024);
      return keyGenerator.genKeyPair();
    } catch(Exception e) {
      throw new CryptographicException();
    }
  }

  public static byte[] sign(DSAPrivateKey privateKey, byte[] message) throws CryptographicException {
    try {
      Signature signAlgorithm = Signature.getInstance("DSA");

      signAlgorithm.initSign(privateKey);
      signAlgorithm.update(message);

      return signAlgorithm.sign();
    } catch (Exception e) {
      throw new CryptographicException();
    }
  }

  public static boolean verify(
    DSAPublicKey publicKey,
    byte[] message,
    byte[] signature
  ) throws CryptographicException {
    try {
      Signature verifyAlgorithm = Signature.getInstance("DSA");

      verifyAlgorithm.initVerify(publicKey);
      verifyAlgorithm.update(message);

      return verifyAlgorithm.verify(signature);
    } catch (Exception e) {
      throw new CryptographicException();
    }
  }
}
