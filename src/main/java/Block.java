import java.util.List;

public class Block {
  static final int CAPACITY = 10;
  final List<Transaction> transactions;
  final String previousBlockHash;
  final String hash;
  final int nonce;
  final long createdAt;

  public Block(
    List<Transaction> transactions,
    String previousBlockHash,
    int nonce,
    long createdAt
  ) throws CryptographicException {
    this.transactions = transactions;
    this.previousBlockHash = previousBlockHash;
    this.nonce = nonce;
    this.createdAt = createdAt;
    this.hash = createHash(previousBlockHash, nonce, createdAt, transactions);
  }

  Block(List<Transaction> transactions, Blockchain blockchain) throws CryptographicException {
    String previousBlockHash = blockchain.last().hash;
    int nonce = 0;
    long createdAt = Utils.now();
    String hash = null;

    for (; nonce < Integer.MAX_VALUE; nonce++) {
      hash = createHash(previousBlockHash, nonce, createdAt, transactions);

      if (isValidHash(hash) && !blockchain.exists(hash)) {
        break;
      }
    }

    this.transactions = transactions;
    this.previousBlockHash = previousBlockHash;
    this.nonce = nonce;
    this.createdAt = createdAt;
    this.hash = hash;
  }

  static String createHash(
    String previousBlockHash,
    int nonce,
    long createdAt,
    List<Transaction> transactions
  ) throws CryptographicException {
    String transactionsIds = transactions
      .stream()
      .map(transaction -> transaction.id)
      .reduce(String::concat)
      .orElse("");

    String data = String.join(
      "",
      previousBlockHash,
      String.valueOf(nonce),
      String.valueOf(createdAt),
      transactionsIds
    );

    return SHA256.hash(data);
  }

  static boolean isValidHash(String hash) {
    return hash.startsWith("00");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Block that = (Block) o;
    return this.hash.equals(that.hash);
  }
}
