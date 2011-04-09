package net.infopeers.restrant.util.gae;

import net.infopeers.restrant.engine.params.ExtensionMultimap;
import net.infopeers.restrant.engine.params.ExtensionMultimapFactory;

public class GaeExtensionMultimapFactory implements ExtensionMultimapFactory {

	@Override
	public ExtensionMultimap create() {
		return new GaeExtensionParamPolicy();
	}

}
