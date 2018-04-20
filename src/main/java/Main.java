import java.security.NoSuchAlgorithmException;

public class Main {
  public static void main(String[] args) throws NoSuchAlgorithmException {
    Network network = new Network();

    network.populate(100);
    network.interweave();
    network.generateRandomTransactions(10);
  }
}
