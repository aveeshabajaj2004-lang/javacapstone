import javax.swing.*;
import java.awt.*;

public class StartPage extends JFrame {

    StartPage() {
        setTitle("RideEase Rentals");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(238, 246, 255));
        add(panel);

        JLabel title = new JLabel("RideEase Rentals");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(new Color(25, 84, 166));
        title.setBounds(240, 45, 350, 45);
        panel.add(title);

        JLabel subtitle = new JLabel("Your ride, your way.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        subtitle.setForeground(new Color(70, 70, 70));
        subtitle.setBounds(300, 90, 250, 30);
        panel.add(subtitle);

        JPanel twoCard = createCard("2 Wheeler", "Bikes and scooters for quick city rides", 90, 160);
        JPanel fourCard = createCard("4 Wheeler", "Cars and SUVs for comfortable trips", 400, 160);

        panel.add(twoCard);
        panel.add(fourCard);

        JButton twoBtn = new JButton("Book 2 Wheeler");
        twoBtn.setBounds(135, 330, 200, 45);
        styleButton(twoBtn);
        panel.add(twoBtn);

        JButton fourBtn = new JButton("Book 4 Wheeler");
        fourBtn.setBounds(445, 330, 200, 45);
        styleButton(fourBtn);
        panel.add(fourBtn);

        twoBtn.addActionListener(e -> {
            new VehicleListPage("2 Wheeler");
            dispose();
        });

        fourBtn.addActionListener(e -> {
            new VehicleListPage("4 Wheeler");
            dispose();
        });

        setVisible(true);
    }

    JPanel createCard(String heading, String description, int x, int y) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(x, y, 260, 140);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(190, 210, 230), 2));

        JLabel h = new JLabel(heading);
        h.setFont(new Font("Segoe UI", Font.BOLD, 24));
        h.setForeground(new Color(25, 84, 166));
        h.setBounds(55, 25, 200, 35);
        card.add(h);

        JLabel d = new JLabel("<html><center>" + description + "</center></html>");
        d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        d.setForeground(new Color(80, 80, 80));
        d.setBounds(35, 70, 200, 45);
        card.add(d);

        return card;
    }

    void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(new Color(25, 84, 166));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
    }

    public static void main(String[] args) {
        new StartPage();
    }
}