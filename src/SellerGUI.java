import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fasterxml.jackson.databind.ObjectMapper;


public class SellerGUI extends JFrame implements ActionListener {
    private Seller seller;
    private ObjectMapper objectMapper;
    private JButton addItemButton;
    private JButton deleteItemButton;
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

        JPanel panel = new JPanel();
        panel.add(addItemButton);
        panel.add(deleteItemButton);

        frame.add(panel);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addItemButton) {
            addItemToStore();
        } else if (e.getSource() == deleteItemButton) {
            deleteItemFromStore();
        }
    }

    private void addItemToStore() {
        String storeName = JOptionPane.showInputDialog(frame, "Enter the store name:");
        if (storeName != null && !storeName.isEmpty()) {
            Store store = seller.getStoreByName(storeName);
            if (store != null) {
                String itemName = JOptionPane.showInputDialog(frame, "Enter the item name:");
                if (itemName != null && !itemName.isEmpty()) {
                    // Here, add logic to gather details for the new item (price, quantity, etc.)
                    Item newItem = new Item(); // Example: Item creation

                    // Adding the item to the store
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
    // public static void main(String[] args) {
    //     // Assuming you have the necessary objects instantiated (like ObjectMapper and Seller)
    //     Seller seller = new Seller(); // Replace with your Seller object creation logic
    //     ObjectMapper objectMapper = new ObjectMapper(); // Replace with your ObjectMapper creation logic

    //     SwingUtilities.invokeLater(() -> new SellerGUI(seller, objectMapper));
    // }
}
