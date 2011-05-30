package net.infopeers.restrant.kitchen.files;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface FilePath {

	FileStorageMetadata getMetadata() throws IOException;

	InputStream readAsStream() throws IOException;

	boolean setReadable(boolean readable) throws IOException;

	void write(byte[] data, FileStorageMetadata metadata) throws IOException;

	URL toURL() throws IOException;

	String getBacket();

	String getPath();

}