import java.util.ArrayList;

public class Block {
  String hash;
  String previousBlockHash;
  ArrayList<Transaction> transactions;
  int nonce;
  long createdAt;
  static final int CAPACITY = 10;

  Block() {
    this.transactions = new ArrayList<>();
  }

  boolean contains(Transaction transaction) {
    return this.transactions.contains(transaction);
  }
}
