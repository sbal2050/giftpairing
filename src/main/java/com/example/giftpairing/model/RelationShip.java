package com.example.giftpairing.model;

public class RelationShip {
	
	private Person relative;
	private Relation relation;
	
	public RelationShip(Person relative, Relation relation) {
		super();
		this.relative = relative;
		this.relation = relation;
	}

	public Person getRelative() {
		return relative;
	}

	public void setRelative(Person relative) {
		this.relative = relative;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	@Override
	public String toString() {
		return "RelationShip [relation= " + relation + " with relative=" + relative.getName() + "]";
	}
}
