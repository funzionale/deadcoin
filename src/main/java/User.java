import java.security.KeyPair;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.List;

public class User {
  final DSAPublicKey publicKey;
  final DSAPrivateKey privateKey;
  final ArrayList<User> peers;
  final ArrayList<Transaction> transactionsBuffer;
  final Blockchain blockchain;
  final BlockchainCache blockchainCache;
  // @TODO [QUESTION]: isMiner
  // @TODO [QUESTION]: isHonest

  User(Blockchain ledger) throws CryptographicException {
    KeyPair keyPair = DSA.buildKeyPair();

    this.publicKey = (DSAPublicKey) keyPair.getPublic();
    this.privateKey = (DSAPrivateKey) keyPair.getPrivate();
    this.peers = new ArrayList<>();
    this.transactionsBuffer = new ArrayList<>();
    this.blockchain = ledger.clone();
    this.blockchainCache = new BlockchainCache();
  }

  void addPeer(User peer) {
    if (!this.equals(peer) && !this.peers.contains(peer)) {
      this.peers.add(peer);
    }
  }

  void createTransaction(User receiver, int amount) throws CryptographicException {
    Transaction newTransaction =
        new Transaction(this.privateKey, this.publicKey, receiver.publicKey, amount);

    this.transactionsBuffer.add(newTransaction);
    this.broadcastTransaction(newTransaction);

    if (this.isReadyToMine()) {
      List<Transaction> groupedTransactions = this.transactionsBuffer.subList(0, Block.CAPACITY);

      Block block = this.createBlock(groupedTransactions);
      this.broadcastBlock(block);

      this.transactionsBuffer.removeAll(groupedTransactions);
    }
  }

  void handleTransaction(Transaction transaction) throws CryptographicException {
    if (!transaction.hasValidSignature()) {
      return;
    }

    if (this.transactionsBuffer.contains(transaction)) {
      return;
    }

    this.transactionsBuffer.add(transaction);
    this.broadcastTransaction(transaction);

    if (this.isReadyToMine()) {
      List<Transaction> groupedTransactions = this.transactionsBuffer.subList(0, Block.CAPACITY);

      Block block = this.createBlock(groupedTransactions);
      this.blockchain.append(block);
      this.broadcastBlock(block);

      this.transactionsBuffer.removeAll(groupedTransactions);
    }
  }

  void broadcastTransaction(Transaction transaction) throws CryptographicException {
    int randomPeersCount = Utils.random(1, this.peers.size() + 1);

    while (randomPeersCount-- > 0) {
      User randomPeer = this.getRandomPeer();
      randomPeer.handleTransaction(transaction);
    }
  }

  Block createBlock(List<Transaction> transactions) throws CryptographicException {
    return new Block(transactions, this.blockchain);
  }

  void handleBlock(Block block) {
    if (this.blockchain.exists(block)) {
      return;
    }

    if (block.previousBlockHash.equals(this.blockchain.last().hash)) {
      this.blockchain.append(block);
    } else if (block.previousBlockHash.equals(this.blockchain.last().previousBlockHash)) {
      this.blockchainCache.append(block);
    } else {
      int cacheIndex = this.blockchainCache.append(block);
      int cacheEntrySize = this.blockchainCache.getCacheEntryGetSize(cacheIndex);
      String previousHash = this.blockchainCache.getCacheEntryPreviousHash(cacheIndex);

      for (int i = blockchain.size() - 1; i >= 0; i--) {
        if (blockchain.get(i).previousBlockHash.equals(previousHash)) {
          int steps = blockchain.size() - i;

          if (steps < cacheEntrySize) {
            ArrayList<Block> blocks = this.blockchain.remove(i, this.blockchain.size());
            ArrayList<Block> cachedBlocks = this.blockchainCache.swap(cacheIndex, blocks);
            this.blockchain.append(cachedBlocks);
          } else if (steps - cacheEntrySize >= 3) {
            blockchainCache.discard(cacheIndex);
          }
        }
      }
    }
  }

  void broadcastBlock(Block block) {
    int randomPeersCount = Utils.random(1, this.peers.size() + 1);

    while (randomPeersCount-- > 0) {
      User randomPeer = this.getRandomPeer();
      randomPeer.handleBlock(block);
    }
  }

  boolean isReadyToMine() {
    return this.transactionsBuffer.size() >= Block.CAPACITY;
  }

  User getRandomPeer() {
    int randomIndex = Utils.random(this.peers.size());
    return this.peers.get(randomIndex);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User that = (User) o;
    return this.publicKey.equals(that.publicKey);
  }
}
