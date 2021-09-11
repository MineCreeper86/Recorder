package top.evanluo.console;

public class ConsoleManager {
	static boolean showDebug = true;
	static boolean showFurther = true;
	public static final int PORT_IN_USE = 0x00;
	public static final int OUTPUT_FAILED = 0x20;
	public static final int INPUT_FAILED = 0x21;
	public static final int NO_SUCH_PORT = 0xA0;
	public static void sendInfo(String part, String info) {
		if(showDebug) System.out.println("\u001b[00;32m"+part+" - "+info+"\u001b[00m");
	}
	public static void sendException(int id, String part, String further) {
    	if((id >= 32 || showDebug) && id < 64) System.out.println("\u001b[00;33m"+part+" - "+transferToString(id)+" (0x"+Integer.toHexString(id)+") "+"\u001b[00m");
    	if(id > 64) System.out.println("\u001b[00;31m"+part+" - "+transferToString(id)+" (0x"+Integer.toHexString(id)+") "+"\u001b[00m");
    	if(showFurther) System.out.println("Further Information: " + further);
    }
	public static void sendException(int id, String part, String further, Exception e) {
		String sOut = "";
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
        sendException(id, part, further+"\nStack Trace Below:\n"+sOut);
    }
	private static String transferToString(int id) {
		String e = "Unknown Exception";
		switch(id) {
		case 0x00:e="Port In Use";break;
		case 0x20:e="Output Failed";break;
		case 0x21:e="Input Failed";break;
		case 0xA0:e="No Such Port";break;
		}
		return e;
	}
}
