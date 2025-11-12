package holmes.match_gems;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gem extends JButton {
	int animationFrame = 1;
	GemColor color;
	int x;
	int y;

	public Gem(GemColor color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setBackground(Color.WHITE);
		this.setBorder(null);
	}


	/**
	 * Overrides the getIcon method of JLabel.
	 * Each call to this method advances the animationFrame counter,
	 * which is used to retrieve the next BufferedImage from the cache.
	 * <p>
	 * The end result is that calling this method repeatedly will produce a series of images that form an animation.
	 *
	 * @return - current frame of the animation for the spinning gem
	 */
	public ImageIcon getIcon() {
		ImageCache cache = Helper.cache;
		String path = Constants.gemFilePath(color, animationFrame);
		BufferedImage image;
		try {
			image = cache.getImage(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		animationFrame++;

		if (animationFrame > 30) {
			animationFrame = 1;
		}

		return new ImageIcon(image);
	}
}
