package holmes.match_gems;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gem extends JButton {
	int animationFrame = 1;
	GemColor color;
	GemAnimation animation = GemAnimation.Spin;
	int x;
	int y;
	boolean animationStart = true;

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
		String path;

		//Null color represents an empty space in the game board that will be filled by falling gems
		if (color == null) {
			path = Constants.BLANK_GEM_PATH;
		} else {
			//Use the animation state to determine which animation frames to request
			switch (animation) {
				case Spin -> {
					if (animationFrame > Constants.SPIN_FRAMES) {
						animationFrame = 1;
					}
					path = Constants.gemFilePath(color, animationFrame);
				}

				case SwapLeft -> {
					path = "";//TODO: create paths to animation frames

					//Reset the animation frame counter at the start of a new animation
					if (animationStart) {
						animationFrame = 1;
						animationStart = false;
					} else {
						//After processing all frames of the animation, reset the frame counter and animation state
						if (animationFrame > 10) {//TODO: determine number of animation frames for the swap moves
							animation = GemAnimation.Spin;
							animationFrame = 1;
							path = Constants.gemFilePath(color, animationFrame);
							animationStart = true;
						}
					}
				}

				case SwapRight -> {
					path = "";

					if (animationStart) {
						animationFrame = 1;
						animationStart = false;
					} else {
						if (animationFrame > 10) {
							animation = GemAnimation.Spin;
							animationFrame = 1;
							path = Constants.gemFilePath(color, animationFrame);
							animationStart = true;
						}
					}
				}

				case SwapUp -> {
					path = "";

					if (animationStart) {
						animationFrame = 1;
						animationStart = false;
					} else {
						if (animationFrame > 10) {
							animation = GemAnimation.Spin;
							animationFrame = 1;
							path = Constants.gemFilePath(color, animationFrame);
							animationStart = true;
						}
					}
				}

				case SwapDown -> {
					path = "";

					if (animationStart) {
						animationFrame = 1;
						animationStart = false;
					} else {
						if (animationFrame > 10) {
							animation = GemAnimation.Spin;
							animationFrame = 1;
							path = Constants.gemFilePath(color, animationFrame);
							animationStart = true;
						}
					}
				}

				case ClearMatch -> {
					path = "";

					if (animationStart) {
						animationFrame = 1;
						animationStart = false;
					} else {
						if (animationFrame > 10) {
							animation = GemAnimation.Spin;
							animationFrame = 1;
							path = Constants.gemFilePath(color, animationFrame);
							animationStart = true;
						}
					}
				}

				case DropFill -> {
					path = "";

					if (animationStart) {
						animationFrame = 1;
						animationStart = false;
					} else {
						if (animationFrame > 10) {
							animation = GemAnimation.Spin;
							animationFrame = 1;
							path = Constants.gemFilePath(color, animationFrame);
							animationStart = true;
						}
					}
				}

				default -> path = Constants.BLANK_GEM_PATH;
			}
		}

		BufferedImage image;
		try {
			image = Constants.cache.getImage(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		animationFrame++;

		return new ImageIcon(image);
	}
}
