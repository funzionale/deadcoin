import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

public class Transaction {
  final String id;
  final DSAPublicKey senderPublicKey;
  final DSAPublicKey receiverPublicKey;
  final int amount;
  final long createdAt;
  final byte[] signature;

  Transaction(
    DSAPrivateKey senderPrivateKey,
    DSAPublicKey senderPublicKey,
    DSAPublicKey receiverPublicKey,
    int amount
  ) throws CryptographicException {
    this.id = Utils.uuid();
    this.senderPublicKey = senderPublicKey;
    this.receiverPublicKey = receiverPublicKey;
    this.amount = amount;
    this.createdAt = Utils.now();
    this.signature = this.createSignature(senderPrivateKey, receiverPublicKey, amount);
  }

  byte[] createSignature(
    DSAPrivateKey senderPrivateKey,
    DSAPublicKey receiverPublicKey,
    int amount
  ) throws CryptographicException {
    String receiverPublicKeyStringified = receiverPublicKey.toString();
    String amountStringified = String.valueOf(amount);

    byte[] data = String.join("\n", receiverPublicKeyStringified, amountStringified).getBytes();
    return DSA.sign(senderPrivateKey, data);
  }

  boolean hasValidSignature() throws CryptographicException {
    String receiverPublicKeyStringified = this.receiverPublicKey.toString();
    String amountStringified = String.valueOf(this.amount);

    byte[] data = String.join("\n", receiverPublicKeyStringified, amountStringified).getBytes();
    return DSA.verify(this.senderPublicKey, data, this.signature);
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
