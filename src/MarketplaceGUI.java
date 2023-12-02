import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarketplaceGUI extends JComponent implements Runnable {
    private Marketplace marketplace;
    private Person user;
    private ObjectMapper objectMapper;
    private Item selectedProduct;

    public MarketplaceGUI (Marketplace marketplace, Person user, ObjectMapper objectMapper) {
        this.marketplace = marketplace;
        this.user = user;
        this.objectMapper = objectMapper;
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
            JButton refreshButton = new JButton("Refresh");

            ArrayList<Item> itemsList = marketplace.getAllMarketPlaceItems();
            Item[] itemsArray = new Item[itemsList.size()];
            for (int i = 0; i < itemsList.size(); i++)
                itemsArray[i] = itemsList.get(i);

            JComboBox productsComboBox = new JComboBox(itemsArray);

            JLabel productsLabel = new JLabel("Select a product to view its information:");
            JButton selectButton = new JButton("Select");

            marketplaceBottomPanel.add(searchButton);
            marketplaceBottomPanel.add(searchText);
            marketplaceBottomPanel.add(sortPriceButton);
            marketplaceBottomPanel.add(sortQuantityButton);
            marketplaceBottomPanel.add(backToHomeButton);
            marketplaceBottomPanel.add(refreshButton);

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

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == storeInfoButton) {

                    } else if (e.getSource() == marketplaceButton) {
                        homeFrame.setVisible(false);
                        marketplaceFrame.setVisible(true);
                    } else if (e.getSource() == cartButton) {

                    }  else if (e.getSource() == purchaseHistoryButton) {

                    } else if (e.getSource() == exportButton) {

                    } else if (e.getSource() == editAccountButton) {

                    } else if (e.getSource() == deleteAccountButton) {

                    } else if (e.getSource() == signOutButton) {

                    } else if (e.getSource() == searchButton) {
                        productsComboBox.removeAllItems();
                        for (Item item : marketplace.searchProducts(searchText.getText(), itemsList))
                            productsComboBox.addItem(item);
                    } else if (e.getSource() == sortPriceButton) {
                        productsComboBox.removeAllItems();
                        for (Item item : marketplace.sortByPrice(itemsArray))
                            productsComboBox.addItem(item);
                    } else if (e.getSource() == sortQuantityButton) {
                        productsComboBox.removeAllItems();
                        for (Item item : marketplace.sortByQuantity(itemsArray))
                            productsComboBox.addItem(item);
                    } else if (e.getSource() == backToHomeButton) {
                        marketplaceFrame.setVisible(false);
                        homeFrame.setVisible(true);
                    } else if (e.getSource() == refreshButton) {
                        productsComboBox.removeAllItems();
                        for (Item item : marketplace.getAllMarketPlaceItems())
                            productsComboBox.addItem(item);
                    } else if (e.getSource() == selectButton) {
                        selectedProduct = (Item) productsComboBox.getSelectedItem();
                        marketplaceFrame.setVisible(false);
                        productLabel.setText("Product: " + selectedProduct.getName());
                        descriptionLabel.setText("Description: " + selectedProduct.getDescription());
                        quantityLabel.setText("Quantity available: " + selectedProduct.getStock());
                        productPageFrame.setVisible(true);
                    } else if (e.getSource() == purchaseButton) {
                        String numProducts = JOptionPane.showInputDialog(null, "How many items would you like to purchase",
                                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
                        ((Buyer) user).buyItem(selectedProduct, marketplace, objectMapper, numProducts);
                    } else if (e.getSource() == addToCartButton) {
                        ((Buyer) user).addItemToCart(selectedProduct, objectMapper);
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
            refreshButton.addActionListener(actionListener);
            productsComboBox.addActionListener(actionListener);
            selectButton.addActionListener(actionListener);

            purchaseButton.addActionListener(actionListener);
            addToCartButton.addActionListener(actionListener);
            backToMarketplaceButton.addActionListener(actionListener);

        } else {

        }
    }
}
