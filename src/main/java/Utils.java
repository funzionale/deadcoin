import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;

public class Utils {
  public static int random(int upperBound) {
    return random(0, upperBound);
  }

  public static int random(int lowerBound, int upperBound) {
    return new Random().nextInt(upperBound - lowerBound) + lowerBound;
  }

  public static String uuid() {
    return UUID.randomUUID().toString();
  }

  public static long now() {
    return Instant.now().getEpochSecond();
  }

  public static <T> void forEachIndexed(BiConsumer<? super T, Integer> consumer, List<T> list) {
    for (int i = 0; i < list.size(); i++) {
      consumer.accept(list.get(i), i);
    }
  }
}
