package HOMEWORK;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10_StringTest_2 {

    @ParameterizedTest
    @ValueSource(strings = {"В лесу родилась ёлочка", "В лесу она росла", "Зимой и летом стройная", "Зелёная была"} )

    public void testPoem2(String line) {
        int minValue = 16;
        Map<String, String> hash = new HashMap<>();
        if (line.length() > 0) {
            hash.put("name", line);
        }
        assertTrue(line.length()>minValue-1, "Строчка больше 15 символов");
    }
}
