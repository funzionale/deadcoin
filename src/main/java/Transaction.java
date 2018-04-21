import java.security.PublicKey;

public class Transaction {
  final String id;
  final PublicKey senderPublicKey;
  final PublicKey receiverPublicKey;
  final int amount;
  final long createdAt;

  Transaction(PublicKey senderPublicKey, PublicKey receiverPublicKey, int amount) {
    this.id = Utils.uuid();
    this.senderPublicKey = senderPublicKey;
    this.receiverPublicKey = receiverPublicKey;
    this.amount = amount;
    this.createdAt = Utils.now();
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
