package pipe.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class SimpleSprite {

	private BufferedImage bi;
	
	protected int spriteWidth;

	protected int spriteHeight;
	
	public SimpleSprite(String file) {
		try {
			bi = ImageIO.read(getClass().getClassLoader().getResourceAsStream(file));
		} catch (IOException e) {
			System.err.println("Can not read Sprite file: " + file);
			e.printStackTrace();
		}
		spriteWidth = bi.getWidth();
		spriteHeight = bi.getHeight();
	}

	public SimpleSprite(File file) {
		try {
			bi = ImageIO.read(file);
		} catch (IOException e) {
			System.err.println("Can not read Sprite file: " + file);
			e.printStackTrace();
		}
		spriteWidth = bi.getWidth();
		spriteHeight = bi.getHeight();
	}

	public SimpleSprite(URL url) {
		try {
			this.bi = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		spriteWidth = bi.getWidth();
		spriteHeight = bi.getHeight();
	}

	public SimpleSprite(BufferedImage bi) {
		this.bi = SpriteUtils.deepCopy(bi);
		spriteWidth = bi.getWidth();
		spriteHeight = bi.getHeight();
	}

	public SimpleSprite(SimpleSprite sprite) {
		bi = SpriteUtils.deepCopy(sprite.bufferedImage());
		spriteWidth = bi.getWidth();
		spriteHeight = bi.getHeight();
	}

	public BufferedImage bufferedImage() {
		return bi;
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(bi, x, y, x + spriteWidth, y + spriteHeight, 0, 0, spriteWidth, spriteHeight, null);

	}

	public void addTransparency(final int rgb) {
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = rgb | 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else {
					// nothing to do
					return rgb;
				}
			}
		};
		ImageProducer ip = new FilteredImageSource(bi.getSource(), filter);
		Image i = Toolkit.getDefaultToolkit().createImage(ip);
		bi = SpriteUtils.imageToBufferedImage(i, spriteWidth, spriteHeight);
	}

	public int width() {
		return spriteWidth;
	}

	public int height() {
		return spriteHeight;
	}
	
}
