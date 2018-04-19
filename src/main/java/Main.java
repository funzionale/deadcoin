public class Main {
  public static void main(String[] args) throws Exception {
    Network network = new Network();
    network.populate(100);
    network.generateRandomTransactions(10);
  }
}
