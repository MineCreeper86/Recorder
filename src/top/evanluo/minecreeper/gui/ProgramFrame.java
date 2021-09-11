package top.evanluo.minecreeper.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import top.evanluo.console.ConsoleManager;
import top.evanluo.console.PublicVariables;
import top.evanluo.minecreeper.receiver.DeviceControl;

public class ProgramFrame extends JFrame{
	private static final long serialVersionUID = 1L;
    public static ProgramFrame f = null;
    public static HiRecordPane hiRecordPane = null;
    public static RecordStartButton rStartButton = null;
    public static RecordLabel rLabel = null;
    public ProgramFrame(String string) {
    	super(string);
	}
	public static void startUI() throws Exception {
    	f = new ProgramFrame("操作台 - 设备在"+PublicVariables.arduino.getName().substring(4));
    	f.getContentPane().setBackground(Color.WHITE);
    	f.setBounds(500, 200, 900, 660);
    	f.setLayout(null);
    	f.setVisible(true);
    	hiRecordPane = new ProgramFrame.HiRecordPane();
        f.add(hiRecordPane);
        Thread.sleep(10);
        rStartButton = new ProgramFrame.RecordStartButton();
        f.add(rStartButton);
        Thread.sleep(10);
        rLabel = new ProgramFrame.RecordLabel();
        f.add(rLabel);
        Thread.sleep(10);
        f.setBounds(500, 200, 920, 720);
    }
	
	
	
	
	
	public static class RecordLabel extends JLabel{
		private static final long serialVersionUID = 1L;
		public RecordLabel() {
			super();
			this.setHorizontalAlignment(JLabel.RIGHT);
			this.setFont(new Font("微软雅黑",Font.PLAIN,30));
			this.setBounds(20, 480, 220, 60);
		}
		public void setValue(long l) {
			if(l==-1) this.setText("");
			else {
				String s = l+"";
				while(s.length()<3) s = "0"+s;
				this.setText(s.substring(0,s.length()-2)+"."+s.substring(s.length()-2));
			}
		}
    }
	public static class RecordStartButton extends JButton{
		private static final long serialVersionUID = 1L;
    	public RecordStartButton() {
    		super();
    		this.setOpaque(false);
    		this.setContentAreaFilled(false);
    		this.setBorderPainted(false);
    		this.setBounds(260, 490, 100, 40);
    		this.setIcon(AssetLoader.getImage("button/rstart.png"));
    		this.addMouseListener(new MouseAdapter() {

    			@Override
    			public void mouseEntered(MouseEvent e) {
    				ProgramFrame.rStartButton.setIcon(AssetLoader.getImage("button/rstart_on.png"));
    				if(PublicVariables.wuNeiGui)
    					ProgramFrame.rStartButton.setBounds((int)Math.round(Math.random()*920), (int)Math.round(Math.random()*720), 220, 60);
    			}

    			public void mouseExited(MouseEvent e) {
    				ProgramFrame.rStartButton.setIcon(AssetLoader.getImage("button/rstart.png"));
    			}

    		});
    		this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	ConsoleManager.sendInfo("Record Button", "Button Pressed");
                    DeviceControl.startCounting();
                    PublicVariables.startTime = System.currentTimeMillis()+1000;
                    ConsoleManager.sendInfo("Record Button", "Start Time Set: "+PublicVariables.startTime);
                    PublicVariables.isCounting = true;
                }
            });
    	}
    }
	public static class HiRecordPane extends Container{
		private static final long serialVersionUID = 1L;
		private JLabel personal = null;
		private JLabel classmate = null;
		public HiRecordPane() {
	    	super();
	    	this.setBounds(0,0,920,720);
	    	personal = new JLabel();
	    	personal.setBounds(20,560,100,40);
	    	personal.setIcon(AssetLoader.getImage("record/personal.png"));
	    	this.add(personal);
	    	classmate = new JLabel();
	    	classmate.setBounds(20,620,100,40);
	    	classmate.setIcon(AssetLoader.getImage("record/classmate.png"));
	    	this.add(classmate);
	    	
		}
		public void updateHi(int result) {
			
		}
    }
}
