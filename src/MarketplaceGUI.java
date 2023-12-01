import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MarketplaceGUI extends JComponent implements Runnable {
    private Marketplace marketplace;
    private Person user;

    //Home page
    JButton storeInfoButton;
    JButton marketplaceButton;
    JButton cartButton;
    JButton purchaseHistoryButton;
    JButton exportButton;
    JButton editAccountButton;
    JButton deleteAccountButton;
    JButton signOutButton;

    //Marketplace
    JButton searchButton;
    JTextField searchText;
    JButton sortPriceButton;
    JButton sortQuantityButton;
    JButton backButton;
    JButton refreshButton;

    public MarketplaceGUI (Marketplace marketplace, Person user) {
        this.marketplace = marketplace;
        this.user = user;
    }



    public void run() {

        if (user instanceof Buyer) {
            JFrame homeFrame = new JFrame();
            homeFrame.setTitle("Home");

            homeFrame.setSize(600, 400);
            homeFrame.setLocationRelativeTo(null);
            homeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            homeFrame.setVisible(true);

            JFrame marketplaceFrame = new JFrame();
            marketplaceFrame.setTitle("zBay Marketplace");

            marketplaceFrame.setSize(600, 400);
            marketplaceFrame.setLocationRelativeTo(null);
            marketplaceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            marketplaceFrame.setVisible(false);

            storeInfoButton = new JButton("View store information");
            marketplaceButton = new JButton("View marketplace");
            cartButton = new JButton("View cart");
            purchaseHistoryButton = new JButton("View purchase history");
            exportButton = new JButton("Export purchase history to CSV");
            editAccountButton = new JButton("Edit account");
            deleteAccountButton = new JButton("Delete Account");
            signOutButton = new JButton("Sign out");

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

            JPanel marketplacePanel = new JPanel();
            marketplacePanel.setLayout(new FlowLayout());

            searchButton = new JButton("Search");
            searchText = new JTextField("", 10);
            sortPriceButton = new JButton("Sort by price");
            sortQuantityButton = new JButton("Sort by quantity");
            backButton = new JButton("Back");
            refreshButton = new JButton("Refresh");

            marketplacePanel.add(searchButton);
            marketplacePanel.add(searchText);
            marketplacePanel.add(sortPriceButton);
            marketplacePanel.add(sortQuantityButton);
            marketplacePanel.add(backButton);
            marketplacePanel.add(refreshButton);
            marketplaceFrame.add(marketplacePanel);

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

                    } else if (e.getSource() == sortPriceButton) {

                    } else if (e.getSource() == sortQuantityButton) {

                    } else if (e.getSource() == backButton) {
                        marketplaceFrame.setVisible(false);
                        homeFrame.setVisible(true);
                    } else if (e.getSource() == refreshButton) {

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
            backButton.addActionListener(actionListener);
            refreshButton.addActionListener(actionListener);

        } else {

        }
    }
}
