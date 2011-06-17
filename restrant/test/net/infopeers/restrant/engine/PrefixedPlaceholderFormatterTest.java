package net.infopeers.restrant.engine;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import net.infopeers.commons.regex.Replacer;

import org.junit.Test;

public class PrefixedPlaceholderFormatterTest {

	@Test
	public void senario() throws Exception {

		String path = "/test/:abc/:format.test";
		
		
		PrefixedPlaceholderFormatter f = new PrefixedPlaceholderFormatter();

		String regex = f.getReplaceRegex();
		Pattern p = Pattern.compile(regex);
		
		Replacer replacer = new Replacer(p){
			@Override
			protected String replace(int groupIndex, String value) {
				return value;
			}
		};
		
		assertEquals("/test/abc/format.test", replacer.replace(path));

	}

}
