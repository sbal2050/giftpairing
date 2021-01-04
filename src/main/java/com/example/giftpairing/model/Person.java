package com.example.giftpairing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Person {
	
	private UUID id;
	private String name;
	private boolean isAssigned;
	private List<RelationShip> relationShips;
	
	public Person(String name) {
		super();
		this.id = UUID.randomUUID();
		this.name = name;
		this.isAssigned = false;
		this.relationShips = new ArrayList<>();
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
	
	public List<RelationShip> getRelationShips() {
		return Collections.unmodifiableList(relationShips);
	}
	
	public void addRelationShip(RelationShip newRelationShip) {
		relationShips.stream().filter(r -> newRelationShip.getRelative().equals(r.getRelative()))
			.findAny().ifPresentOrElse(
					(r) -> {}, 
					() -> {relationShips.add(newRelationShip);});
					 
	}
	
	public boolean removeRelationShip(RelationShip relationShip) {
		return this.relationShips.remove(relationShip);
	}
	
	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	
	@Override
	public String toString() {
		return "{id : " + id + ", name : " + name + "}";
	}
}
