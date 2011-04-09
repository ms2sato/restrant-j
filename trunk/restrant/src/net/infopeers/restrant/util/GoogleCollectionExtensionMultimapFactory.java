package net.infopeers.restrant.util;

import net.infopeers.restrant.engine.params.ExtensionMultimap;
import net.infopeers.restrant.engine.params.ExtensionMultimapFactory;

public class GoogleCollectionExtensionMultimapFactory implements
		ExtensionMultimapFactory {

	@Override
	public ExtensionMultimap create() {
		return new GoogleCollectionExtensionParamPolicy();
	}

}
