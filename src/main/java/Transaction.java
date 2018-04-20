import java.security.PublicKey;
import java.util.UUID;

public class Transaction {
  String id;
  PublicKey senderPublicKey;
  PublicKey receiverPublicKey;
  int amount;
  long createdAt;

  Transaction() {
    this.id = UUID.randomUUID().toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Transaction that = (Transaction) o;
    return this.id.equals(that.id);
  }
}
