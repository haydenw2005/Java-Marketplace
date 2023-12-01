import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MarketplaceGUI extends JComponent implements Runnable {
    private Marketplace marketplace;
    private Person user;

    JButton storeInfoButton;
    JButton marketplaceButton;
    JButton cartButton;
    JButton purchaseHistoryButton;
    JButton exportButton;
    JButton editAccountButton;
    JButton deleteAccountButton;
    JButton signOutButton;

    public MarketplaceGUI (Marketplace marketplace, Person user) {
        this.marketplace = marketplace;
        this.user = user;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == storeInfoButton) {

            } else if (e.getSource() == marketplaceButton) {

            }
        }
    };

    public void run() {
        if (user instanceof Buyer) {
            JFrame frame = new JFrame();
            frame.setTitle("zBay Marketplace");

            Container content = frame.getContentPane();

            content.setLayout(new BorderLayout());
            content.add(this, BorderLayout.CENTER);

            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout());
            frame.setVisible(true);

            storeInfoButton = new JButton("View store information");
            storeInfoButton.addActionListener(actionListener);
            marketplaceButton = new JButton("View marketplace");
            marketplaceButton.addActionListener(actionListener);
            cartButton = new JButton("View cart");
            cartButton.addActionListener(actionListener);
            purchaseHistoryButton = new JButton("View purchase history");
            purchaseHistoryButton.addActionListener(actionListener);
            exportButton = new JButton("Export purchase history to CSV");
            exportButton.addActionListener(actionListener);
            editAccountButton = new JButton("Edit account");
            editAccountButton.addActionListener(actionListener);
            deleteAccountButton = new JButton("Delete Account");
            deleteAccountButton.addActionListener(actionListener);
            signOutButton = new JButton("Sign out");
            signOutButton.addActionListener(actionListener);

            JPanel panel = new JPanel();
            panel.add(storeInfoButton);
            panel.add(marketplaceButton);
            panel.add(cartButton);
            panel.add(purchaseHistoryButton);
            panel.add(exportButton);
            panel.add(editAccountButton);
            panel.add(deleteAccountButton);
            panel.add(signOutButton);
            content.add(panel, BorderLayout.NORTH);
        } else {

        }
    }
}
