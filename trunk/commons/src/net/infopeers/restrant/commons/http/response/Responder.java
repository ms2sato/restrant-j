package net.infopeers.restrant.commons.http.response;

import java.io.IOException;

public interface Responder {

	boolean start() throws IOException;
}