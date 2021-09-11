package top.evanluo.minecreeper.gui;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class AssetLoader {
    public static ImageIcon getImage(String path) {
    	ImageIcon result = null;
    	try {
			result = new ImageIcon(ImageIO.read(AssetLoader.class.getResourceAsStream("/assets/image/"+path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
}
