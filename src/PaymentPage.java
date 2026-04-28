import javax.swing.*;
import java.sql.*;

public class PaymentPage extends JFrame {

    int bookingId, vehicleId;
    double amount;

    PaymentPage(int bookingId, int vehicleId, double amount) {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.amount = amount;

        setTitle("Payment Page");

        JLabel amountLabel = new JLabel("Total Amount: Rs. " + amount);
        amountLabel.setBounds(100, 50, 250, 30);
        add(amountLabel);

        JLabel modeLabel = new JLabel("Payment Mode:");
        modeLabel.setBounds(70, 110, 120, 30);
        add(modeLabel);

        String[] modes = {"UPI", "Cash", "Card"};
        JComboBox<String> modeBox = new JComboBox<>(modes);
        modeBox.setBounds(200, 110, 150, 30);
        add(modeBox);

        JButton payBtn = new JButton("Pay Now");
        payBtn.setBounds(140, 180, 120, 35);
        add(payBtn);

        payBtn.addActionListener(e -> {
            String mode = modeBox.getSelectedItem().toString();
            makePayment(mode);
        });

        setLayout(null);
        setSize(420, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void makePayment(String mode) {
        try {
            Connection con = DBConnection.getConnection();

            String paySql = "INSERT INTO payments(booking_id, amount, payment_mode, payment_status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(paySql);

            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.setString(3, mode);
            ps.setString(4, "Paid");

            ps.executeUpdate();

            String bookingSql = "UPDATE bookings SET booking_status='Confirmed' WHERE booking_id=?";
            PreparedStatement ps2 = con.prepareStatement(bookingSql);
            ps2.setInt(1, bookingId);
            ps2.executeUpdate();

            String vehicleSql = "UPDATE vehicles SET status='Booked' WHERE vehicle_id=?";
            PreparedStatement ps3 = con.prepareStatement(vehicleSql);
            ps3.setInt(1, vehicleId);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this, "Payment Successful! Booking Confirmed.");

            new StartPage();
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}