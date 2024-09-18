import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Function to convert a number from a given base to decimal
    public static int baseToDecimal(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Function to perform Lagrange interpolation to find f(0), the constant term
    public static double lagrangeInterpolation(List<int[]> points, int k) {
        double f0 = 0;
        
        for (int j = 0; j < k; j++) {
            double product = 1;
            int xj = points.get(j)[0];
            int yj = points.get(j)[1];
            
            for (int i = 0; i < k; i++) {
                if (i != j) {
                    int xi = points.get(i)[0];
                    product *= (0.0 - xi) / (xj - xi);
                }
            }
            f0 += yj * product;
        }
        
        return f0;
    }

    // Function to parse JSON input manually
    public static int findConstantTerm(String inputJson) {
        try {
            // Extract 'n' and 'k' values
            int n = Integer.parseInt(inputJson.split("\"n\":")[1].split(",")[0].trim());
            int k = Integer.parseInt(inputJson.split("\"k\":")[1].split("}")[0].trim());

            // Extract the points and convert them to base-10 integers
            List<int[]> points = new ArrayList<>();
            
            for (int i = 1; i <= n; i++) {
                String searchKey = "\"" + i + "\":";
                if (inputJson.contains(searchKey)) {
                    String baseStr = inputJson.split(searchKey)[1].split("\"base\":")[1].split(",")[0].trim().replace("\"", "");
                    String valueStr = inputJson.split(searchKey)[1].split("\"value\":")[1].split("}")[0].trim().replace("\"", "");
                    int base = Integer.parseInt(baseStr);
                    int value = baseToDecimal(valueStr, base);
                    points.add(new int[]{i, value});
                }
            }

            // Perform Lagrange interpolation to find the constant term (f(0))
            double constantTerm = lagrangeInterpolation(points, k);
            
            return (int) Math.round(constantTerm);
        } catch (Exception e) {
            System.err.println("Error processing JSON input: " + e.getMessage());
            return 0;
        }
    }

    public static void main(String[] args) {
        // Path to the JSON file
        String filePath = "resources/testcase2.json";
        
        // Read the JSON file
        StringBuilder inputJsonBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputJsonBuilder.append(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
            return;
        }
        
        // Convert the input from the StringBuilder to a single string
        String inputJson = inputJsonBuilder.toString();

        // Call the function to find the constant term (f(0))
        int constantTerm = findConstantTerm(inputJson);
        System.out.println("Constant term (f(0)): " + constantTerm);
    }
}