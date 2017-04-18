import java.util.ArrayList;
import java.util.List;

public class Fibonacci {
    List<Integer> memory = new ArrayList();


    public int getFibonacci(int n) {
        int fibonacci = -1;
        if (memory.contains(n)) fibonacci = memory.get(n);
        if (n == 0) fibonacci = 0;
        if (n == 1) fibonacci = 1;
        fibonacci = (getFibonacci(n - 1) + getFibonacci(n - 2));
        if (!memory.contains(n)) memory.add(n);
        return fibonacci;
    }
}
