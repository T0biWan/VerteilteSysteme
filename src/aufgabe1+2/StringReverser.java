/**
 * Created by Tobias on 28.04.2017.
 */
public class StringReverser {
    public static String reverse(String string) {
        String returnString = "";
        int stringLength = string.length();
        char[] stringArr = string.toCharArray();
        for (int i = stringLength-1; i > -1; i--) {
            returnString += stringArr[i];
        }

        return returnString;
    }

    public static void main(String[] args) {
        System.out.println(reverse("Test"));
    }
}
