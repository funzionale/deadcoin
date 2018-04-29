import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.ArrayList;

public class Network {
  Blockchain ledger;
  ArrayList<User> users;

  Network() throws CryptographicException {
    this.ledger = new Blockchain();
    this.users = new ArrayList<>();
  }

  void populate(int usersCount) throws CryptographicException, InvalidArgumentException {
    if (usersCount < 10) {
      throw new InvalidArgumentException(new String[]{"Users must be greater than 10"});
    }

    while (usersCount-- > 0) {
      this.users.add(new User(this.ledger));
    }
  }

  void interweave() {
    Utils.forEachIndexed(
        (user, index) -> {
          int randomPeersCount = Utils.random(1, this.users.size() / 10);

          while (user.peers.size() < randomPeersCount--) {
            this.connect(user, this.getRandomUser());
          }
        },
        this.users);
  }

  void generateRandomTransactions(int transactionsCount) throws CryptographicException {
    while (transactionsCount-- > 0) {
      User randomSender = this.getRandomUser();
      User randomReceiver = this.getRandomUser();
      int randomAmount = Utils.random(1, 100);

      randomSender.createTransaction(randomReceiver, randomAmount);
    }
  }

  void connect(User userA, User userB) {
    userA.addPeer(userB);
    userB.addPeer(userA);
  }

  User getRandomUser() {
    int randomIndex = Utils.random(this.users.size());
    return this.users.get(randomIndex);
  }
}
