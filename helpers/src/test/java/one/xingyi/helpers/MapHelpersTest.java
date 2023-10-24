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
                "{\n" +
                        "  \"1one\"             :1one=1,\n" +
                        "  \"2two\"             :2two=2,\n" +
                        "  \"3three\"           :3three=3,\n" +
                        "  \"4four\"            :4four=4,\n" +
                        "  \"5longlonglongname\":5longlonglongname=5\n" +
                        "}", MapHelpers.jsonPrint("\n", map, (k, i) -> k + "=" + i));

    }

}