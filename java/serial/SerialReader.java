package serial;

import jssc.SerialPort;
import jssc.SerialPortList;
import jssc.SerialPortException;

public class SerialReader {
    private SerialPort serial;
    public static final int ID_LENGTH = 10;
    public static final String DELIM = "\n";

    public SerialReader() {
        this(0);
    }

    public SerialReader(int n) {
        try {
            serial = new SerialPort(SerialPortList.getPortNames()[n]);
            System.out.println("Opening Serial Port: " + serial.openPort());
            serial.setParams(9600, 8, 1, 0);
        } catch (SerialPortException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public String getCardID() {
        return getMessage()[0];
    }

    public String getSensorID() {
        return getMessage()[1];
    }

    private String[] getMessage() {
        String id = "";
        String oldID = " ";
        try {
            while (id.length() < ID_LENGTH && oldID.compareTo(id) == 0) {
                oldID = id;
                id = readBytesUntil(DELIM);
            }
        } catch (SerialPortException e ) {
            e.printStackTrace();
            System.exit(-1);
        }
        return id.split("&");
    }

    public void finalize() throws Throwable {
        System.out.println("Closing Port: " + serial.closePort());
    }

    private String readBytesUntil(String delim)
            throws SerialPortException {
        String serialData = new String(serial.readBytes());
        String oldSerialData = "";
        while (!(oldSerialData.contains(delim)
                && serialData.contains(delim))) {
            oldSerialData = serialData;
            serialData = new String(serial.readBytes());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        String returnString = oldSerialData + serialData;
        return returnString.split(delim)[1];
    }

}
