package net.infopeers.restrant.engine;

/**
 * PlaceholderFormatter for prefixed placeholder.
 * defaults, prefix is ':'. to RoR standard expression.
 * @author ms2
 */
public class PrefixedPlaceholderFormatter implements PlaceholderFormatter {
	
	private String prefix = ":";
	
	public PrefixedPlaceholderFormatter(){
		
	}

	public PrefixedPlaceholderFormatter(String prefix){
		this.prefix = prefix;
	}

	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.PlaceholderFormatter#isPlaceholder(java.lang.String)
	 */
	public boolean isPlaceholder(String target) {
		return target.startsWith(prefix);
	}

	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.PlaceholderFormatter#enPlaceholder(java.lang.String)
	 */
	public String enPlaceholder(String label){
		return prefix + label;
	}
	
	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.PlaceholderFormatter#dePlaceholder(java.lang.String)
	 */
	public String dePlaceholder(String placeholder) {
		return placeholder.substring(1);
	}

	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.PlaceholderFormatter#hasPlaceholder(java.lang.String)
	 */
	public boolean hasPlaceholder(String text) {
		//TODO: fix to more better
		return text.indexOf(prefix) != -1;
	}
}
