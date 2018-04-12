import javax.crypto.Cipher;
import java.security.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {
        Network network = new Network();
        network.populate(100);
        network.generateRandomTransactions(10);
    }

    static class Network {
        Blockchain ledger;
        ArrayList<User> users;

        Network() {
            this.ledger = new Blockchain();
            this.users = new ArrayList<>();
        }

        void populate(int usersCount) {
            for (int i = 0; i < usersCount; i++) {
                this.users.add(new User(ledger));
            }

            Random random = new Random();

            for (int i = 0; i < this.users.size(); i++) {
                User user = this.users.get(i);

                for (int j = i; j < random.nextInt(this.users.size() / 10) + i && j < users.size(); j++) {
                    User peer = this.users.get(j);

                    user.addPeer(peer);
                    peer.addPeer(user);
                }
            }
        }

        void generateRandomTransactions(int period) throws Exception {
            Random random = new Random();
            long i = 0;
            while (i < 1000) {
                User randomSender = this.users.get(random.nextInt(this.users.size()));
                Transaction transaction = randomSender.createTransaction("Hello World");

                randomSender.notifyPeers(transaction);

                System.out.println(i++);
            }
        }
    }

    static class User {
        // @TODO: User honesty
        ArrayList<User> peers;
        Blockchain blockchain;
        Block uncommittedBlock;
        private KeyPair keyPair;

        User(Blockchain ledger) {
            try {
                this.keyPair = buildKeyPair();
                this.peers = new ArrayList<>();
                // @TODO: Clone the ledger
                this.blockchain = ledger;
                this.uncommittedBlock = new Block();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        void addPeer(User peer) {

            if (!this.equals(peer) && !this.peers.contains(peer))
                this.peers.add(peer);
        }

        Transaction createTransaction(String message) throws Exception {
            byte[] encrypted = encrypt(this.keyPair.getPrivate(), message);
            Transaction newTransaction = new Transaction(this.keyPair.getPublic(), encrypted);

            this.uncommittedBlock.append(newTransaction);

            if (this.uncommittedBlock.isFull()) {
                // TODO: Broadcast block
//        this.uncommittedBlock.clear();
            }

            return newTransaction;
        }

        void notifyPeers(Transaction transaction) {
            Random random = new Random();
            int numberOfPeers = random.nextInt(this.peers.size() + 1);

            for (int i = 0; i < numberOfPeers; i++) {
                User randomPeer = this.peers.get(random.nextInt(this.peers.size()));
                randomPeer.handleTransaction(transaction);
            }
        }

        void handleTransaction(Transaction transaction) {
            // @TODO [QUESTION]: Verify/confirm transaction
            // [Answer]: No!

            if (this.uncommittedBlock.contains(transaction)) {
                return;
            }

            this.uncommittedBlock.append(transaction);

            if (this.uncommittedBlock.isFull()) {
                // TODO [QUESTION]: Crossover between broadcasting transactions & blocks
                // [Answer]: Keep all in block and broadcast a subset and remove them from the Arraylist

                // TODO: Broadcast block
//        this.uncommittedBlock.clear();
            }

            this.notifyPeers(transaction);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User that = (User) o;
            return this.getPublicKey().equals(that.getPublicKey());
        }

        public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
            final int keySize = 512;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.genKeyPair();
        }

        public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            return cipher.doFinal(message.getBytes());
        }

        public static byte[] decrypt(PublicKey publicKey, byte[] encrypted) throws Exception {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            return cipher.doFinal(encrypted);
        }

        public PublicKey getPublicKey() {
            return this.keyPair.getPublic();
        }
    }

    static class Transaction {
        // TODO: Timestamp
        // TODO: Recipient, sender & amount
        // TODO [QUESTION]: TTL
        // [Answer]: No need. Randomize selected peers
        private String id;
        private PublicKey publicKey;
        private byte[] encryptedMessage;

        public Transaction(PublicKey publicKey, byte[] encryptedMessage) {
            this.id = UUID.randomUUID().toString();
            this.publicKey = publicKey;
            this.encryptedMessage = encryptedMessage;
        }

        public String getId() {
            return id;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public byte[] getEncryptedMessage() {
            return encryptedMessage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Transaction that = (Transaction) o;
            return this.id.equals(that.id);
        }
    }

    static class Block {
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

    static class Blockchain {
        void append(Block block) {
        }
    }

    static class BlockOverflowException extends Exception {
    }
}
