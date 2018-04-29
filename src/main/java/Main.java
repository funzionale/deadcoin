import com.sun.javaws.exceptions.InvalidArgumentException;

public class Main {
  public static void main(String[] args) throws CryptographicException, InvalidArgumentException {
    Network network = new Network();

    network.populate(100);
    network.interweave();
    network.generateRandomTransactions(50);
  }
}
