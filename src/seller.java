import java.util.Scanner;

public class Seller {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Seller Portal!");

        // Simulate login
        if (!loginToPortal()) {
            System.out.println("Login failed. Exiting the application.");
            return;
        }

        // Navigate to Store A
        navigateToStore("A");

        // Add items to sell
        addItemToSell();

        System.out.println("Thank you for using the Seller Portal!");
    }

    private static boolean loginToPortal() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        return true;
    }

    private static void navigateToStore(String storeName) {
    output that we have navigated to the store.
        System.out.println("Navigated to Store " + storeName + ".");
    }

    private static void addItemToSell() {
        System.out.print("Enter the name of the item you want to sell: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter the price of the item: ");
        String itemPrice = scanner.nextLine();

        System.out.println("Added " + itemName + " for sale at $" + itemPrice + ".");
    }
}
