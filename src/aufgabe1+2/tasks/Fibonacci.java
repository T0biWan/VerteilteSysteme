package tasks;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {
    Map<Integer, Integer> memory = new HashMap();

    public int getFibonacci(int n) {
        int fibonacci = -1;
        if (memory.containsKey(n)) {
            fibonacci = memory.get(n);
        } else {
            fibonacci = fibonacci(n);
            memory.put(n, fibonacci);
        }

        return fibonacci;
    }

    private int fibonacci(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return (fibonacci(n - 1) + fibonacci(n - 2));
    }
}
