package holmes.match_gems;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gem extends JLabel {
	int animationFrame = 1;
	final GemColor color;

	public Gem(GemColor color) {
		this.color = color;
	}

	public String resourcePath() {
		switch (color) {
			case BLUE -> {
				return "Crystals/64/Blue";
			}
			case CLEAR -> {
				return "Crystals/64/Clear";
			}
			case GREEN -> {
				return "Crystals/64/Green";
			}
			case PINK -> {
				return "Crystals/64/Pink";
			}
			case RED -> {
				return "Crystals/64/Red";
			}
			case YELLOW -> {
				return "Crystals/64/Yellow";
			}
			default -> {
				return "Crystals/Original/Blue";
			}
		}
	}

	/**
	 * Overrides the getIcon method of JLabel
	 * Each call to this method advances the animationFrame counter,
	 * which is used to retrieve the next BufferedImage from the cache.
	 * <p>
	 * The end result is that calling this method repeatedly will produce a series of images that form an animation.
	 *
	 * @return - current frame of the animation for the spinning gem
	 */
	public ImageIcon getIcon() {
		ImageCache cache = Helper.cache;
		String path = resourcePath() + "/" + animationFrame + ".png";
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
