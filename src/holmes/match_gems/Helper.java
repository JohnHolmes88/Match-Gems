package holmes.match_gems;

public class Helper {
	/*
	This static instance of ImageCache is instantiated and initialized by the GameBoard during start up,
	and then Gem objects will request cached images from it during each tick of animation updates.
	This allows all the animation frames of the gems to be loaded into memory for performance.
	*/
	public static final ImageCache cache = new ImageCache();
}
