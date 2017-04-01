package doorsensor;
import webio.RFSecureClient;
import serial.SerialReader;

import jssc.*;
/**
 * Created by Beatriz on 01/04/2017.
 */
public class Main {

    //    while() {
//        try {
//            client.logEvent(sr.getCardID(), sr.getSensorID());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public static void main(String[] args) {
        RFSecureClient client = new RFSecureClient();

        try {
            SerialPort s = new SerialPort(SerialPortList.getPortNames()[0]);
            s.openPort();
            //s.setParams(9600, 8, 1, 0);
            s.readBytes();
            s.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    try {
                        String string = new String(s.readBytes());
                        System.out.println(string);
                        //String[] stringArr = string.split("\n")[1].split("&");
                        //client.logEvent(stringArr[0], stringArr[1]);
                    } catch (Exception e) {
                        e.printStackTrace();;
                    }
                }
            }, new Integer('\n'));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        RFSecureClient client = new RFSecureClient();

        try {
            SerialPort s = new SerialPort(SerialPortList.getPortNames()[0]);
            s.openPort();
            s.setParams(9600, 8, 1, 0);
            s.readBytes();
            s.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    try {
                        String string = new String(s.readBytes());
                        System.out.println(string);
                        //String[] stringArr = string.split("\n")[1].split("&");
                        //client.logEvent(stringArr[0], stringArr[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Integer('\n'));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


