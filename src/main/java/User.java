import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class User {
  private PublicKey publicKey;
  private PrivateKey privateKey;
  ArrayList<User> peers;
  private ArrayList<Transaction> transactionsBuffer;
  private Blockchain blockchain;
  // @TODO [QUESTION]: isMiner
  // @TODO [QUESTION]: isHonest

  User(Blockchain ledger) throws NoSuchAlgorithmException {
    KeyPair keyPair = RSA.buildKeyPair();

    this.publicKey = keyPair.getPublic();
    this.privateKey = keyPair.getPrivate();
    this.peers = new ArrayList<>();
    this.transactionsBuffer = new ArrayList<>();
    this.blockchain = ledger; // @TODO: Clone the ledger
  }

  void addPeer(User peer) {
    if (!this.equals(peer) && !this.peers.contains(peer)) {
      this.peers.add(peer);
    }
  }

  void createTransaction(User receiver, int amount) {
    Transaction newTransaction = new Transaction(this.publicKey, receiver.publicKey, amount);

    this.transactionsBuffer.add(newTransaction);
    this.broadcastTransaction(newTransaction);

    if (this.isReadyToMine()) {
      List<Transaction> groupedTransactions = this.transactionsBuffer.subList(0, Block.CAPACITY);

      // @TODO: Mine block
      // @TODO: Broadcast block

      this.transactionsBuffer.removeAll(groupedTransactions);
    }
  }

  void handleTransaction(Transaction transaction) {
    // @TODO [QUESTION]: Verify transaction
    // [Answer]: No!

    if (this.transactionsBuffer.contains(transaction)) {
      return;
    }

    this.transactionsBuffer.add(transaction);
    this.broadcastTransaction(transaction);

    if (this.isReadyToMine()) {
      List<Transaction> groupedTransactions = this.transactionsBuffer.subList(0, Block.CAPACITY);

      // @TODO: Mine block
      // @TODO: Broadcast block

      this.transactionsBuffer.removeAll(groupedTransactions);
    }
  }

  void broadcastTransaction(Transaction transaction) {
    int randomPeersCount = Utils.random(1, this.peers.size() + 1);

    while (randomPeersCount-- > 0) {
      User randomPeer = this.getRandomPeer();
      randomPeer.handleTransaction(transaction);
    }
  }

  void createBlock() {
    // @TODO
  }

  void handleBlock() {
    // @TODO
  }

  void broadcastBlock() {
    // @TODO
  }

  boolean isReadyToMine() {
    return this.transactionsBuffer.size() >= Block.CAPACITY;
  }

  User getRandomPeer() {
    int randomIndex = Utils.random(this.peers.size());
    return this.peers.get(randomIndex);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User that = (User) o;
    return this.publicKey.equals(that.publicKey);
  }
}
