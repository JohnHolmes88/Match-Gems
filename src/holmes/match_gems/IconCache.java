package holmes.match_gems;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Builds and maintains a map of resource paths to image icons.
 * Initialized only once by the Constants class, that instance can be used as a kind of
 * "object database" which will remain loaded in memory.
 * <p>
 * The GameBoard will instantiate the cache map by calling getIcon for all animation frames of all gems,
 * totaling number of animation frames x number of gem colors. The product is used as the number of expected mappings.
 */
public class IconCache {
	//Calculate the number of expected mappings by multiplying the number of animation frames by the number of gem colors
	//One additional icon is added to represent the 'null' gem color, which is simply a blank ImageIcon with set dimensions
	private final int expectedMappings = (Constants.SPIN_FRAMES * GemColor.values().length) + 1;
	private final Map<String, ImageIcon> cache = HashMap.newHashMap(expectedMappings);

	//Create the 'blank' image for cleared gems and add it to the cache
	public IconCache() {
		BufferedImage blankImage = new BufferedImage(Constants.GEM_SIZE, Constants.GEM_SIZE, BufferedImage.TYPE_INT_ARGB);
		cache.put(Constants.BLANK_GEM_PATH, new ImageIcon(blankImage));
	}

	/**
	 * A call to this method will check if the resulting icon specified by the resource path is already in the cache
	 * map or not. If it is in the map, that entry is returned. Otherwise, the new icon is added to the cache to save
	 * future performance, and then returned.
	 * <p>
	 * This allows the game board to be reset without having to cache all the icons again.
	 *
	 * @param path - resource path to the image
	 * @return - an ImageIcon loaded from the file at the resource path
	 * @throws IOException - no error handling is done here. If a resource path doesn't resolve to an icon, there is no backup plan.
	 */
	public ImageIcon getIcon(String path) throws IOException {
		if (cache.containsKey(path)) {
			return cache.get(path);
		} else {
			URL url = ClassLoader.getSystemResource(path);
			BufferedImage image = ImageIO.read(url);
			ImageIcon icon = new ImageIcon(image);
			cache.put(path, icon);
			return icon;
		}
	}
}
