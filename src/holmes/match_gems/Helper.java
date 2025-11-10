package holmes.match_gems;

public class Helper {
	//This static instance of ImageCache is used by both GameBoard to initialize it
	//and Gem to request cached images from it.
	//This allows all the animation frames of the gems to be cached for performance.
	public static final ImageCache cache = new ImageCache();
}
