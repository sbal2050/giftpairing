package com.example.giftpairing.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class Family {
	
	private UUID familyId;
	private Set<Person> members;
	
	
	public Family() {
		super();
		this.familyId = UUID.randomUUID();
		this.members = new HashSet<>();
	}

	public UUID getFamilyId() {
		return familyId;
	}

	public boolean addMember(Person member) {
		return this.members.add(member);
	}

	public Set<Person> getMembers() {
		return Collections.unmodifiableSet(this.members);
	}
	
	public boolean removeMember(Person member) {
		return members.remove(member);
	}

}
