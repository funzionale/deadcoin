import java.util.ArrayList;
import java.util.Random;

public class Network {
  private Blockchain ledger;
  private ArrayList<User> users;

  Network() {
    this.ledger = new Blockchain();
    this.users = new ArrayList<>();
  }

  void populate(int usersCount) {
    for (int i = 0; i < usersCount; i++) {
      this.users.add(new User(ledger));
    }

    Random random = new Random();

    for (int i = 0; i < this.users.size(); i++) {
      User user = this.users.get(i);

      for (int j = i; j < random.nextInt(this.users.size() / 10) + i && j < users.size(); j++) {
        User peer = this.users.get(j);

        user.addPeer(peer);
        peer.addPeer(user);
      }
    }
  }

  void generateRandomTransactions(int period) throws Exception {
    Random random = new Random();
    long i = 0;
    while (i < 1000) {
      User randomSender = this.users.get(random.nextInt(this.users.size()));
      Transaction transaction = randomSender.createTransaction("Hello World");

      randomSender.notifyPeers(transaction);

      System.out.println(i++);
    }
  }
}
