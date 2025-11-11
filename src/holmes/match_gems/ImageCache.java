package holmes.match_gems;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Builds and maintains a map of resource paths to images.
 * Initialized only once by the Helper class, that instance can be used as a kind of
 * "object database" which will remain loaded in memory.
 * <p>
 * The GameBoard will instantiate the cache map by calling getImage for all animation frames of all gems,
 * totalling 30 frames x 6 gems, or 180 images. Thus, 180 is used as the expected capacity for the map.
 */
public class ImageCache {
	private final Map<String, BufferedImage> cache = HashMap.newHashMap(180);

	/**
	 * A call to this method will check if the resulting image specified by the resource path is already in the cache
	 * map or not. If it is in the map, that entry is returned. Otherwise, the new image is added to the cache to save
	 * future performance, and then returned.
	 * <p>
	 * This allows the game board to be reset without having to cache all the images again.
	 *
	 * @param path - resource path to the image
	 * @return - a BufferedImage loaded from the file at the resource path
	 * @throws IOException - no error handling is done here. If a resource path doesn't resolve to an image, there is no backup plan.
	 */
	public BufferedImage getImage(String path) throws IOException {
		if (cache.containsKey(path)) {
			return cache.get(path);
		} else {
			URL url = ClassLoader.getSystemResource(path);
			BufferedImage image = ImageIO.read(url);
			cache.put(path, image);
			return image;
		}
	}
}
