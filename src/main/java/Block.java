import java.util.ArrayList;

public class Block {
  String previousBlockHash;
  private ArrayList<Transaction> transactions;
  static final int capacity = 10;

  Block() {
    this.transactions = new ArrayList<>();
  }

  void append(Transaction transaction) {
    this.transactions.add(transaction);
  }

  boolean contains(Transaction transaction) {
    return this.transactions.contains(transaction);
  }

  boolean isFull() {
    return this.transactions.size() >= capacity;
  }

  void clear() {
    for (int i = 0; i < capacity; i++) {
      this.transactions.remove(i);
    }
  }
}
