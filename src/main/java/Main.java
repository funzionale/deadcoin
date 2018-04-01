import javax.crypto.Cipher;
import java.security.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Main {
  public static void main(String[] args) throws Exception {
    Network network = new Network();
    network.populate(100);
    network.generateRandomTransactions(10);
  }

  static class Network {
    Blockchain ledger;
    ArrayList<User> users;

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

      while (1 != 0) {
        User randomSender = this.users.get(random.nextInt(this.users.size()));
        Transaction transaction = randomSender.createTransaction("Hello World");

        randomSender.notifyPeers(transaction);

        System.out.println("asdfsdhfhhh");
      }
    }
  }

  static class User {
    // @TODO: User honesty
    ArrayList<User> peers;
    Blockchain blockchain;
    Block uncommittedBlock;
    private KeyPair keyPair;

    User(Blockchain ledger) {
      try {
        this.keyPair = buildKeyPair();
        this.peers = new ArrayList<>();
        // @TODO: Clone the ledger
        this.blockchain = ledger;
        this.uncommittedBlock = new Block();
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    }

    void addPeer(User peer) {
      // TODO: Safeguard against duplicates & yourself
      this.peers.add(peer);
    }

    Transaction createTransaction(String message) throws Exception {
      byte[] encrypted = encrypt(this.keyPair.getPrivate(), message);
      Transaction newTransaction = new Transaction(this.keyPair.getPublic(), encrypted);

      this.uncommittedBlock.append(newTransaction);

      if (this.uncommittedBlock.isFull()) {
        // TODO: Broadcast block
        this.uncommittedBlock.clear();
      }

      return newTransaction;
    }

    void notifyPeers(Transaction transaction) throws BlockOverflowException {
      for (User peer : this.peers) {
        peer.handleTransaction(transaction);
      }
    }

    void handleTransaction(Transaction transaction) throws BlockOverflowException {
      // @TODO [QUESTION]: Verify/confirm transaction

      if (this.uncommittedBlock.contains(transaction)) {
        return;
      }

      this.uncommittedBlock.append(transaction);

      if (this.uncommittedBlock.isFull()) {
        // TODO [QUESTION]: Crossover between broadcasting transactions & blocks
        this.uncommittedBlock.clear();
      }

      this.notifyPeers(transaction);
    }

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
      final int keySize = 512;
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(keySize);
      return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);

      return cipher.doFinal(message.getBytes());
    }

    public static byte[] decrypt(PublicKey publicKey, byte[] encrypted) throws Exception {
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, publicKey);

      return cipher.doFinal(encrypted);
    }

    public PublicKey getPublicKey() {
      return this.keyPair.getPublic();
    }
  }

  static class Transaction {
    // TODO: Timestamp
    // TODO: Recipient, sender & amount
    // TODO [QUESTION]: TTL
    private String id;
    private PublicKey publicKey;
    private byte[] encryptedMessage;

    public Transaction(PublicKey publicKey, byte[] encryptedMessage) {
      this.id = UUID.randomUUID().toString();
      this.publicKey = publicKey;
      this.encryptedMessage = encryptedMessage;
    }

    public String getId() {
      return id;
    }

    public PublicKey getPublicKey() {
      return publicKey;
    }

    public byte[] getEncryptedMessage() {
      return encryptedMessage;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Transaction that = (Transaction) o;
      return this.id.equals(that.id);
    }
  }

  static class Block {
    String previousBlockHash;
    private ArrayList<Transaction> transactions;
    static final int capacity = 10;

    Block() {
      this.transactions = new ArrayList<>();
    }

    void append(Transaction transaction) throws BlockOverflowException {
      if (this.isFull()) {
        throw new BlockOverflowException();
      }

      this.transactions.add(transaction);
    }

    boolean contains(Transaction transaction) {
      return this.transactions.contains(transaction);
    }

    boolean isFull() {
      return this.transactions.size() == capacity;
    }

    void clear() {
      this.transactions.clear();
    }
  }

  static class Blockchain {
    void append(Block block) {}
  }

  static class BlockOverflowException extends Exception {}
}
