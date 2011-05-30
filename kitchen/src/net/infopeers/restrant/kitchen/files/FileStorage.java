package net.infopeers.restrant.kitchen.files;


public interface FileStorage {

	FilePath getPath(String path);

	FilePath getPath(String backet, String path);

}