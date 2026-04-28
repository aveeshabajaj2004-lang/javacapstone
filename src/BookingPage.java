import javax.swing.*;
import java.sql.*;

public class BookingPage extends JFrame {

    JTextField nameField, phoneField, daysField;
    int vehicleId;
    String vehicleName;
    double rentPerDay;

    BookingPage(int vehicleId, String vehicleName, double rentPerDay) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.rentPerDay = rentPerDay;

        setTitle("Booking Page");

        JLabel vehicleLabel = new JLabel("Selected Vehicle: " + vehicleName);
        vehicleLabel.setBounds(50, 30, 300, 30);
        add(vehicleLabel);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(50, 80, 130, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 80, 180, 30);
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 130, 130, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(200, 130, 180, 30);
        add(phoneField);

        JLabel daysLabel = new JLabel("No. of Days:");
        daysLabel.setBounds(50, 180, 130, 30);
        add(daysLabel);

        daysField = new JTextField();
        daysField.setBounds(200, 180, 180, 30);
        add(daysField);

        JButton paymentBtn = new JButton("Proceed to Payment");
        paymentBtn.setBounds(130, 240, 180, 35);
        add(paymentBtn);

        paymentBtn.addActionListener(e -> proceedPayment());

        setLayout(null);
        setSize(450, 350);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void proceedPayment() {
        try {
            String name = nameField.getText();
            String phone = phoneField.getText();
            int days = Integer.parseInt(daysField.getText());

            double total = days * rentPerDay;

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO bookings(customer_name, phone, vehicle_id, days, total_amount, booking_status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setInt(3, vehicleId);
            ps.setInt(4, days);
            ps.setDouble(5, total);
            ps.setString(6, "Pending Payment");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int bookingId = 0;

            if (rs.next()) {
                bookingId = rs.getInt(1);
            }

            new PaymentPage(bookingId, vehicleId, total);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid details");
            ex.printStackTrace();
        }
    }
}
