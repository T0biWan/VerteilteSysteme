public class Main {
    public static void main(String args[]) {
        Fibonacci fibonacci = new Fibonacci();
        OfflineClient offlineClient = new OfflineClient();

//        int input = offlineClient.inputInt();
//        System.out.println("Fibonacci #" + input + ": " + fibonacci.getFibonacci(input));

//        for (int i = 0; i<10; i++) fibonacci.getFibonacci(i);
        System.out.println(fibonacci.memory.contains(10));
        fibonacci.memory.add(10);
        System.out.println(fibonacci.memory.contains(10));
        System.out.println(fibonacci.getFibonacci(10));
    }
}
