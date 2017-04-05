package doorsensor;

/**
 * Created by Beatriz on 01/04/2017.
 */
    import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
    import serial.SerialReader;
    import webio.RFSecureClient;

    import java.util.Enumeration;


    public class SimpleSerialReader {

        private class COMHandler
                implements SerialPortEventListener,
                Runnable{

            SerialPort comPort;
            BufferedReader input;
            OutputStream output;
            boolean isMotionDevice;
            String oldLine;
            String inputLine;

            public void initialize() throws Exception{
                input = new BufferedReader(new InputStreamReader(comPort.getInputStream()));
                output = comPort.getOutputStream();

                // add event listeners
                comPort.addEventListener(this);
                comPort.notifyOnDataAvailable(true);
            }

            public void run() {
                try { this.initialize(); } catch (Exception e) {e.printStackTrace();}
            }

            public synchronized void serialEvent(SerialPortEvent oEvent) {
                if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    try {
                        oldLine = inputLine;
                        inputLine = input.readLine();
                        String[] event = inputLine.split("&");
                        if (event.length == 4) {
                            boolean e1 = event[1].compareTo("1") == 0;
                            boolean e2 = event[3].compareTo("1") == 0;
                            if (oldLine.compareTo(inputLine) != 0
                                    && (e1 || e2)) {
                                RFSecureClient.logMovementEvent((e1) ?
                                        event[0] : event[2]);
                            }
                        } else {
                            RFSecureClient.logEvent(event[0],
                                    event[1]);
                        }
                        // System.out.println(inputLine);
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                }
            }

            public COMHandler(SerialPort comPort) {
                this.comPort = comPort;
            }

            public void finalize() throws Throwable{
                if (comPort != null) {
                    serialPort.removeEventListener();
                    serialPort.close();
                }
            }
        }

        SerialPort serialPort;
        /** The port we're normally going to use. */
        private static final String PORT_NAMES[] = {
                //"/dev/tty.usbserial-A9007UX1", // Mac OS X
                //"/dev/ttyACM0", // Raspberry Pi
                //"/dev/ttyUSB0", // Linux
                "COM1",
                "COM2",
                "COM3",
                "COM4",
                "COM5",
                "COM6",
                "COM7",
                "COM8",
                "COM9",
                "COM10",
                "COM11",
                "COM12",
                "COM13",
                "COM14"// Windows
        };
        /**
         * A BufferedReader which will be fed by a InputStreamReader
         * converting the bytes into characters
         * making the displayed results codepage independent
         */
        public static final int TIME_OUT = 2000;
        /** Default bits per second for COM port. */
        public static final int DATA_RATE = 115200;

        public void initialize() {
            // the next line is for Raspberry Pi and
            // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
           // System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            //First, Find an instance of serial port as set in PORT_NAMES.
            SerialPort s = null;
            Runnable[] comPortHandlers = new Runnable[PORT_NAMES.length];
            Thread[] handlerThreads = new Thread[comPortHandlers.length];
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                for (int i = 0; i < PORT_NAMES.length; i++) {
                    if (currPortId.getName().equals(PORT_NAMES[i])) {
                        portId = currPortId;
                        try {
                            s = (SerialPort) portId.open(this.getClass().getName(),
                                TIME_OUT);
                            s.setSerialPortParams(DATA_RATE,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            comPortHandlers[i] = new COMHandler(s);
                            handlerThreads[i] = new Thread(comPortHandlers[i]);
                            handlerThreads[i].start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (portId == null) {
                System.out.println("Could not find COM port.");
            }
        }


        public static void main(String[] args) throws Exception {
            MainClass main = new MainClass();
            main.initialize();
            Thread t=new Thread() {
                public void run() {
                    //the following line will keep this app alive for 1000 seconds,
                    //waiting for events to occur and responding to them (printing incoming messages to console).
                    try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
                }
            };
            t.start();
            System.out.println("Started");
        }
    }
