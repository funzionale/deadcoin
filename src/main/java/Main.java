import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {
      Network network = new Network();
      network.populate(5);
  }

  static class Network {
      ArrayList<User> users;

      Network() {
          this.users = new ArrayList<User>();
      }

      void populate(int usersCount) {
          for (int i = 0; i < usersCount; i++) {
              this.users.add(new User());
          }
      }
  }

  static class User {
      ArrayList<User> peers;
      Blockchain blockchain;
      Block uncommittedBlock;

      void notifyPeers() {}

      void handleTransaction() {}
  }

  static class Transaction {
      String id;
  }

  static class Block {
      String previousBlockHash;

      void append(Transaction transaction) {}
      boolean containsa(Transaction transaction) { return false; }
  }

  static class Blockchain {
      void append(Block block) {}
  }
}
