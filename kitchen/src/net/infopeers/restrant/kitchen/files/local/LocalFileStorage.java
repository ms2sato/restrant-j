package net.infopeers.restrant.kitchen.files.local;

import net.infopeers.restrant.kitchen.files.FilePath;
import net.infopeers.restrant.kitchen.files.FileStorage;

public class LocalFileStorage implements FileStorage {

	@Override
	public FilePath getPath(String path) {
		return new LocalFilePath(path);
	}

	@Override
	public FilePath getPath(String backet, String path) {
		return new LocalFilePath(backet, path);
	}

}
