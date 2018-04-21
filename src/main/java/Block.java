import java.util.ArrayList;

public class Block {
  final ArrayList<Transaction> transactions;
  final String hash;
  final String previousBlockHash;
  final int nonce;
  final long createdAt;
  static final int CAPACITY = 10;

  Block(ArrayList<Transaction> transactions, String hash, String previousBlockHash, int nonce) {
    this.transactions = transactions;
    this.hash = hash;
    this.previousBlockHash = previousBlockHash;
    this.nonce = nonce;
    this.createdAt = Utils.now();
  }
}
