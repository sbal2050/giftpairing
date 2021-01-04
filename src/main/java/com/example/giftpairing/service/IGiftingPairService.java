package com.example.giftpairing.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.example.giftpairing.model.Family;
import com.example.giftpairing.model.Person;


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
	Map<Person, Person> getGiftingPairs(UUID familyId);
}
