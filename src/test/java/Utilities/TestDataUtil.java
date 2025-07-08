package Utilities;
import java.util.UUID;

public class TestDataUtil {
    public static String generateStrainName() {
        return "Strain_" + UUID.randomUUID().toString().substring(0, 6);
    }



public static String getRandomDigits(int length) {
    return String.valueOf((int) (Math.random() * Math.pow(10, length)));
}
public static String generateRandomTag() {
    return "TAG_" + UUID.randomUUID().toString().substring(0, 6);
}

}