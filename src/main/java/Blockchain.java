import java.util.ArrayList;
import java.util.Arrays;

public class Blockchain {
  ArrayList<Block> blocks;

  Blockchain() throws CryptographicException {
    ArrayList<Transaction> transactions = new ArrayList<>();
    String previousBlockHash = "0000000000000000000000000000000000000000000000000000000000000000";
    int nonce = 2083236893;
    long createdAt = 1231006505;

    Block genesisBlock = new Block(transactions, previousBlockHash, nonce, createdAt);

    this.blocks = new ArrayList<>(Arrays.asList(genesisBlock));
  }

  void append(Block block) {
    if (this.exists(block)) {
      return;
    }

    this.blocks.add(block);
  }

  boolean exists(Block block) {
    return this.blocks.contains(block);
  }

  boolean exists(String hash) {
    return this.blocks.parallelStream().anyMatch(block -> block.hash.equals(hash));
  }

  Block last() {
    if (this.blocks.isEmpty()) {
      return null;
    }

    return this.blocks.get(this.blocks.size() - 1);
  }
}
