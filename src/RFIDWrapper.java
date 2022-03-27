import com.fazecast.jSerialComm.SerialPort;
import java.io.DataOutputStream;
import java.sql.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class RFIDWrapper {
    public SerialPort getALASReader() {
        SerialPort ports[] = SerialPort.getCommPorts();
        SerialPort onport;

        for (SerialPort port : ports) {
            onport = port;
            //System.out.println("trying Port:- " + port.getSystemPortName());
            if (onport.openPort()) {
                onport.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
                Scanner data = new Scanner(port.getInputStream());
                while (data.hasNextLine()) {
                    String msg = "";
                    try {
                        msg = data.nextLine();
                    } catch (Exception e) {
                    }
                    if (msg.equals("ALAS COM PORT RFID")) {
                        return onport;
                    } else {
                        onport.closePort();
                    }
                }
            } else {
                onport = null;
            }
        }
        return null;
    }

    public String readCard(SerialPort sp){
        if (sp == null)
            return null;
        DataOutputStream out = new DataOutputStream(sp.getOutputStream());
        Scanner sc = new Scanner(sp.getInputStream());

        try{
            out.writeBytes("1");
            
            return sc.nextLine();
        }catch (Exception e) {
            return null;
        }
    }

    public String readStudCard(SerialPort sp){
        if (sp == null)
            return null;
        DataOutputStream out = new DataOutputStream(sp.getOutputStream());
        Scanner sc = new Scanner(sp.getInputStream());

        try{
            out.writeBytes("1");
            return sc.nextLine();
        }catch (Exception e) {
            return null;
        }
    }
    
    public int mapBookId(String rfidvalue){
        int bkid = 0;
        int count = 0;
        System.out.println(rfidvalue);
        try {
            Connection con = JavaConnect.ConnecrDb();
            Statement st = con.createStatement();
            System.out.println(rfidvalue);
            String sql = "Select * from book where rfid = '"+rfidvalue+"'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                count++;
                bkid = rs.getInt("book_id");
            }
            if (count == 0) {
              // JOptionPane.showMessageDialog(null,"Tag isn't registered with our system!"); 
            }//24-12*2001
            return bkid;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
            return 0;
        }
    }
    
    public int mapStudId(String rfidvalue){
        int bkid = 0;
        int count = 0;
        System.out.println(rfidvalue);
        try {
            Connection con = JavaConnect.ConnecrDb();
            Statement st = con.createStatement();
            System.out.println(rfidvalue);
            String sql = "Select * from student where rfid = '"+rfidvalue+"'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                count++;
                bkid = rs.getInt("student_id");
            }
            if (count == 0) {
              //  JOptionPane.showMessageDialog(null,"Tag isn't registered with our system!");
            }//24-12*2001
            return bkid;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
            return 0;
        }
    }
    
    
    
    public static void main(String[] args) {
        RFIDWrapper han = new RFIDWrapper();
        SerialPort sp = han.getALASReader();

        if(sp != null){
            System.out.println(han.readCard(sp).trim());
        }else{
            JOptionPane.showMessageDialog(null,"Communication Port for ALAS not detected!");
        }
    }
    
}