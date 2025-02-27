package top.marchand.java.countdown.number;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record Draw (int[] numbers, int target) {
  public Draw {
    if (numbers.length != 6) {
      throw new IllegalStateException("Invalid Draw. It must contains exactly 6 numbers.");
    }
    if(target<100) {
      throw new IllegalStateException("Invalid Draw. Target must be between 100 and 1000");
    }
    if(target>=1000) {
      throw new IllegalStateException("Invalid Draw. Target must be between 100 and 1000");
    }
  }
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final List<Integer> SMALLS = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10));
    private final List<Integer> BIGS = new ArrayList<>(List.of(25,50,75,100));
    private final List<Integer> selectedValues = new ArrayList<>();
    private final Random random = new Random();

    public Builder small() {
      checkSize();
      int i = random.nextInt(SMALLS.size());
      selectedValues.add(SMALLS.remove(i));
      return this;
    }
    public Builder big() {
      checkSize();
      if(BIGS.isEmpty()) throw new IllegalStateException("Illegal call. 4 bigs have already been selected.");
      int i = random.nextInt(BIGS.size());
      selectedValues.add(BIGS.remove(i));
      return this;
    }
    public Draw build() {
      if(selectedValues.size()<6) {
        ArrayList<Integer> inputs = new ArrayList<>(SMALLS.size() + BIGS.size());
        inputs.addAll(SMALLS);
        inputs.addAll(BIGS);
        while(selectedValues.size()<6) {
          int i = random.nextInt(inputs.size());
          selectedValues.add(inputs.remove(i));
        }
      }
      return new Draw(
          selectedValues
              .stream()
              .mapToInt(i -> i)
              .toArray(),
          random.nextInt(100, 1000)
      );
    }

    private void checkSize() throws IllegalStateException{
      if(selectedValues.size() >= 6) {
        throw new IllegalStateException("Illegal call. It must not contains more than 6 numbers.");
      }
    }
  }
}
