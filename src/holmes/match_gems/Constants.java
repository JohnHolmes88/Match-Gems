package holmes.match_gems;

public class Constants {
	/*
	This static instance of ImageCache is instantiated and initialized by the GameBoard during start up,
	and then Gem objects will request cached images from it during each tick of animation updates.
	This allows all the animation frames of the gems to be loaded into memory for performance.
	*/
	public static final ImageCache cache = new ImageCache();


	//Second directory can be "32", "64", or "Original". These represent different sizes of the gem images.
	private static final String GEM_MAIN_FILEPATH = "Crystals/64/";
	private static final String GEM_FILE_EXTENSION = ".png";

	public static String gemFilePath(GemColor color, int frame) {
		return GEM_MAIN_FILEPATH + color.name() + "/" + frame + GEM_FILE_EXTENSION;
	}
}
