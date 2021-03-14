package uk.ac.ed.bikerental;

import java.util.UUID;

public class ID {
	private String id;
	private ID_TYPE id_type;
	
	public ID(ID_TYPE id_type) {
		this.id = UUID.randomUUID().toString();
		this.id_type = id_type;
	}
	
	
	
	public String getId() {
		return id;
	}


	public ID_TYPE getId_type() {
		return id_type;
	}


	@Override
	public String toString() {
		return this.id;
	}


}
