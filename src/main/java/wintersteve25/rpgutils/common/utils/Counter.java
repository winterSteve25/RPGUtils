package wintersteve25.rpgutils.common.utils;

import java.util.Arrays;
import java.util.Iterator;

public class Counter implements Iterator<Integer[]> {

    private final Integer[] nums;
    private final int[] maximums;

    public Counter(int... maximums) {
        nums = new Integer[maximums.length];
        Arrays.fill(nums, 0);
        nums[0] = -1;
        this.maximums = maximums;
    }

    @Override
    public boolean hasNext() {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != maximums[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer[] next() {
        increment(0);
        Integer[] numsCopy = new Integer[nums.length];
        System.arraycopy(nums, 0, numsCopy, 0, nums.length);
        return numsCopy;
    }

    // Call recursively
    private void increment(int index) {
        if (nums[index] == maximums[index]) {
            nums[index] = 0;
            increment(index + 1);
        } else {
            nums[index]++;
        }
    }
}
