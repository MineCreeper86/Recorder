package top.evanluo.minecreeper.receiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import top.evanluo.console.ConsoleManager;

public class Serial {
    public static final ArrayList<String> findPorts() {
        // ��õ�ǰ���п��ô���
        @SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNameList = new ArrayList<String>();
        // �����ô�������ӵ�List�����ظ�List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return portNameList;
    }
    /**
     * �򿪴���
     * 
     * @param portName
     *            �˿�����
     * @param baudrate
     *            ������
     * @return ���ڶ���
     * @throws PortInUseException
     *             �����ѱ�ռ��
     */
    public static final SerialPort openPort(String portName, int baudrate) throws PortInUseException {
        try {
            // ͨ���˿���ʶ��˿�
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            // �򿪶˿ڣ������˿����ֺ�һ��timeout���򿪲����ĳ�ʱʱ�䣩
            CommPort commPort = portIdentifier.open(portName, 2000);
            // �ж��ǲ��Ǵ���
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    // ����һ�´��ڵĲ����ʵȲ���
                    // ����λ��8
                    // ֹͣλ��1
                    // У��λ��None
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                }
                return serialPort;
            }
        } catch (NoSuchPortException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * �رմ���
     * 
     * @param serialport
     *            ���رյĴ��ڶ���
     */
    public static void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
        }
    }

    /**
     * �����ڷ�������
     * 
     * @param serialPort
     *            ���ڶ���
     * @param order
     *            ����������
     */
    public static void sendToPort(SerialPort serialPort, byte[] order, String further) {
    	String pName = "Undefined";
        OutputStream out = null;
        try {
        	pName = serialPort.getName();
            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
        } catch (IOException e) {
        	ConsoleManager.sendException(ConsoleManager.OUTPUT_FAILED, "Serial."+pName, further+"This is an IOException.\nPort Name: "+pName+"\nBaud Rate: "+serialPort.getBaudRate(), e);
        } catch (NullPointerException e) {
        	ConsoleManager.sendException(ConsoleManager.OUTPUT_FAILED, "Serial.Undefined", further+"This is a NullPointerException.", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �Ӵ��ڶ�ȡ����
     * 
     * @param serialPort
     *            ��ǰ�ѽ������ӵ�SerialPort����
     * @return ��ȡ��������
     */
    public static byte[] readFromPort(SerialPort serialPort) {
        InputStream in = null;
        byte[] bytes = {};
        try {
            in = serialPort.getInputStream();
            // ��������СΪһ���ֽ�
            byte[] readBuffer = new byte[1];
            int bytesNum = in.read(readBuffer);
            while (bytesNum > 0) {
                int str1Length = bytes.length;
                int str2length = readBuffer.length;
                bytes = Arrays.copyOf(bytes, str1Length+str2length);//��������
                System.arraycopy(readBuffer, 0, bytes, str1Length, str2length);
                //System.out.println(Arrays.toString(bytes));
                bytesNum = in.read(readBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * ��Ӽ�����
     * 
     * @param port
     *            ���ڶ���
     * @param listener
     *            ���ڴ�����Ч���ݼ���
     */
    public static void addListener(SerialPort serialPort, DataAvailableListener listener) {
        try {
            // ��������Ӽ�����
            serialPort.addEventListener(new SerialPortListener(listener));
            // ���õ������ݵ���ʱ���Ѽ��������߳�
            serialPort.notifyOnDataAvailable(true);
            // ���õ�ͨ���ж�ʱ�����ж��߳�
            serialPort.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ڼ���
     */
    public static class SerialPortListener implements SerialPortEventListener {

        private DataAvailableListener mDataAvailableListener;

        public SerialPortListener(DataAvailableListener mDataAvailableListener) {
            this.mDataAvailableListener = mDataAvailableListener;
        }

        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE: // 1.���ڴ�����Ч����
                if (mDataAvailableListener != null) {
                    mDataAvailableListener.dataAvailable();
                }
                break;

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.��������������
                break;

            case SerialPortEvent.CTS: // 3.�������������
                break;

            case SerialPortEvent.DSR: // 4.����������׼������
                break;

            case SerialPortEvent.RI: // 5.����ָʾ
                break;

            case SerialPortEvent.CD: // 6.�ز����
                break;

            case SerialPortEvent.OE: // 7.��λ�����������
                break;

            case SerialPortEvent.PE: // 8.��żУ�����
                break;

            case SerialPortEvent.FE: // 9.֡����
                break;

            case SerialPortEvent.BI: // 10.ͨѶ�ж�
                break;

            default:
                break;
            }
        }
    }

    /**
     * ���ڴ�����Ч���ݼ���
     */
    public interface DataAvailableListener {
        /**
         * ���ڴ�����Ч����
         */
        void dataAvailable();
    }
}
