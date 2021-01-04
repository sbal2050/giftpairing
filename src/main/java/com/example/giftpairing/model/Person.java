package com.example.giftpairing.model;

import java.util.UUID;


public class Person {
	
	private UUID id;
	private String name;
	private boolean assigned;
	
	public Person(String name) {
		super();
		this.id = UUID.randomUUID();
		this.name = name;
		this.assigned = false;
	}
	
	public UUID getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	@Override
	public String toString() {
		return "[Name=" + name + "]";
	}
	
}
