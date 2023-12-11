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

/**
 * GUI for the buyer and seller marketplace.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Ryan, Soham
 * @version December 8, 2023
 */

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

    public MarketplaceGUI(Marketplace marketplace, Person user, ObjectMapper objectMapper, ObjectInputStream ois,
            ObjectOutputStream oos) {
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
                        String numProducts = JOptionPane.showInputDialog(null,
                                "How many items would you like to purchase",
                                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
                        // ((Buyer) user).buyItem(selectedProduct, marketplace, objectMapper,
                        // numProducts);
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
            // SELLER GUI

            // EVERYTHING THAT THE GUI NEEDS TO DO:
            /*
             * System.out.println("\t(1) List, edit, or delete items ~");
             * System.out.println("\t(2) Create, edit, or delete stores ~");
             * System.out.println("\t(3) View listed products dashboard ~");
             * System.out.println("\t(4) View all sold products dashboard ~");
             * System.out.println("\t(5) View all product buyers dashboard ~");
             * System.out.println("\t(6) Export store items to CSV ~");
             * System.out.println("\t(7) Import store items from CSV ~");
             * System.out.println("\t(8) Edit account");
             * System.out.println("\t(9) Delete account");
             * System.out.println("\t(10) Sign-out ~");
             */

            JFrame sellerFrame = new JFrame("Seller Interface");
            sellerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sellerFrame.setSize(600, 400);
            sellerFrame.setLocationRelativeTo(null);

            JPanel sellerBottomPanel = new JPanel();
            sellerBottomPanel.setLayout(new FlowLayout());
            sellerBottomPanel.setSize(600, 300);
            sellerBottomPanel.setLocation(0, 300);

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.setSize(600, 200);
            panel.setLocation(0, 0);

            JButton addItemButton = new JButton("Add Item to Store");
            JButton deleteItemButton = new JButton("Delete Item from Store");
            JButton restockItemButton = new JButton("Restock an Item");
            JButton createNewStoreButton = new JButton("Create New Store");
            JButton editStoreButton = new JButton("Edit Store");
            JButton viewStoreSalesButton = new JButton("View Store Sales");
            JButton viewListedProductsButton = new JButton("View Listed Products");
            JButton viewSoldProductsButton = new JButton("View Sold Products");
            JButton viewProductBuyersButton = new JButton("View Product Buyers");
            JButton exportToCSVButton = new JButton("Export Store Items to CSV");
            JButton importFromCSVButton = new JButton("Import Store Items from CSV");
            JButton editAccountButton = new JButton("Edit account");
            JButton deleteAccountButton = new JButton("Delete Account");
            JButton signOutButton = new JButton("Sign out");
            JButton refreshButton = new JButton("Refresh");

            sellerFrame.add(editAccountButton);
            sellerFrame.add(deleteAccountButton);
            sellerFrame.add(signOutButton);

            ActionListener actionListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == addItemButton) {
                        marketplace.createItem((Seller) user, objectMapper, oos, ois);
                    } else if (e.getSource() == deleteItemButton) {
                        deleteItemFromStore((Seller) user, sellerFrame);
                    } else if (e.getSource() == restockItemButton) {
                        restockItem((Seller) user, sellerFrame);
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
                    } else if (e.getSource() == viewStoreSalesButton) {
                        viewStoreSales((Seller) user, sellerFrame);
                    } else if (e.getSource() == viewListedProductsButton) {
                        // TODO: get updated seller object from network
                        viewListedProducts((Seller) user, sellerFrame);
                    } else if (e.getSource() == viewSoldProductsButton) {
                        // TODO: get updated seller object from network
                        viewSoldProducts((Seller) user, sellerFrame);
                    } else if (e.getSource() == viewProductBuyersButton) {
                        // TODO: get updated seller object from network
                        viewProductBuyers((Seller) user, sellerFrame);
                    }
                    // TODO: REFRESH BUTTON
                    else if (e.getSource() == refreshButton) {
                        // TODO: get updated seller object from network
                    }
                }
            };

            addItemButton.addActionListener(actionListener);
            deleteItemButton.addActionListener(actionListener);
            restockItemButton.addActionListener(actionListener);
            createNewStoreButton.addActionListener(actionListener);
            editStoreButton.addActionListener(actionListener);
            viewStoreSalesButton.addActionListener(actionListener);
            viewSoldProductsButton.addActionListener(actionListener);
            viewListedProductsButton.addActionListener(actionListener);
            viewProductBuyersButton.addActionListener(actionListener);
            exportToCSVButton.addActionListener(actionListener);
            importFromCSVButton.addActionListener(actionListener);
            editAccountButton.addActionListener(actionListener);
            deleteAccountButton.addActionListener(actionListener);
            signOutButton.addActionListener(actionListener);
            refreshButton.addActionListener(actionListener);

            panel.add(addItemButton);
            panel.add(deleteItemButton);
            panel.add(restockItemButton);
            panel.add(createNewStoreButton);
            panel.add(editStoreButton);
            panel.add(viewStoreSalesButton);
            panel.add(viewListedProductsButton);
            panel.add(viewSoldProductsButton);
            panel.add(viewProductBuyersButton);
            panel.add(exportToCSVButton);
            panel.add(importFromCSVButton);
            panel.add(editAccountButton);
            panel.add(deleteAccountButton);
            panel.add(signOutButton);

            sellerBottomPanel.add(refreshButton);

            sellerFrame.add(sellerBottomPanel);
            sellerFrame.add(panel);
            sellerFrame.setVisible(true);
        }
    }

    private void deleteItemFromStore(Seller seller, JFrame frame) {
        Store store = storePicker(seller, frame);
        if (store.getName() != null && !store.getName().isEmpty()) {
            if (store != null) {
                String itemName = JOptionPane.showInputDialog(frame, "Enter the item name:");
                if (itemName != null && !itemName.isEmpty()) {
                    // Deleting the item from the store
                    store.deleteItem(seller.getUsername(), itemName, objectMapper);
                    JOptionPane.showMessageDialog(frame, "Item deleted from store: " + itemName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid item name!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private void restockItem(Seller seller, JFrame frame) {
        String itemName = JOptionPane.showInputDialog(frame, "Enter the item name:");
        if (itemName != null && !itemName.isEmpty()) {
            Item itemToChange = marketplace.getItemFromAllStores(itemName, seller);
            Store store = seller.getStoreByItem(itemToChange);
            if (itemToChange != null) {
                int stock;
                String stockString = JOptionPane.showInputDialog(frame, "What would you like the item stock to be?");
                if (stockString != null && stockString.chars().allMatch(Character::isDigit)) {
                    stock = Integer.parseInt(stockString);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid stock!");
                    return;
                }
                itemToChange.setStock(stock);
                String dir = "/sellers/" + seller.getUsername() + "/stores/" + store.getName() + "/stockItems";
                // JsonUtils.addObjectToJson(dir, itemName, itemToChange, objectMapper); //
                // TODO: JSON STUFF AND NETWORK IO
                JOptionPane.showMessageDialog(frame, itemName + " restocked to " + stock + " items.");
            } else {
                JOptionPane.showMessageDialog(frame, "Sorry, we can't find an item with this name.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid item name!");
        }
    }

    private void createNewStore(Seller seller, JFrame frame) {
        String storeName = JOptionPane.showInputDialog(frame, "Enter the new store name:");
        if (storeName != null && !storeName.isEmpty()) {
            // Create a new store object
            Store newStore = new Store(storeName, new HashMap<>(), new HashMap<>());
            seller.getStores().put(storeName, newStore); // Add the new store to the seller's stores

            // TODO: Network

            JOptionPane.showMessageDialog(frame, "New store created: " + storeName);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private void editStore(Seller seller, JFrame frame) {
        Store storeToEdit = storePicker(seller, frame);
        if (storeToEdit.getName() != null && !storeToEdit.getName().isEmpty()) {
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
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private void viewStoreSales(Seller seller, JFrame frame) {
        Store store = storePicker(seller, frame);

        if (store.getName() != null && !store.getName().isEmpty()) {
            if (store != null) {
                String salesData = retrieveSalesDataForStore(store);
                JOptionPane.showMessageDialog(frame, "Sales data for " + store.getName() + ":\n" + salesData);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid store name!");
        }
    }

    private Store storePicker(Seller seller, JFrame frame) {
        ArrayList<String> storeNames = new ArrayList<String>();
        ArrayList<Store> stores = new ArrayList<Store>();

        for (Map.Entry<String, Store> store : seller.getStores().entrySet()) {
            storeNames.add(store.getValue().getName());
            stores.add(store.getValue());
        }
        JComboBox<String> storeComboBox = new JComboBox<String>(storeNames.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(frame, storeComboBox, "Select a Store", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Store selectedStore = stores.get(storeComboBox.getSelectedIndex());
            return selectedStore;
        }
        return new Store();
    }

    private void viewListedProducts(Seller seller, JFrame frame) {
        ArrayList<Item> stockItems = seller.getAllStoreItems("stock");
        ArrayList<String> stockItemStrings = new ArrayList<String>();
        for (Item item : stockItems) {
            stockItemStrings.add(item.toString(marketplace));
        }
        JList<String> itemList = new JList<>(stockItemStrings.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JOptionPane.showMessageDialog(frame, scrollPane, "All listed products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewSoldProducts(Seller seller, JFrame frame) {
        ArrayList<Item> soldItems = seller.getAllStoreItems("sold");
        ArrayList<String> soldItemsStrings = new ArrayList<String>();
        for (Item item : soldItems) {
            soldItemsStrings.add(item.toString(marketplace));
        }
        JList<String> itemList = new JList<>(soldItemsStrings.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JOptionPane.showMessageDialog(frame, scrollPane, "All sold products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewProductBuyers(Seller seller, JFrame frame) {
        HashMap<String, Integer> buyers = seller.allBuyers();
        ArrayList<String> buyerStrings = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : buyers.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            buyerStrings.add("User " + key + " has bought " + value + " products from you.");
        }
        JList<String> itemList = new JList<>(buyerStrings.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JOptionPane.showMessageDialog(frame, scrollPane, "All product buyers", JOptionPane.INFORMATION_MESSAGE);
    }

    private String retrieveSalesDataForStore(Store store) {
        StringBuilder sales = new StringBuilder();
        sales.append("Total sales: ").append(store.numProductsSold()).append("\n");
        sales.append("Sold items:\n");
        for (Map.Entry<String, Item> entry : store.getSoldItems().entrySet()) {
            sales.append(entry.getValue().getName()).append(" - Quantity: ").append(entry.getValue().getCount())
                    .append("\n");
        }
        if (store.getSoldItems().entrySet().size() == 0) {
            sales.append("None.");
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
