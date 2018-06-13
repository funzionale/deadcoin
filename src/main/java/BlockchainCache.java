import java.util.ArrayList;
import java.util.Arrays;

public class BlockchainCache {
  ArrayList<ArrayList<Block>> cache;

  public BlockchainCache() {
    this.cache = new ArrayList<>();
  }

  int append(Block block) {
    int cacheSize = this.cache.size();

    for (int i = 0; i < cacheSize; i++) {
      ArrayList<Block> blockArray = this.cache.get(i);

      if (blockArray.get(blockArray.size() - 1).hash.equals(block.previousBlockHash)) {
        blockArray.add(block);
        return i;
      }
    }

    ArrayList<Block> blockArray = new ArrayList<>(Arrays.asList(block));
    this.cache.add(blockArray);
    return cacheSize;
  }

  String getCacheEntryPreviousHash(int index) {
    return this.cache.get(index).get(0).previousBlockHash;
  }

  int getCacheEntryGetSize(int index) {
    return this.cache.get(index).size();
  }

  void discard(int cacheIndex) {
    this.cache.remove(cacheIndex);
  }

  ArrayList<Block> swap(int index, ArrayList<Block> newCacheEntry) {
    return this.cache.set(index, newCacheEntry);
  }
}
