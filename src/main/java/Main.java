import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class Main {
  public static void main(String[] args)
          throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
    Network network = new Network();

    network.populate(100);
    network.interweave();
    network.generateRandomTransactions(10);
  }
}
