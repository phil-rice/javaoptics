package one.xingyi.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface Permutations {

    static List<Boolean> toList(int num, int size) {
        List<Boolean> result = new ArrayList<>();
        String str = Integer.toBinaryString(num);
        for (int i = 0; i < size; i++)
            if (i < str.length())
                result.add(str.charAt(str.length() - 1 - i) == '1');
            else
                result.add(false);
        return result;
    }
    static Stream<List<Boolean>> permutate(int size) {
        int max = (int) (0.5 + Math.pow(2, size));
        return Stream.iterate(0, i -> i + 1).limit(max).map(i -> toList(i, size));
    }


}
