import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;


public class SellerGUI extends JFrame implements ActionListener {
    private Seller seller;
    private ObjectMapper objectMapper;
    private JButton addItemButton;
    private JButton deleteItemButton;
    private JButton createNewStoreButton;
    private JButton editStoreButton;
    private JButton viewStoreSalesButton; // New button for viewing store sales
    private JFrame frame;

    public SellerGUI(Seller seller, ObjectMapper objectMapper) {
        this.seller = seller;
        this.objectMapper = objectMapper;

        frame = new JFrame("Seller Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addItemButton = new JButton("Add Item to Store");
        addItemButton.addActionListener(this);

        deleteItemButton = new JButton("Delete Item from Store");
        deleteItemButton.addActionListener(this);

        createNewStoreButton = new JButton("Create New Store");
        createNewStoreButton.addActionListener(this);

        editStoreButton = new JButton("Edit Store");
        editStoreButton.addActionListener(this);

        viewStoreSalesButton = new JButton("View Store Sales"); // Initialize the new button
        viewStoreSalesButton.addActionListener(this); // Add action listener for the new button

        JPanel panel = new JPanel();
        panel.add(addItemButton);
        panel.add(deleteItemButton);
        panel.add(createNewStoreButton);
        panel.add(editStoreButton);
        panel.add(viewStoreSalesButton); // Add the new button to the panel

        frame.add(panel);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // Existing functionality remains the same
        if (e.getSource() == addItemButton) {
            addItemToStore();
        } else if (e.getSource() == deleteItemButton) {
            deleteItemFromStore();
        } else if (e.getSource() == createNewStoreButton) {
            createNewStore();
        } else if (e.getSource() == editStoreButton) {
            editStore();
        }

        // Adding functionality for the new button
        else if (e.getSource() == viewStoreSalesButton) {
            viewStoreSales(); // Call the method to view store sales
        }
    }

    private void addItemToStore() {
        String storeName = JOptionPane.showInputDialog(frame, "Enter the store name:");
        if (storeName != null && !storeName.isEmpty()) {
            Store store = seller.getStoreByName(storeName);
            if (store != null) {
                String itemName = JOptionPane.showInputDialog(frame, "Enter the item name:");
                if (itemName != null && !itemName.isEmpty()) {
                    // Gather details for the new item (price, quantity, etc.)
                    double price = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter the item price:"));
                    int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the item quantity:"));

                    Item newItem = new Item(itemName, price, quantity); // Create new Item

                    // Add the item to the store
                    store.addToStockItems(newItem, seller.getUsername(), objectMapper);
                    JOptionPane.showMessageDialog(frame, "Item added to store: " + itemName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid item name!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Store not found!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }





    private void deleteItemFromStore() {
        String storeName = JOptionPane.showInputDialog(frame, "Enter the store name:");
        if (storeName != null && !storeName.isEmpty()) {
            Store store = seller.getStoreByName(storeName);
            if (store != null) {
                String itemName = JOptionPane.showInputDialog(frame, "Enter the item name:");
                if (itemName != null && !itemName.isEmpty()) {
                    // Deleting the item from the store
                    store.deleteItem(seller.getUsername(), itemName, objectMapper);
                    JOptionPane.showMessageDialog(frame, "Item deleted from store: " + itemName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid item name!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Store not found!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private void createNewStore() {
        String storeName = JOptionPane.showInputDialog(frame, "Enter the new store name:");
        if (storeName != null && !storeName.isEmpty()) {
            // Create a new store object
            Store newStore = new Store(storeName, new HashMap<>(), new HashMap<>());
            seller.getStores().put(storeName, newStore); // Add the new store to the seller's stores

            // Perform any additional operations if needed, like updating the database

            JOptionPane.showMessageDialog(frame, "New store created: " + storeName);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private void editStore() {
        String storeName = JOptionPane.showInputDialog(frame, "Enter the store name to edit:");
        if (storeName != null && !storeName.isEmpty()) {
            Store storeToEdit = seller.getStoreByName(storeName);
            if (storeToEdit != null) {
                String newStoreName = JOptionPane.showInputDialog(frame, "Enter the new store name:");
                if (newStoreName != null && !newStoreName.isEmpty()) {
                    storeToEdit.setName(newStoreName); // Update the store name
                    seller.getStores().put(newStoreName, storeToEdit); // Update the store in the seller's stores

                    // Perform any additional operations if needed, like updating the database

                    JOptionPane.showMessageDialog(frame, "Store updated: " + newStoreName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid store name!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Store not found!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private void viewStoreSales() {
        String storeName = JOptionPane.showInputDialog(frame, "Enter the store name to view sales:");
        if (storeName != null && !storeName.isEmpty()) {
            Store store = seller.getStoreByName(storeName);
            if (store != null) {
                // Assuming you have a method to retrieve sales data for a store
                String salesData = retrieveSalesDataForStore(store);

                // Display sales data for the selected store
                JOptionPane.showMessageDialog(frame, "Sales data for " + storeName + ":\n" + salesData);
            } else {
                JOptionPane.showMessageDialog(frame, "Store not found!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private String retrieveSalesDataForStore(Store store) {
        // Simulated sales data for demonstration purposes
        StringBuilder sales = new StringBuilder();
        sales.append("Total sales: ").append(store.numProductsSold()).append("\n");
        sales.append("Sold items:\n");
        for (Map.Entry<String, Item> entry : store.getSoldItems().entrySet()) {
            sales.append(entry.getValue().getName()).append(" - Quantity: ").append(entry.getValue().getStock()).append("\n");
        }
        return sales.toString();
    }

    public static void main(String[] args) {
        Seller seller = new Seller(); // Replace with your Seller object creation logic
        ObjectMapper objectMapper = new ObjectMapper(); // Replace with your ObjectMapper creation logic

        SwingUtilities.invokeLater(() -> new SellerGUI(seller, objectMapper));
    }
}
