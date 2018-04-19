import java.security.PublicKey;
import java.util.UUID;

public class Transaction {
  // TODO: Timestamp
  // TODO: Recipient, sender & amount
  // TODO [QUESTION]: TTL
  // [Answer]: No need. Randomize selected peers
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
