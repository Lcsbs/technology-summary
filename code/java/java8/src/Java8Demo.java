import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author ：Lcsbs @ https://github.com/Lcsbs
 * Created in 2020/10/27 16:41
 * @description ：A Guide to Java 8
 */
public class Java8Demo {
    public static void main(String[] args) {
        //Lambda 表达式
        List<String> names = Arrays.asList("spark", "hadoop", "redis", "kafka");
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });

        //Method 和 Construct 引用
        TestClass testClass = new TestClass();
        Function<String, String> test = testClass::test;
        String hello = test.apply("Hello");
        System.out.println(hello);

        //流 Streams 函数式编程
        List<String> stringList = Arrays.asList("a1", "a2", "b1", "c2", "c1");

        stringList.stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);

    }

}
