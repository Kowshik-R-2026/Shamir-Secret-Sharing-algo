import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SecretSharing {

    // Helper method to decode the y values from the given base to decimal
    public static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base); // This will convert the given value in specific base to decimal
    }

    // Lagrange interpolation to find the constant term (c) of the polynomial
    public static BigInteger lagrangeInterpolation(List<int[]> points, int k) {
        BigInteger result = BigInteger.ZERO;
        BigInteger modulo = BigInteger.valueOf(Long.MAX_VALUE); // Assume some large prime to avoid overflow

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(points.get(i)[0]);
            BigInteger yi = BigInteger.valueOf(points.get(i)[1]);

            BigInteger term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(points.get(j)[0]);
                    term = term.multiply(xj.negate()).mod(modulo);
                    term = term.multiply(xi.subtract(xj).modInverse(modulo)).mod(modulo);
                }
            }
            result = result.add(term).mod(modulo);
        }
        return result;
    }

    // Main method to read JSON and process the test case
    public static void processTestCase(String fileName) throws Exception {
        // Load and parse the JSON file
        JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));

        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        List<int[]> points = new ArrayList<>();

        // Parse and decode the points from the JSON object
        for (int i = 1; i <= n; i++) {
            if (jsonObject.has(String.valueOf(i))) {
                JSONObject point = jsonObject.getJSONObject(String.valueOf(i));
                int x = i; // x is the key in the JSON
                int base = Integer.parseInt(point.getString("base"));
                BigInteger y = decodeValue(point.getString("value"), base);
                points.add(new int[] {x, y.intValue()});
            }
        }

        // Perform Lagrange interpolation to find the constant term (c)
        BigInteger constantTerm = lagrangeInterpolation(points, k);
        System.out.println("The constant term (c) from " + fileName + " is: " + constantTerm);
    }

    public static void main(String[] args) throws Exception {
        // Process both test_case_1.json and test_case_2.json
        processTestCase("test_case_1.json");
        processTestCase("test_case_2.json");
    }
}
