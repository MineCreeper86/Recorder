package top.evanluo.minecreeper.gui;

import java.util.ArrayList;
import java.util.Arrays;

import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import top.evanluo.console.ConsoleManager;
import top.evanluo.console.PublicVariables;
import top.evanluo.console.SerialReadThread;
import top.evanluo.console.TimeRecordThread;
import top.evanluo.minecreeper.receiver.DeviceControl;
import top.evanluo.minecreeper.receiver.Serial;
/* Exception List
 * Debug (0x00 ~ 0x1F) Debug-Showing Console Only
 * 0x00 - Port In Use
 * 
 * Normal (0x20 ~ 0x3F) Console Only
 * 0x20 - Output Failed
 * 
 * Exception (0x40 ~ 0x5F)
 * 
 * Warning (0x60 ~ 0x7F)
 * 
 * Serious (0x80 ~ 0x9F)
 * 
 * Fatal (0xA0 ~ 0xBF)
 * 0xA0 - No Such Port
 */
public class Main {
	public static SerialPort arduinoMakeForSure = null;
	public static void main(String[] args) throws Exception {
		ConsoleManager.sendInfo("Main", "Port Scanning");
		ArrayList<String> portsArr = Serial.findPorts();
		for(int i = 0;i < portsArr.size();i++) if(!portsArr.get(i).contains("COM")) portsArr.remove(i);
        String[] ports = portsArr.toArray(new String[portsArr.size()]);
        ArrayList<SerialPort> enabledPortsArr = new ArrayList<SerialPort>();
        ConsoleManager.sendInfo("Main", "Ports: \n"+Arrays.toString(ports));
        SerialPort arduino = null;
        int baudrate = 115200;
        for(int i=0;i<ports.length;i++) {
        	ConsoleManager.sendInfo("Main."+ports[i], "Port Opening");
        	try {
				arduino = Serial.openPort(ports[i], baudrate);
				enabledPortsArr.add(arduino);
				ConsoleManager.sendInfo("Main."+ports[i], "Port Open Succeed");
			} catch (PortInUseException e) {
				ConsoleManager.sendException(ConsoleManager.PORT_IN_USE, "Main."+ports[i], 
						"Exception Occurred During Locating the Arduino\nCurrent Port: "+ports[i]+"\nCurrent Baud Rate: "+baudrate+" Hz\n", e);
				
			}
        }
        if(enabledPortsArr.isEmpty()) ConsoleManager.sendException(ConsoleManager.NO_SUCH_PORT, "Main", 
				"Exception Occurred During Preparing to Verify the Device but No Openable Port Found\nPlease Check if You Terminated All Progresses");
        SerialPort[] enabledPorts = enabledPortsArr.toArray(new SerialPort[enabledPortsArr.size()]);
		Thread.sleep(2000);
		for(int i=0;i<enabledPorts.length;i++) {
			ConsoleManager.sendInfo("Main."+enabledPorts[i].getName(), "Port Verifying");
        	Serial.sendToPort(enabledPorts[i], "verifyArduino8410;".getBytes(), 
					"Exception Occurred During Verifying the Arduino\nCurrent Port: "+enabledPorts[i].getName()+"\nCurrent Baud Rate: "+baudrate+" Hz\n");
			ConsoleManager.sendInfo("Main."+enabledPorts[i].getName(), "Port Verify Message Sent");
			Thread.sleep(100);
			String reply = new String(Serial.readFromPort(enabledPorts[i]));
			if(!reply.contentEquals("")) {
				ConsoleManager.sendInfo("Main."+enabledPorts[i].getName(), "Message Received:\n"+reply);
				if(reply.contentEquals("ack8411;")) {
					ConsoleManager.sendInfo("Main."+enabledPorts[i].getName(), "Arduino Confirmed");
					PublicVariables.arduino = enabledPorts[i];
				}
			}
        }
		if(PublicVariables.arduino==null) ConsoleManager.sendException(ConsoleManager.NO_SUCH_PORT, "Main", 
				"Exception Occurred After Verifying but No Arduino Found\nPlease Check if You Pluged it and Wrote the Correct Program");
		DeviceControl.clear();
		PublicVariables.arduino.addEventListener(new SerialPortEventListener() {

			@Override
			public void serialEvent(SerialPortEvent arg0) {
				
			}
			
		});
		TimeRecordThread.body.start();
		SerialReadThread.body.start();
		ProgramFrame.startUI();
	}
	
}
