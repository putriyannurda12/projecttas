import frame.BarangViewFrame;
import frame.PembeliViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args){
        //Koneksi.getConnection();
       // PembeliViewFrame viewFrame = new PembeliViewFrame();
          BarangViewFrame viewFrame = new BarangViewFrame();
        viewFrame.setVisible(true);
    }


}
