import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarketplaceGUI extends JComponent implements Runnable {
    private Marketplace marketplace;
    private Person user;
    private ObjectMapper objectMapper;
    private ArrayList<Item> itemsList;
    private Item selectedProduct;
    private JList storeInfoList = new JList();
    private JList purchaseHistoryList = new JList();
    private JList cartList = new JList();
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public MarketplaceGUI (Marketplace marketplace, Person user, ObjectMapper objectMapper, ObjectInputStream ois, ObjectOutputStream oos) {
        this.marketplace = marketplace;
        this.user = user;
        this.objectMapper = objectMapper;
        itemsList = marketplace.getAllMarketPlaceItems();
        this.ois = ois;
        this.oos = oos;
    }



    public void run() {
        if (user instanceof Buyer) {
            JFrame homeFrame = new JFrame();
            homeFrame.setTitle("Home");
            homeFrame.setSize(600, 400);
            homeFrame.setLocationRelativeTo(null);
            homeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            homeFrame.setVisible(true);

            JButton storeInfoButton = new JButton("View store information");
            JButton marketplaceButton = new JButton("View marketplace");
            JButton cartButton = new JButton("View cart");
            JButton purchaseHistoryButton = new JButton("View purchase history");
            JButton exportButton = new JButton("Export purchase history to CSV");
            JButton editAccountButton = new JButton("Edit account");
            JButton deleteAccountButton = new JButton("Delete Account");
            JButton signOutButton = new JButton("Sign out");

            JPanel homePanel = new JPanel();
            homePanel.setLayout(new FlowLayout());

            homePanel.add(storeInfoButton);
            homePanel.add(marketplaceButton);
            homePanel.add(cartButton);
            homePanel.add(purchaseHistoryButton);
            homePanel.add(exportButton);
            homePanel.add(editAccountButton);
            homePanel.add(deleteAccountButton);
            homePanel.add(signOutButton);

            homeFrame.add(homePanel, BorderLayout.CENTER);
/////////////////////////////////////////////////////////////////////
            JFrame marketplaceFrame = new JFrame();
            marketplaceFrame.setTitle("zBay Marketplace");
            marketplaceFrame.setSize(600, 400);
            marketplaceFrame.setLocationRelativeTo(null);
            marketplaceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            marketplaceFrame.setVisible(false);

            JPanel marketplaceBottomPanel = new JPanel();
            marketplaceBottomPanel.setLayout(new FlowLayout());
            marketplaceBottomPanel.setSize(600, 200);
            marketplaceBottomPanel.setLocation(0, 200);

            JPanel marketplaceTopPanel = new JPanel();
            marketplaceTopPanel.setLayout(new FlowLayout());
            marketplaceTopPanel.setSize(600, 200);
            marketplaceTopPanel.setLocation(0, 0);


            JButton searchButton = new JButton("Search");
            JTextField searchText = new JTextField("", 10);
            JButton sortPriceButton = new JButton("Sort by price");
            JButton sortQuantityButton = new JButton("Sort by quantity");
            JButton backToHomeButton = new JButton("Back");
            JButton refreshMarketplaceButton = new JButton("Refresh");


            JComboBox productsComboBox = new JComboBox();
            for (Item item : itemsList)
                productsComboBox.addItem(item.toString(marketplace));

            JLabel productsLabel = new JLabel("Select a product to view its information:");
            JButton selectButton = new JButton("Select");

            marketplaceBottomPanel.add(searchButton);
            marketplaceBottomPanel.add(searchText);
            marketplaceBottomPanel.add(sortPriceButton);
            marketplaceBottomPanel.add(sortQuantityButton);

            marketplaceBottomPanel.add(refreshMarketplaceButton);

            marketplaceTopPanel.add(productsLabel);
            marketplaceTopPanel.add(productsComboBox);
            marketplaceTopPanel.add(selectButton);

            marketplaceFrame.add(marketplaceBottomPanel);
            marketplaceFrame.add(marketplaceTopPanel);
/////////////////////////////////////////////////////////////////////
            JFrame productPageFrame = new JFrame();
            productPageFrame.setTitle("zBay Marketplace");
            productPageFrame.setSize(600, 400);
            productPageFrame.setLocationRelativeTo(null);
            productPageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            productPageFrame.setVisible(false);

            JPanel productPagePanel = new JPanel();
            productPagePanel.setLayout(new BoxLayout(productPagePanel, BoxLayout.Y_AXIS));

            JLabel productLabel = new JLabel();
            JLabel descriptionLabel = new JLabel();
            JLabel quantityLabel = new JLabel();
            JButton purchaseButton = new JButton("Purchase");
            JButton addToCartButton = new JButton("Add to cart");
            JButton backToMarketplaceButton = new JButton("Back");

            productPagePanel.add(productLabel);
            productPagePanel.add(descriptionLabel);
            productPagePanel.add(quantityLabel);
            productPagePanel.add(purchaseButton);
            productPagePanel.add(addToCartButton);
            productPagePanel.add(backToMarketplaceButton);

            productPageFrame.add(productPagePanel, BorderLayout.CENTER);
/////////////////////////////////////////////////////////////////////
            JFrame storeInfoFrame = new JFrame();
            storeInfoFrame.setTitle("zBay Marketplace");
            storeInfoFrame.setSize(600, 400);
            storeInfoFrame.setLocationRelativeTo(null);
            storeInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            storeInfoFrame.setVisible(false);

            JPanel storeInfoTopPanel = new JPanel();
            storeInfoTopPanel.setLayout(new FlowLayout());

            JLabel listStoresLabel = new JLabel("View stores by: ");
            JButton productsSoldButton = new JButton("Number of products sold");
            JButton productsPurchasedButton = new JButton("Products you purchased");
            JButton sortByProductsSoldButton = new JButton("Sort by number of products sold");
            JButton sortByProductsBoughtButton = new JButton("Sort by number of products bought");
            sortByProductsBoughtButton.setVisible(false);
            JButton refreshStoreInfoButton = new JButton("Refresh");

            storeInfoTopPanel.add(listStoresLabel);
            storeInfoTopPanel.add(productsSoldButton);
            storeInfoTopPanel.add(productsPurchasedButton);
            storeInfoTopPanel.add(sortByProductsSoldButton);
            storeInfoTopPanel.add(sortByProductsBoughtButton);
            storeInfoTopPanel.add(refreshStoreInfoButton);

            JPanel storeInfoBottomPanel = new JPanel();
            storeInfoBottomPanel.setLayout(new FlowLayout());

            updateStoreInfoList(true, false);
            storeInfoBottomPanel.add(storeInfoList);

            storeInfoFrame.add(storeInfoTopPanel);
            storeInfoFrame.add(storeInfoBottomPanel, BorderLayout.SOUTH);
/////////////////////////////////////////////////////////////////////
            JFrame purchaseHistoryFrame = new JFrame();
            purchaseHistoryFrame.setTitle("zBay Marketplace");
            purchaseHistoryFrame.setSize(600, 400);
            purchaseHistoryFrame.setLocationRelativeTo(null);
            purchaseHistoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            purchaseHistoryFrame.setVisible(false);

            JPanel purchaseHistoryPanel = new JPanel();
            purchaseHistoryPanel.setLayout(new FlowLayout());

            JLabel purchaseHistoryLabel = new JLabel("Products you've purchased:");
            JButton refreshPurchaseHistoryButton = new JButton("Refresh");
            updatePurchaseHistoryList();

            purchaseHistoryPanel.add(purchaseHistoryLabel);
            purchaseHistoryPanel.add(purchaseHistoryList);
            purchaseHistoryPanel.add(refreshPurchaseHistoryButton);

            purchaseHistoryFrame.add(purchaseHistoryPanel);
/////////////////////////////////////////////////////////////////////
            JFrame cartFrame = new JFrame();
            cartFrame.setTitle("zBay Marketplace");
            cartFrame.setSize(600, 400);
            cartFrame.setLocationRelativeTo(null);
            cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            cartFrame.setVisible(false);

            JPanel cartPanel = new JPanel();
            cartPanel.setLayout(new FlowLayout());

            updateCartList();
            JButton buyCartButton = new JButton("Buy cart");

            cartPanel.add(cartList);
            cartPanel.add(buyCartButton);

            cartFrame.add(cartPanel);


            ActionListener actionListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == storeInfoButton) {
                        storeInfoTopPanel.add(backToHomeButton);
                        homeFrame.setVisible(false);
                        storeInfoFrame.setVisible(true);
                    } else if (e.getSource() == productsSoldButton) {
                        sortByProductsBoughtButton.setVisible(false);
                        sortByProductsSoldButton.setVisible(true);
                        updateStoreInfoList(true, false);
                    } else if (e.getSource() == productsPurchasedButton) {
                        sortByProductsSoldButton.setVisible(false);
                        sortByProductsBoughtButton.setVisible(true);
                        updateStoreInfoList(false, false);
                    } else if (e.getSource() == sortByProductsSoldButton) {
                        updateStoreInfoList(true, true);
                    } else if (e.getSource() == sortByProductsBoughtButton) {
                        updateStoreInfoList(false, true);
                    } else if (e.getSource() == refreshStoreInfoButton) {
                        updateStoreInfoList(true, false);
                    } else if (e.getSource() == marketplaceButton) {
                        marketplaceBottomPanel.add(backToHomeButton);
                        homeFrame.setVisible(false);
                        marketplaceFrame.setVisible(true);
                    } else if (e.getSource() == cartButton) {
                        updateCartList();
                        cartPanel.add(backToHomeButton);
                        homeFrame.setVisible(false);
                        cartFrame.setVisible(true);
                    } else if (e.getSource() == buyCartButton) {
                        try {
                            oos.writeObject("buyCart");
                            oos.flush();
                            marketplace = (Marketplace) ois.readObject();
                            user = (Buyer) ois.readObject();
                        } catch (IOException | ClassNotFoundException e1) {
                            JOptionPane.showMessageDialog(null,
                                    "There was an error buying cart.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (e.getSource() == purchaseHistoryButton) {
                        purchaseHistoryPanel.add(backToHomeButton);
                        homeFrame.setVisible(false);
                        purchaseHistoryFrame.setVisible(true);
                    } else if (e.getSource() == refreshPurchaseHistoryButton) {
                        updatePurchaseHistoryList();
                    } else if (e.getSource() == exportButton) {
                        String file = JOptionPane.showInputDialog(null, 
                                "Enter filename to export to (excluding .csv extension)",
                                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
                        // get latest buyer object from server
                        try {
                            oos.writeObject("updateBuyer");
                            oos.flush();
                            user = (Person) ois.readObject();
                            CsvUtils.writePurchaseHistoryToCSV(file, (Buyer) user);
                        } catch (IOException | ClassNotFoundException e1) {
                            JOptionPane.showMessageDialog(null,
                                    "There was an error writing to csv.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } else if (e.getSource() == editAccountButton) {
                        marketplace.editUser(user, objectMapper, oos, ois);
                    } else if (e.getSource() == deleteAccountButton) {
                        marketplace.deleteUser(user, objectMapper, oos, ois); 
                        homeFrame.dispose();
                        System.exit(0);
                    } else if (e.getSource() == signOutButton) {
                        homeFrame.dispose();
                        System.exit(0);
                    } else if (e.getSource() == searchButton) {
                        productsComboBox.removeAllItems();
                        for (Item item : marketplace.searchProducts(searchText.getText(), itemsList))
                            productsComboBox.addItem(item.toString(marketplace));
                    } else if (e.getSource() == sortPriceButton) {
                        productsComboBox.removeAllItems();
                        for (Item item : marketplace.sortByPrice(itemsList))
                            productsComboBox.addItem(item.toString(marketplace));
                    } else if (e.getSource() == sortQuantityButton) {
                        productsComboBox.removeAllItems();
                        for (Item item : marketplace.sortByQuantity(itemsList))
                            productsComboBox.addItem(item.toString(marketplace));
                    } else if (e.getSource() == backToHomeButton) {
                        marketplaceFrame.setVisible(false);
                        storeInfoFrame.setVisible(false);
                        purchaseHistoryFrame.setVisible(false);
                        cartFrame.setVisible(false);
                        homeFrame.setVisible(true);
                    } else if (e.getSource() == refreshMarketplaceButton) {
                        searchText.setText("");
                        productsComboBox.removeAllItems();
                        itemsList = marketplace.getAllMarketPlaceItems();
                        try {
                            oos.writeObject("updateMarketplace");
                            oos.flush();
                            marketplace = (Marketplace) ois.readObject();
                        } catch (IOException | ClassNotFoundException e1) {
                            JOptionPane.showMessageDialog(null,
                                    "There was an error.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        for (Item item : itemsList)
                            productsComboBox.addItem(item.toString(marketplace));
                    } else if (e.getSource() == selectButton) {
                        selectedProduct = getItem((String) productsComboBox.getSelectedItem());
                        marketplaceFrame.setVisible(false);
                        productLabel.setText("Product: " + selectedProduct.getName());
                        descriptionLabel.setText("Description: " + selectedProduct.getDescription());
                        quantityLabel.setText("Quantity available: " + selectedProduct.getStock());
                        productPageFrame.setVisible(true);
                    } else if (e.getSource() == purchaseButton) {
                        String numProducts = JOptionPane.showInputDialog(null, "How many items would you like to purchase",
                                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
                        // ((Buyer) user).buyItem(selectedProduct, marketplace, objectMapper, numProducts);
                        try {
                            oos.writeObject("buyItem");
                            oos.writeObject(selectedProduct);
                            oos.writeObject(numProducts);
                            oos.flush();

                            // fetch updates
                            marketplace.updateMarketPlace((Marketplace) ois.readObject());
                        } catch (IOException | ClassNotFoundException except) {
                            JOptionPane.showMessageDialog(null,
                                    "There was an error pruchasing item.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (e.getSource() == addToCartButton) {
                        // ((Buyer) user).addItemToCart(selectedProduct, objectMapper);
                        try {
                            oos.writeObject("addToCart");
                            oos.writeObject(selectedProduct);
                            oos.flush();

                            // fetch updates
                            marketplace.updateMarketPlace((Marketplace) ois.readObject());
                        } catch (IOException | ClassNotFoundException except) {
                            JOptionPane.showMessageDialog(null,
                                    "There was an error adding to cart.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (e.getSource() == backToMarketplaceButton) {
                        productPageFrame.setVisible(false);
                        marketplaceFrame.setVisible(true);
                    }
                }
            };

            storeInfoButton.addActionListener(actionListener);
            marketplaceButton.addActionListener(actionListener);
            cartButton.addActionListener(actionListener);
            purchaseHistoryButton.addActionListener(actionListener);
            exportButton.addActionListener(actionListener);
            editAccountButton.addActionListener(actionListener);
            deleteAccountButton.addActionListener(actionListener);
            signOutButton.addActionListener(actionListener);

            searchButton.addActionListener(actionListener);
            sortPriceButton.addActionListener(actionListener);
            sortQuantityButton.addActionListener(actionListener);
            backToHomeButton.addActionListener(actionListener);
            refreshMarketplaceButton.addActionListener(actionListener);
            productsComboBox.addActionListener(actionListener);
            selectButton.addActionListener(actionListener);

            purchaseButton.addActionListener(actionListener);
            addToCartButton.addActionListener(actionListener);
            backToMarketplaceButton.addActionListener(actionListener);

            productsSoldButton.addActionListener(actionListener);
            productsPurchasedButton.addActionListener(actionListener);
            sortByProductsSoldButton.addActionListener(actionListener);
            sortByProductsBoughtButton.addActionListener(actionListener);
            refreshStoreInfoButton.addActionListener(actionListener);

            refreshPurchaseHistoryButton.addActionListener(actionListener);

            buyCartButton.addActionListener(actionListener);

        } else {

            //HERE IS THE INCOMPLETE GUI LOGIC. SOME BUTTONS ARE THERE BUT NOT FULL FUNCTIONAL

            //EVERYTHING THAT THE GUI NEEDS TO DO:
            /*
            System.out.println("\t(1) List, edit, or delete items ~");
            System.out.println("\t(2) Create, edit, or delete stores ~");
            System.out.println("\t(3) View listed products dashboard ~");
            System.out.println("\t(4) View all sold products dashboard ~");
            System.out.println("\t(5) View all product buyers dashboard ~");
            System.out.println("\t(6) Edit account");
            System.out.println("\t(7) Delete account");
            System.out.println("\t(8) Sign-out ~");
            */

            JFrame sellerFrame = new JFrame("Seller Interface");
            sellerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sellerFrame.setSize(600, 400);
            sellerFrame.setLocationRelativeTo(null);

            JButton addItemButton = new JButton("Add Item to Store");


            JButton deleteItemButton = new JButton("Delete Item from Store");


            JButton createNewStoreButton = new JButton("Create New Store");


            JButton editStoreButton = new JButton("Edit Store");

            JButton viewStoreSalesButton = new JButton("View Store Sales"); // Initialize the new button

            JButton editAccountButton = new JButton("Edit account");
            JButton deleteAccountButton = new JButton("Delete Account");
            JButton signOutButton = new JButton("Sign out");

            sellerFrame.add(editAccountButton);
            sellerFrame.add(deleteAccountButton);
            sellerFrame.add(signOutButton);

            ActionListener actionListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // Existing functionality remains the same
                    if (e.getSource() == addItemButton) {
                        marketplace.createItem((Seller) user, objectMapper, oos, ois);
                    } else if (e.getSource() == deleteItemButton) {
                        deleteItemFromStore((Seller) user, sellerFrame);
                    } else if (e.getSource() == createNewStoreButton) {
                        createNewStore((Seller) user, sellerFrame);
                    } else if (e.getSource() == editStoreButton) {
                        editStore((Seller) user, sellerFrame);
                    } else if (e.getSource() == editAccountButton) {
                        marketplace.editUser(user, objectMapper, oos, ois);
                    } else if (e.getSource() == deleteAccountButton) {
                        marketplace.deleteUser(user, objectMapper, oos, ois);
                        sellerFrame.dispose();
                        System.exit(0);
                    } else if (e.getSource() == signOutButton) {
                        sellerFrame.dispose();
                        System.exit(0);
                    }

                    // Adding functionality for the new button
                    else if (e.getSource() == viewStoreSalesButton) {
                        viewStoreSales((Seller) user, sellerFrame); // Call the method to view store sales
                    }
                }
            };
            addItemButton.addActionListener(actionListener);
            deleteItemButton.addActionListener(actionListener);
            createNewStoreButton.addActionListener(actionListener);
            editStoreButton.addActionListener(actionListener);
            viewStoreSalesButton.addActionListener(actionListener); // Add action listener for the new button
            editAccountButton.addActionListener(actionListener);
            deleteAccountButton.addActionListener(actionListener);
            signOutButton.addActionListener(actionListener);

            JPanel panel = new JPanel();
            panel.add(addItemButton);
            panel.add(deleteItemButton);
            panel.add(createNewStoreButton);
            panel.add(editStoreButton);
            panel.add(viewStoreSalesButton); // Add the new button to the panel
            panel.add(editAccountButton);
            panel.add(deleteAccountButton);
            panel.add(signOutButton);

            sellerFrame.add(panel);
            sellerFrame.setVisible(true);
        }
    }

    private void deleteItemFromStore(Seller seller, JFrame frame) {
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

    private void createNewStore(Seller seller, JFrame frame) {
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

    private void editStore(Seller seller, JFrame frame) {
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

    private void viewStoreSales(Seller seller, JFrame frame) {
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

    private Item getItem(String itemToString) {
        for (int i = 0; i < itemsList.size(); i++) {
            if (itemToString.equals(itemsList.get(i).toString(marketplace)))
                return itemsList.get(i);
        }
        return null;
    }

    private void updateStoreInfoList(boolean byProductsSold, boolean sorted) {
        ArrayList<Store> allStores = marketplace.getAllStores();
        DefaultListModel listModel = new DefaultListModel();
        if (byProductsSold) {
            if (sorted) {
                allStores = marketplace.sortByProductsSold(allStores);
            }
            for (int i = 0; i < allStores.size(); i++) {
                listModel.addElement(allStores.get(i).getName() + " | Products sold: " +
                        allStores.get(i).numProductsSold());
            }
        } else {
            if (sorted) {
                allStores = marketplace.sortByProductsBought(allStores, (Buyer) user);
            }

            ArrayList<ArrayList<Item>> list = new ArrayList<ArrayList<Item>>();
            for (int i = 0; i < allStores.size(); i++) {
                list.add(allStores.get(i).getProductsPurchasedFromStore(marketplace, (Buyer) user));
            }

            for (int i = 0; i < allStores.size(); i++) {
                String s = null;
                if (list.get(i).size() > 0) {
                    s = "From " + allStores.get(i).getName() + ", you purchased: ";
                    for (int j = 0; j < list.get(i).size() - 1; j++) {
                        s += list.get(i).get(j).getCount() + " " +
                                list.get(i).get(j).getName() + ", ";
                    }
                    s += list.get(i).get(list.get(i).size() - 1).getCount() + " " +
                            list.get(i).get(list.get(i).size() - 1).getName();

                } else {
                    s = "From " + allStores.get(i).getName() + ", you purchased: nothing";
                }
                listModel.addElement(s);
            }
        }

        storeInfoList.setModel(listModel);
    }

    private void updatePurchaseHistoryList() {
        DefaultListModel listModel = new DefaultListModel();
        ArrayList<String> purchaseHistory = ((Buyer) user).getPurchaseHistory(marketplace);
        for (String item : purchaseHistory)
            listModel.addElement(item);

        purchaseHistoryList.setModel(listModel);
    }

    private void updateCartList() {
        DefaultListModel listModel = new DefaultListModel();
        try {
            oos.writeObject("updateBuyer");
            oos.flush();
            user = (Buyer) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> cart = ((Buyer) user).cartToStringList(marketplace); // TODO: Link to network
        for (String item : cart)
            listModel.addElement(item);

        cartList.setModel(listModel);
    }
}
