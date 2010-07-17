package net.infopeers.restrant.engine;

/**
 * RoRに準じた表記のPlaceholderFormatter
 * @author ms2
 *
 */
public class DefaultPlaceholderFormatter implements PlaceholderFormatter {

	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.PlaceholderFormatter#isPlaceholder(java.lang.String)
	 */
	public boolean isPlaceholder(String target) {
		return target.startsWith(":");
	}

	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.PlaceholderFormatter#enPlaceholder(java.lang.String)
	 */
	public String enPlaceholder(String label){
		return ":" + label;
	}
	
	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.PlaceholderFormatter#dePlaceholder(java.lang.String)
	 */
	public String dePlaceholder(String placeholder) {
		return placeholder.substring(1);
	}
}
