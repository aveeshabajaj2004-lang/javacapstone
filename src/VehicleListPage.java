import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class VehicleListPage extends JFrame {

    JTable table;
    DefaultTableModel model;
    String vehicleType;
    JTextField searchField;
    JComboBox<String> statusFilter;

    VehicleListPage(String vehicleType) {
        this.vehicleType = vehicleType;

        setTitle("RideEase Rentals - " + vehicleType);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(238, 246, 255));
        add(panel);

        JLabel title = new JLabel(vehicleType + " Collection");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(25, 84, 166));
        title.setBounds(330, 25, 400, 45);
        panel.add(title);

        JLabel subtitle = new JLabel("Choose your preferred ride and continue your booking");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(80, 80, 80));
        subtitle.setBounds(320, 70, 420, 30);
        panel.add(subtitle);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setBounds(70, 115, 80, 30);
        panel.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(130, 115, 220, 32);
        panel.add(searchField);

        JLabel filterLabel = new JLabel("Status:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setBounds(390, 115, 80, 30);
        panel.add(filterLabel);

        statusFilter = new JComboBox<>(new String[]{"All", "Available", "Unavailable", "Booked"});
        statusFilter.setBounds(450, 115, 160, 32);
        panel.add(statusFilter);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(640, 115, 120, 32);
        styleButton(searchBtn);
        panel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(780, 115, 120, 32);
        styleButton(refreshBtn);
        panel.add(refreshBtn);

        String[] columns = {"ID", "Vehicle Name", "Type", "Rent/Day", "Status"};
        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(200, 220, 245));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(25, 84, 166));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 35));

        table.setDefaultRenderer(Object.class, new StatusRenderer());

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(70, 170, 850, 310);
        panel.add(pane);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(120, 520, 160, 45);
        styleButton(backBtn);
        panel.add(backBtn);

        JButton bookBtn = new JButton("Book Selected Vehicle");
        bookBtn.setBounds(360, 520, 260, 45);
        styleButton(bookBtn);
        panel.add(bookBtn);

        JButton closeBtn = new JButton("Exit");
        closeBtn.setBounds(700, 520, 160, 45);
        styleButton(closeBtn);
        panel.add(closeBtn);

        loadVehicles();

        searchBtn.addActionListener(e -> loadVehicles());

        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            statusFilter.setSelectedItem("All");
            loadVehicles();
        });

        backBtn.addActionListener(e -> {
            new StartPage();
            dispose();
        });

        closeBtn.addActionListener(e -> System.exit(0));

        bookBtn.addActionListener(e -> bookVehicle());

        setVisible(true);
    }

    void loadVehicles() {
        model.setRowCount(0);

        try {
            Connection con = DBConnection.getConnection();

            String searchText = searchField.getText();
            String selectedStatus = statusFilter.getSelectedItem().toString();

            String sql = "SELECT * FROM vehicles WHERE vehicle_type=? AND vehicle_name LIKE ?";

            if (!selectedStatus.equals("All")) {
                sql += " AND status=?";
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, vehicleType);
            ps.setString(2, "%" + searchText + "%");

            if (!selectedStatus.equals("All")) {
                ps.setString(3, selectedStatus);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("vehicle_id"),
                        rs.getString("vehicle_name"),
                        rs.getString("vehicle_type"),
                        rs.getDouble("rent_per_day"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void bookVehicle() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle first");
            return;
        }

        String status = table.getValueAt(row, 4).toString();

        if (!status.equalsIgnoreCase("Available")) {
            JOptionPane.showMessageDialog(this, "This vehicle is currently not available");
            return;
        }

        int vehicleId = Integer.parseInt(table.getValueAt(row, 0).toString());
        String vehicleName = table.getValueAt(row, 1).toString();
        double rent = Double.parseDouble(table.getValueAt(row, 3).toString());

        new BookingPage(vehicleId, vehicleName, rent);
        dispose();
    }

    void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(25, 84, 166));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = table.getValueAt(row, 4).toString();

            if (!isSelected) {
                if (status.equalsIgnoreCase("Available")) {
                    c.setBackground(new Color(220, 255, 220));
                } else {
                    c.setBackground(new Color(255, 225, 225));
                }
            }

            setHorizontalAlignment(CENTER);
            return c;
        }
    }
}