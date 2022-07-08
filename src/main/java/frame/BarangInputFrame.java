package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerBeanInfo;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class BarangInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JPanel buttonPanel;
    private JButton batalButton;
    private JButton simpanButton;
    private JComboBox namacomboBox;
    private JRadioButton a38RadioButton;
    private JRadioButton a41RadioButton;
    private JRadioButton a39RadioButton;
    private JRadioButton a40RadioButton;
    private JTextField jenisTextField;
    private JTextField warnaTextField;
    private DatePicker tanggalDatePicker;
    private ButtonGroup ukuranButtonGroup;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(id));

        String findSQL = "SELECT * FROM barang WHERE id =?";
        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                int pembeliId = rs.getInt("pembeli_id");
                for (int i=0; i < namacomboBox.getItemCount(); i++) {
                    namacomboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) namacomboBox.getSelectedItem();
                    if (pembeliId == item.getValue()) {
                        break;
                    }
                }
                String ukuran = rs.getString("ukuran");
                if (ukuran != null) {
                    if (ukuran.equals("38")) {
                        a38RadioButton.setSelected(true);
                    } else if (ukuran.equals("39")) {
                        a39RadioButton.setSelected(true);
                    } else if (ukuran.equals("40")) {
                        a40RadioButton.setSelected(true);
                    } else if (ukuran.equals("41")) {
                        a41RadioButton.setSelected(true);
                    }
                }
                jenisTextField.setText(rs.getString("jenis"));
                warnaTextField.setText(rs.getString("warna"));
                for (int i = 0; i < namacomboBox.getItemCount(); i++) {
                    namacomboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) namacomboBox.getSelectedItem();
                    if (pembeliId == item.getValue()) {
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BarangInputFrame() {
        simpanButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            String nama = namaTextField.getText();

            if (nama.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama barang",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }
            if (jenisTextField.getText().equals("")) {
                jenisTextField.setText("");
            }
            String jenis = String.valueOf(jenisTextField.getText());
            if (jenis.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data jenis sepatu",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                jenisTextField.requestFocus();
                return;
            }
            if (warnaTextField.getText().equals("")) {
                warnaTextField.setText("0");
            }
            String warna = String.valueOf(warnaTextField.getText());
            if (warna.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data warna",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                warnaTextField.requestFocus();
                return;
            }
            ComboBoxItem item = (ComboBoxItem) namacomboBox.getSelectedItem();
            int pembeliId = item.getValue();
            if (pembeliId == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama pembeli",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namacomboBox.requestFocus();
                return;
            }
            String tanggalPembelian = tanggalDatePicker.getText();
            if (tanggalPembelian.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi tangal pembelian",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                tanggalDatePicker.requestFocus();
                return;
            }
            String ukuran = "";
            if (a38RadioButton.isSelected()) {
                ukuran = "38";
            } else if (a39RadioButton.isSelected()) {
                ukuran = "39";
            } else if (a40RadioButton.isSelected()) {
                ukuran = "40";
            } else if (a41RadioButton.isSelected()) {
                ukuran = "41";
            } else {
                JOptionPane.showMessageDialog(null,
                        "Pilih Ukuran",
                        "Validasi Data Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                if(id == 0) {
                    String cekSQL = "SELECT * FROM barang WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada"
                        );
                    } else {
                        String insertSQL = "INSERT INTO barang (id, nama, pembeli_id,jenis, ukuran,warna, tanggalpembelian) VALUES (NULL, ?,?,?,?,?,?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, pembeliId);
                        ps.setString(3, jenis);
                        ps.setString(4, ukuran);
                        ps.setString(5, warna);
                        ps.setString(6,tanggalPembelian);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String cekSQL = "SELECT * FROM barang WHERE nama = ? AND id != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada"
                        );
                    } else {
                        String updateSQL = "UPDATE barang SET nama=?, pembeli_id =?,jenis = ?,ukuran = ?,warna = ?,tanggalpembelian= ? WHERE id=?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, pembeliId);
                        ps.setString(3, jenis);
                        ps.setString(4, ukuran);
                        ps.setString(5, warna);
                        ps.setString(6,tanggalPembelian);
                        ps.setInt(7, id);
                        ps.executeUpdate();
                        dispose();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        kustomisasiKomponen();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        pack();
        setTitle("Input Barang");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    public void kustomisasiKomponen() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM pembeli ORDER BY nama";

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            namacomboBox.addItem(new ComboBoxItem(0, "Pilih nama pembeli"));
            while (rs.next()) {
                namacomboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("nama")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ukuranButtonGroup = new ButtonGroup();
        ukuranButtonGroup.add(a38RadioButton);
        ukuranButtonGroup.add(a39RadioButton);
        ukuranButtonGroup.add(a40RadioButton);
        ukuranButtonGroup.add(a41RadioButton);

        DatePickerSettings dps = new DatePickerSettings();
        dps.setFormatForDatesBeforeCommonEra("yyy-MM-dd");
        tanggalDatePicker.setSettings(dps);
    }

}


