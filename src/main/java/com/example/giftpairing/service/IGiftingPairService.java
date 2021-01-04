package com.example.giftpairing.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.giftpairing.model.Family;
import com.example.giftpairing.model.Person;
import com.example.giftpairing.model.Relation;


/**
 * @author sulagnabal
 *
 */
public interface IGiftingPairService {
	
	List<Family> getFamilies();
	Family getFamily(UUID familyId);
	Family createFamily(Person person);
	Family createDefaultFamily();
	void addMember(UUID familyId, Person person);
	void addRelationShips(Person person, Person relative, Relation relation);
	Map<Person, Person> getGiftingPairs(UUID familyId);
	Map<String, List<String>> getGiftingPairsWithinImmediateFamily(UUID familyId);
	
}
