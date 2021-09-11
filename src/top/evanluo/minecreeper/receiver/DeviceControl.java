package top.evanluo.minecreeper.receiver;

import top.evanluo.console.PublicVariables;
import top.evanluo.minecreeper.gui.ProgramFrame;

public class DeviceControl {
    public static void showValue(int num,int dot) {
    	String order = "showValue--A"+num+"--A"+dot;
    	packedOrder(order);
    }
    public static void clear() {
    	packedOrder("clear");
    }
    public static void startCounting() {
    	packedOrder("count");
    }
    
    
    public static void processOrder(String inputString) {
    	if(inputString.indexOf("end")==0){
    	    int value = Integer.parseInt(inputString.substring(6, inputString.length()-1));
    	    PublicVariables.isCounting = false;
    	    ProgramFrame.rLabel.setValue(value);
    	  }
    }
    
    
    
    
    private static void packedOrder(String order) {
    	order = order+";";
    	Serial.sendToPort(PublicVariables.arduino, order.getBytes(), "Order Sending By OrderSender:"+order);
    }
}
