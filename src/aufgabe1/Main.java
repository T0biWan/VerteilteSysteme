public class Main {
    public static void main(String args[]) {
        Fibonacci fibonacci = new Fibonacci();
        OfflineClient offlineClient = new OfflineClient();

        int input = offlineClient.inputInt();
        System.out.println(fibonacci.getFibonacci(input));
    }
}
