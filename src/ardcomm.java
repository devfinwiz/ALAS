import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.util.Scanner;

public class ardcomm {

    public static void main(String[] args) {
        // create a window with a slider
        JFrame window = new JFrame();
        JSlider slider = new JSlider();
        slider.setMaximum(1023);
        window.add(slider);
        window.pack();
        window.setVisible(true);

        // determine which serial port to use
        SerialPort ports[] = SerialPort.getCommPorts();
        System.out.println("Select a port:");
        int i = 1;
        for(SerialPort port : ports) {
            System.out.println(i++ + ". " + port.getSystemPortName());
        }
        Scanner s = new Scanner(System.in);
        int chosenPort = s.nextInt();

        // open and configure the port
        SerialPort port = ports[chosenPort - 1];
        if(port.openPort()) {
            System.out.println("Successfully opened the port.");
        } else {
            System.out.println("Unable to open the port.");
            return;
        }
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        // enter into an infinite loop that reads from the port and updates the GUI
        Scanner data = new Scanner(port.getInputStream());
        while(data.hasNextLine()) {
            int number = 0;
            String str="";
            try{str = data.nextLine();}catch(Exception e){}
            //slider.setValue(number);
            System.out.println(str);
        }
    }

}