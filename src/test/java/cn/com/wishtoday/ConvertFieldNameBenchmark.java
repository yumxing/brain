package cn.com.wishtoday;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Benchmark)
public class ConvertFieldNameBenchmark {

    private static final String UNDERSCORE_NAME = "first_NAME_LAST_NAME";

    @Benchmark
    public String originalMethod() {
        return convertFieldName(UNDERSCORE_NAME);
    }

    @Benchmark
    public String optimizedMethod() {
        return convertFieldName1(UNDERSCORE_NAME);
    }

    private static String convertFieldName(String underscoreName) {
        String[] fields = underscoreName.toLowerCase().split("_");
        StringBuilder sBuilder = new StringBuilder(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            char[] cs = fields[i].toCharArray();
            cs[0] -= 32;
            sBuilder.append(String.valueOf(cs));
        }
        return sBuilder.toString();
    }

    private static String convertFieldName1(String underscoreName) {
        int len = underscoreName.length();
        StringBuilder sBuilder = new StringBuilder(len);// 预分配容量
        boolean isFirstField = true;// 标记是否是第一个字段
        // 遍历输入字符串中的每个字符
        for (int i = 0; i < len; i++) {
            char c = underscoreName.charAt(i);
            // 如果遇到下划线，跳过并标记下一个字符为大写
            if (c == '_') {
                isFirstField = false;
                continue;
            }
            // 如果不是第一个字段的第一个字符，将其转为大写
            if (!isFirstField) {
                c = Character.toUpperCase(c);
                isFirstField = true;
            } else {
                c = Character.toLowerCase(c);
            }
            // 添加字符到 StringBuilder
            sBuilder.append(c);
        }
        return sBuilder.toString();
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ConvertFieldNameBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
