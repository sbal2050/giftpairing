package com.example.giftpairing.model;

import java.util.UUID;


public class Person {
	
	private UUID id;
	private String name;
	
	public Person(String name) {
		super();
		this.id = UUID.randomUUID();
		this.name = name;
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

	@Override
	public String toString() {
		return "Donor {id : " + id + ", name : " + name + "}";
	}
}
