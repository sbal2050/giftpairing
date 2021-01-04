package com.example.giftpairing.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.giftpairing.error.ErrorCode;
import com.example.giftpairing.error.GiftPairingException;
import com.example.giftpairing.model.Family;
import com.example.giftpairing.model.Person;
import com.example.giftpairing.model.Relation;
import com.example.giftpairing.model.RelationShip;


/**
 * @author sulagnabal
 *
 */
@Service
public class GiftingPairService implements IGiftingPairService {
	
	//Creating an in-memory default family list
	private static Family family = new Family();
	private static Person david = new Person("David Letterman"); 
	private static Person tom = new Person("Thomas Letterman"); 
	private static Person susan = new Person("Susan Letterman"); 
	private static Person mary = new Person("Mary Letterman");
	private static Person charlie = new Person("Charlie Letterman");
	private static Person jenny = new Person("Jenifer Letterman");
	private static Person john = new Person("John Hamilton");
	private static Person kim = new Person("Kimberly Letterman");
	private static Person lori = new Person("Lori Hamilton");
	private static Person jack = new Person("Jack Hamilton");
	static {
		family.addMember(david);
		family.addMember(tom);
		family.addMember(susan);
		family.addMember(mary);
		family.addMember(charlie);
		family.addMember(jenny);
		family.addMember(john);
		family.addMember(kim);
		family.addMember(lori);
		family.addMember(jack);
		
		addRelationShip(david, tom, Relation.SIBLING);
		addRelationShip(david, susan, Relation.SPOUSE);
		addRelationShip(charlie, david, Relation.CHILD);
		addRelationShip(charlie, susan, Relation.CHILD);
		addRelationShip(tom, mary, Relation.SPOUSE);
		addRelationShip(jenny, tom, Relation.PARENT);
		addRelationShip(jenny, mary, Relation.PARENT);
		addRelationShip(kim, tom, Relation.PARENT);
		addRelationShip(kim, mary, Relation.PARENT);
		addRelationShip(jenny, kim, Relation.SIBLING);
		
		addRelationShip(jenny, john, Relation.SPOUSE);
		addRelationShip(lori, john, Relation.PARENT);
		addRelationShip(lori, jenny, Relation.PARENT);
		addRelationShip(jack, john, Relation.PARENT);
		addRelationShip(jack, jenny, Relation.PARENT);
		addRelationShip(jack, lori, Relation.SIBLING);
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
	
	@Override
	public void addRelationShips(Person person, Person relative, Relation relation) {		
		person.addRelationShip(new RelationShip(relative, relation));
		relative.addRelationShip(new RelationShip(person,relation.reverseRelation()));				
	}
	
	
	private static void addRelationShip(Person person, Person relative, Relation relation) {		
		person.addRelationShip(new RelationShip(relative, relation));
		relative.addRelationShip(new RelationShip(person,relation.reverseRelation()));				
	}
	
	/**
	 * Pairs members of a family to give gift to each other.
	 * Throws exception if lookup could not find the family, or family has no member or less than two members.
	 */
	@Override
	public Map<Person, Person> getGiftingPairs(UUID familyId) {
		List<Person> members = randomizeOptions(familyId); 
		return getGiftingPairs(members);
	}
	
	@Override
	public Map<String, List<String>> getGiftingPairsWithinImmediateFamily(UUID familyId) {
		
		List<Person> familyMembers = randomizeOptions(familyId);
		Queue<Person> queue = new LinkedList<>(familyMembers);
		Map<String, List<String>> giftDonorRecipientList = new HashMap<>();
		List<String> recipients;
		EnumSet<Relation> relatives = EnumSet.of(Relation.CHILD, Relation.PARENT, Relation.SPOUSE);
		while(!queue.isEmpty()) {
			Person donor = queue.remove();

			recipients = new ArrayList<>();
			
			List<Person> immediateFamily = getImmediateFamilyMembers(donor.getRelationShips(), r -> relatives.contains(r));
			for(Person m :immediateFamily) {
				if(!m.isAssigned()) {
					m.setAssigned(true);
					recipients.add(m.getName());
				}
			}
			giftDonorRecipientList.put(donor.getName(), recipients);
		}
		giftDonorRecipientList.forEach((k,v) -> {
			System.out.println(k + " : " + v);
		});
		
		return giftDonorRecipientList;
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
			receiver = i == (size - 1) ? family.get(0) : family.get(j);
			pairs.put(donor, receiver);
		}
		return pairs;
	}

	private List<Person> randomizeOptions(UUID familyId) {
		List<Person> members = getFamilyMembers(familyId);
		
		if(CollectionUtils.isEmpty(members) || members.size()<2) {
			throw new GiftPairingException("Family does not have enough members to exchange gifts", ErrorCode.ZERO_MEMBERS);
		}
		//This randomizes the list and ensures the pair are repeated less frequently
		Collections.shuffle(members);
		return members;
	}
	
	
	private List<Person> getFamilyMembers(UUID familyId) {
		families.stream().filter(f -> f.getFamilyId().equals(familyId)).findAny().ifPresentOrElse( 
				f -> { members = f.getMembers();},  
				() -> { throw new GiftPairingException("Family does not exist", ErrorCode.FAMILY_NOT_FOUND);}
				);	
		return CollectionUtils.isEmpty(members)? Collections.emptyList() : members.stream().collect(Collectors.toList());
	}
	
	private List<Person> getImmediateFamilyMembers(List<RelationShip> relationShips, Predicate<Relation> predicate) {
		return relationShips.stream().filter(r -> predicate.test(r.getRelation()))
				.map(RelationShip :: getRelative)
				.collect(Collectors.toList());
	}
	
	public void resetAssignment(UUID familyId) {
		List<Person> members = getFamilyMembers(familyId);
		for(Person m : members) {m.setAssigned(false);}
	}
	
}
