package one.xingyi.helpers;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapHelpersTest {

    @Test
    void testPrint() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1one", 1);
        map.put("2two", 2);
        map.put("3three", 3);
        map.put("4four", 4);
        map.put("5longlonglongname", 5);
        assertEquals(
                "1one              1x\n" +
                        "2two              2x\n" +
                        "3three            3x\n" +
                        "4four             4x\n" +
                        "5longlonglongname 5x", MapHelpers.print(map, (k,i) -> k + "="+ i));

    }

}