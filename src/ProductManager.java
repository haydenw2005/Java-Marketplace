import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ProductManager {
    public ProductManager() {
    }

    public static void main(String[] args) {
        String inputFilePath = "product.csv";
        String outputFilePath = "output.csv";
        List<String[]> data = readCSV(inputFilePath);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ProductID you want to modify: ");
        String productIdToModify = scanner.nextLine();
        Iterator var6 = data.iterator();

        while (var6.hasNext()) {
            String[] row = (String[]) var6.next();
            if (productIdToModify.equals(row[0])) {
                System.out.print("Enter the new StoreName: ");
                row[1] = scanner.nextLine();
                System.out.print("Enter the new ProductName: ");
                row[2] = scanner.nextLine();
                System.out.print("Enter the new Description: ");
                row[3] = scanner.nextLine();
                System.out.print("Enter the new Quantity: ");
                row[4] = scanner.nextLine();
                System.out.print("Enter the new Price: ");
                row[5] = scanner.nextLine();
                break;
            }
        }

        writeCSV(outputFilePath, data);
        System.out.println("CSV processing completed. Output written to: " + outputFilePath);
    }

    private static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<String[]>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line;
            try {
                while ((line = br.readLine()) != null) {
                    String[] row = line.split(",");
                    data.add(row);
                }
            } catch (Throwable var6) {
                try {
                    br.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            br.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return data;
    }

    private static void writeCSV(String filePath, List<String[]> data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

            try {
                Iterator var3 = data.iterator();

                while (var3.hasNext()) {
                    String[] row = (String[]) var3.next();
                    StringBuilder line = new StringBuilder();

                    for (int i = 0; i < row.length; ++i) {
                        line.append(row[i]);
                        if (i < row.length - 1) {
                            line.append(",");
                        }
                    }

                    bw.write(line.toString());
                    bw.newLine();
                }
            } catch (Throwable var8) {
                try {
                    bw.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }

                throw var8;
            }

            bw.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }
}
