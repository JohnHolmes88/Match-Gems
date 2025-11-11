package holmes.match_gems;

public class Constants {
	//Second directory can be "32", "64", or "Original". These represent different sizes of the gem images.
	private static final String GEM_MAIN_FILEPATH = "Crystals/64/";
	private static final String GEM_FILE_EXTENSION = ".png";

	public static String gemFilePath(GemColor color, int frame) {
		return GEM_MAIN_FILEPATH + color.name() + "/" + frame + GEM_FILE_EXTENSION;
	}
}
