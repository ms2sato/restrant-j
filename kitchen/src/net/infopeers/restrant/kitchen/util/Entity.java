package net.infopeers.restrant.kitchen.util;

import java.util.Date;

public interface Entity {

	Long getSchemeVersion();

	void setSchemeVersion(Long schemeVersion);

	Date getCreatedAt();

	void setCreatedAt(Date created);

	Date getUpdatedAt();

	void setUpdatedAt(Date updated);
}
