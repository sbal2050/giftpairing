package com.example.giftpairing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.giftpairing.error.ErrorCode;
import com.example.giftpairing.error.GiftPairingException;
import com.example.giftpairing.model.Family;
import com.example.giftpairing.model.Person;


/**
 * @author sulagnabal
 *
 */
@Service
public class GiftingPairService implements IGiftingPairService {
	
	//Creating an in-memory default family list
	private static Family family;
	static {
		
		family = new Family();
		family.addMember(new Person("David Letterman"));
		family.addMember(new Person("Susan Letterman"));
		family.addMember(new Person("Thomas Letterman"));
		family.addMember(new Person("Mary Letterman"));
		family.addMember(new Person("Charlie Letterman"));
		family.addMember(new Person("Jenifer Letterman"));
		family.addMember(new Person("John Hamilton"));
		family.addMember(new Person("Kimberly Letterman"));
		family.addMember(new Person("Lori Hamilton"));
		family.addMember(new Person("Jack Hamilton"));
		
	}
	
	private List<Family> families;
	private Set<Person> members;
	
	public GiftingPairService() {
		families = new ArrayList<>();
		families.add(family);
		members = new HashSet<>();
	}
	
	
	/**
	 * Creates and returns a new family with a single person.
	 * Throws exception if argument person is null
	 * 
	 */
	@Override
	public Family createFamily(Person person) {
		if(person == null) 
			throw new GiftPairingException("Can not create family with no member", ErrorCode.ZERO_MEMBERS);
		Family family = new Family();
		family.addMember(person);
		families.add(family);
		return family;
	}
	
	/**
	 * Creates and returns an in-memory default family. This can be used as an example to understand the application.
	 */
	@Override
	public Family createDefaultFamily() {
		families.add(family);
		return family;
	}

	/**
	 * Returns list of all the families created in the system. For demonstration purpose this method would return the default in-memory family.
	 */
	@Override
	public List<Family> getFamilies() {
		return Collections.unmodifiableList(families) ;
	}
	
	/**
	 * Looks up family repository and returns the family when a match is found.
	 * Throws exception if lookup could not find the family.
	 * 
	 */
	@Override
	public Family getFamily(UUID familyId) {
		return families.stream()
				.filter(f -> f.getFamilyId().equals(familyId))
				.findAny()
				.orElseThrow(() -> new GiftPairingException("Family not found", ErrorCode.FAMILY_NOT_FOUND));
	}
	
	/**
	 * Allows user to add a family member to a family.
	 * Throws exception if lookup could not find the family.
	 */
	@Override
	public void addMember(UUID familyId, Person person) {
		families.stream().filter(f -> f.getFamilyId().equals(familyId)).findAny().ifPresentOrElse( 
				f -> { f.addMember(person);},  	
				() -> { throw new GiftPairingException("Family does not exist", ErrorCode.FAMILY_NOT_FOUND);}
			);	
	}
	/**
	 * Pairs members of a family to give gift to each other.
	 * Throws exception if lookup could not find the family, or family has no member or less than two members.
	 */
	@Override
	public Map<Person, Person> getGiftingPairs(UUID familyId) {
		List<Person> members = getFamilyMembers(familyId);
		
		if(CollectionUtils.isEmpty(members) || members.size()<2) {
			throw new GiftPairingException("Family does not have enough members to exchange gifts", ErrorCode.ZERO_MEMBERS);
		}
		//This randomizes the list and ensures the pair are repeated less frequently
		Collections.shuffle(members); 
		
		return getGiftingPairs(members);
	}
	
	private List<Person> getFamilyMembers(UUID familyId) {
		families.stream().filter(f -> f.getFamilyId().equals(familyId)).findAny().ifPresentOrElse( 
				f -> { members = f.getMembers();},  
				() -> { throw new GiftPairingException("Family does not exist", ErrorCode.FAMILY_NOT_FOUND);}
				);	
		return CollectionUtils.isEmpty(members)? Collections.emptyList() : members.stream().collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param family 
	 * @return Pairs for donor and receiver.
	 * To ensure, no member is left out from receiving gifts, it is assumed :
	 *       1. That the members are standing in a circle and gives gift to the person standing to his either side.
	 *       2. Donor would not receive gift from his/her recipient
	 */
	private Map<Person, Person> getGiftingPairs(List<Person> family) {

		int size = family.size();
		Map<Person, Person> pairs = new HashMap<>();
		Person donor = null;
		Person receiver = null;

		for(int i = 0, j = 1;i < size && j <=size; i++, j++) {
			donor = family.get(i);
			receiver = family.get(j);
			receiver = i == (size - 1) ? family.get(0) : family.get(j);
			receiver.setAssigned(true);
			pairs.put(donor, receiver);
		}
		return pairs;
	}
}
