package net.infopeers.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtils {

	public static String toString(InputStream is) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuilder sb = new StringBuilder();
		while((line = br.readLine()) != null){
			sb.append(line).append('\n');
		}
		return sb.toString();
	}
}
