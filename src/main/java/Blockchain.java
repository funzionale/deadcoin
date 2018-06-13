import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blockchain {
  final ArrayList<Block> blocks;

  Blockchain() throws CryptographicException {
    ArrayList<Transaction> transactions = new ArrayList<>();
    String previousBlockHash = "0000000000000000000000000000000000000000000000000000000000000000";
    int nonce = 2083236893;
    long createdAt = 1231006505;

    Block genesisBlock = new Block(transactions, previousBlockHash, nonce, createdAt);

    this.blocks = new ArrayList<>(Arrays.asList(genesisBlock));
  }

  Blockchain(ArrayList<Block> blocks) {
    this.blocks = blocks;
  }

  void append(Block block) {
    if (this.exists(block)) {
      return;
    }

    this.blocks.add(block);
  }

  void append(ArrayList<Block> blocks) {
    for (Block block : blocks) {
      this.append(block);
    }
  }

  Block get(int index) {
    return this.blocks.get(index);
  }

  boolean exists(Block block) {
    return this.blocks.contains(block);
  }

  boolean exists(String hash) {
    return this.blocks.parallelStream().anyMatch(block -> block.hash.equals(hash));
  }

  int size() {
    return this.blocks.size();
  }

  Block last() {
    if (this.blocks.isEmpty()) {
      return null;
    }

    return this.blocks.get(this.blocks.size() - 1);
  }

  ArrayList<Block> remove(int startIndex, int endIndex) {
    ArrayList<Block> blocks = new ArrayList<>();

    for (int i = startIndex; i < endIndex; i++) {
      blocks.add(this.blocks.get(i));
    }

    this.blocks.removeAll(blocks);
    return blocks;
  }

  @Override
  protected Blockchain clone() {
    Block[] blocks = this.blocks.toArray(new Block[this.blocks.size()]);
    return new Blockchain(new ArrayList<>(Arrays.asList(blocks)));
  }
}
