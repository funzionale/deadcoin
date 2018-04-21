import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
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
  ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    this.id = Utils.uuid();
    this.senderPublicKey = senderPublicKey;
    this.receiverPublicKey = receiverPublicKey;
    this.amount = amount;
    this.createdAt = Utils.now();
    this.signature = createSignature(senderPrivateKey, receiverPublicKey, amount);
  }

  static byte[] createSignature(
    DSAPrivateKey senderPrivateKey,
    DSAPublicKey receiverPublicKey,
    int amount
  ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    String receiverPublicKeyStringified = receiverPublicKey.toString();
    String amountStrigified = String.valueOf(amount);

    String data = String.join("\n", receiverPublicKeyStringified, amountStrigified);
    return DSA.sign(senderPrivateKey, data.getBytes());
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
