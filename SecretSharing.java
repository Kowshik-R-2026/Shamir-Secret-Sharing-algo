import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;

public class SecretSharing {

    // Helper method to decode the y values from the given base to decimal
    public static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base); // Convert the given value from the specified base to decimal
    }

    // Helper function to solve the system of equations using Gaussian Elimination
    public static double[] gaussElimination(double[][] matrix, double[] results) {
        int n = matrix.length;

        // Forward Elimination (convert to upper triangular form)
        for (int i = 0; i < n; i++) {
            // Find the pivot element
            int max = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[max][i])) {
                    max = j;
                }
            }

            // Swap rows in both the matrix and the result array
            double[] temp = matrix[i];
            matrix[i] = matrix[max];
            matrix[max] = temp;

            double t = results[i];
            results[i] = results[max];
            results[max] = t;

            // Make all rows below this one 0 in the current column
            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                results[j] -= factor * results[i];
                for (int k = i; k < n; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }

        // Back Substitution
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * solution[j];
            }
            solution[i] = (results[i] - sum) / matrix[i][i];
        }

        return solution;
    }

    // Main method to read JSON and process the test case
    public static void processTestCase(String fileName) throws Exception {
        // Load and parse the JSON file
        JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));

        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Create the matrix (x-values raised to the power) and the result array (y-values)
        double[][] matrix = new double[k][k];
        double[] results = new double[k];
        int index = 0;

        for (int i = 1; i <= k; i++) {
            if (jsonObject.has(String.valueOf(i))) {
                JSONObject point = jsonObject.getJSONObject(String.valueOf(i));
                int x = i; // x is the key in the JSON
                int base = Integer.parseInt(point.getString("base"));
                BigInteger y = decodeValue(point.getString("value"), base);
                results[index] = y.doubleValue(); // Store the y value

                // Fill in the matrix for the polynomial powers of x
                for (int j = 0; j < k; j++) {
                    matrix[index][j] = Math.pow(x, k - j - 1);
                }
                index++;
            }
        }

        // Solve the system using Gaussian Elimination
        double[] solution = gaussElimination(matrix, results);

        // The constant term (c) will be the last element of the solution array
        double constantTerm = solution[k - 1];
        System.out.println("The constant term (c) from " + fileName + " is: " + (long)constantTerm);
    }

    public static void main(String[] args) throws Exception {
        // Process both test_case_1.json and test_case_2.json
        processTestCase("test_case_1.json");
        processTestCase("test_case_2.json");
    }
}
