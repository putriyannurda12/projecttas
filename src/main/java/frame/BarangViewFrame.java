package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;


public class BarangViewFrame extends JFrame{

    private JPanel mainPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JPanel buttonPanel;
    private JButton tambahButton;
    private JButton tutupButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton ubahButton;

    public BarangViewFrame(){
        tambahButton.addActionListener(e->{
            BarangInputFrame inputFrame = new BarangInputFrame();
            inputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data",
                        "Validasi Pilih Data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            BarangInputFrame inputFrame = new BarangInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data",
                        "Validasi Pilih Data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );
            if(pilihan == 0){ //pilihan YES
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM barang WHERE id = ?";
                Connection c= Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cetakButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            String selectSQL = "SELECT * FROM barang";
            Object[][] row;
            try {
                Statement s = c.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery(selectSQL);
                rs.last();
                int jumlah = rs.getRow();
                row = new Object[jumlah][2];
                int i = 0;
                rs.beforeFirst();
                while (rs.next()){
                    row[i][0] = rs.getInt("id");
                    row[i][1] = rs.getString("nama");
                    row[i][2] =rs.getString("pembeli_id");
                    row[i][3] =rs.getString("jenis");
                    row[i][4] =rs.getString("ukuran");
                    row[i][5] =rs.getString("warna");
                    row[i][6] =rs.getString("tanggalpembelian");

                    i++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        cariButton.addActionListener(e -> {
            String keyword = "%"+cariTextField.getText()+"%";
            String searchSQL = "SELECT K. *, B.nama AS nama_pembeli FROM barang K " +
                    "LEFT JOIN pembeli B ON K.pembeli_id = B.id" +
                    "WHERE K.nama like ? OR B.nama like ?";
            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();

                String[] header = {"Id", "Nama Barang","Nama Pembeli","Jenis", "Ukuran", "Warna", "Tanggal Pembelian"};
                DefaultTableModel dtm = new DefaultTableModel(header,0);
                viewTable.setModel(dtm);
                viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
                viewTable.getColumnModel().getColumn(1).setMaxWidth(150);
                viewTable.getColumnModel().getColumn(2).setMaxWidth(150);
                viewTable.getColumnModel().getColumn(3).setMaxWidth(150);

                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                viewTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
                viewTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
                viewTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
                Object[] row = new Object[7];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    row[2] = rs.getString("nama_pembeli");
                    row[3] = rs.getString("jenis");
                    row[4] = rs.getString("ukuran");
                    row[5] = rs.getString("warna");
                    row[6] = rs.getString("tanggalpembelian");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        isiTable();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        pack();
        setTitle("Data Barang");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void isiTable() {
        String selectSQL = "SELECT K. *, B.nama AS nama_pembeli FROM barang K " +
                "LEFT JOIN pembeli B ON K.pembeli_id = B.id";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id", "Nama Barang", "Nama Pembeli", "Jenis","Ukuran","Warna" ,"Tanggal Pembelian"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            viewTable.getColumnModel().getColumn(1).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(2).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(3).setMaxWidth(150);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            viewTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
            Object[] row = new Object[7];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("nama_pembeli");
                row[3] =rs.getString("Jenis");
                row[4] = rs.getString("ukuran");
                row[5] = rs.getString("warna");
                row[6] = rs.getString("tanggalpembelian");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
