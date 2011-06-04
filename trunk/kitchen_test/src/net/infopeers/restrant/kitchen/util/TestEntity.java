package net.infopeers.restrant.kitchen.util;

import javax.persistence.Id;

public class TestEntity {

	@Id
	private String id;

	private String dummy;
	
	public String getDummy() {
		return dummy;
	}

	public void setDummy(String dummy) {
		this.dummy = dummy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
