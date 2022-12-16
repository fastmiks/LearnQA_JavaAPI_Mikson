/*Ex10: Тест на короткую фразу

В рамках этой задачи с помощью JUnit необходимо написать тест,
который проверяет длину какой-то переменной типа String с помощью любого выбранного Вами метода assert.

Если текст длиннее 15 символов, то тест должен проходить успешно. Иначе падать с ошибкой.

Результатом должна стать ссылка на такой тест.
*/
package HOMEWORK;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10_StringTest {
    String[] poem = new String[] {"В лесу родилась ёлочка", "В лесу она росла", "Зимой и летом стройная", "Зелёная была"};
    int minValue = 16;

    @Test
    public void testPoem1() {
        assertTrue(poem[0].length()>minValue-1, "Строчка больше 15 символов");
    }

    @Test
    public void testPoem2() {
        assertTrue(poem[1].length()>minValue-1, "Строчка больше 15 символов");
    }

    @Test
    public void testPoem3() {
        assertTrue(poem[2].length()>minValue-1, "Строчка больше 15 символов");
    }

    @Test
    public void testPoem4() {
        assertTrue(poem[3].length()>minValue-1, "Строчка больше 15 символов");
    }
}
