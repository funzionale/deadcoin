import javax.crypto.Cipher;
import java.security.*;
import java.util.ArrayList;
import java.util.Random;

public class User {
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

    if (!this.equals(peer) && !this.peers.contains(peer)) this.peers.add(peer);
  }

  Transaction createTransaction(String message) throws Exception {
    byte[] encrypted = encrypt(this.keyPair.getPrivate(), message);
    Transaction newTransaction = new Transaction(this.keyPair.getPublic(), encrypted);

    this.uncommittedBlock.append(newTransaction);

    if (this.uncommittedBlock.isFull()) {
      // TODO: Broadcast block
      //        this.uncommittedBlock.clear();
    }

    return newTransaction;
  }

  void notifyPeers(Transaction transaction) {
    Random random = new Random();
    int numberOfPeers = random.nextInt(this.peers.size() + 1);

    for (int i = 0; i < numberOfPeers; i++) {
      User randomPeer = this.peers.get(random.nextInt(this.peers.size()));
      randomPeer.handleTransaction(transaction);
    }
  }

  void handleTransaction(Transaction transaction) {
    // @TODO [QUESTION]: Verify/confirm transaction
    // [Answer]: No!

    if (this.uncommittedBlock.contains(transaction)) {
      return;
    }

    this.uncommittedBlock.append(transaction);

    if (this.uncommittedBlock.isFull()) {
      // TODO [QUESTION]: Crossover between broadcasting transactions & blocks
      // [Answer]: Keep all in block and broadcast a subset and remove them from the Arraylist

      // TODO: Broadcast block
      //        this.uncommittedBlock.clear();
    }

    this.notifyPeers(transaction);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User that = (User) o;
    return this.getPublicKey().equals(that.getPublicKey());
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
