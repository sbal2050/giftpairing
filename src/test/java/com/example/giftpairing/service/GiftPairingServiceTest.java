package com.example.giftpairing.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.giftpairing.error.GiftPairingException;
import com.example.giftpairing.model.Family;
import com.example.giftpairing.model.Person;

/**
 * @author sulagnabal
 *
 */
public class GiftPairingServiceTest {
	
	private GiftingPairService giftingPairService;
	private Family family;
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		giftingPairService = new GiftingPairService();
	}
	
	@Test(expected = GiftPairingException.class)
	public void testGetGiftingPairsWithNull() {
		
		giftingPairService.getGiftingPairs(null);
		exception.expectMessage("Family not found");
	}
	
	@Test(expected = GiftPairingException.class)
	public void testGetGiftingPairsWithUnknownFamilyId() {
		giftingPairService.getGiftingPairs(UUID.randomUUID());
		exception.expectMessage("Family does not exist");
	}
	
	@Test(expected = GiftPairingException.class)
	public void testGetGiftingPairsWithEmptyList() {
		Person p = new Person("Hackyou");
		family = giftingPairService.createFamily(p);
		family.removeMember(p);
		giftingPairService.getGiftingPairs(family.getFamilyId());
		exception.expectMessage("Family does not have enough members to exchange gifts");
	}
	
	@Test(expected = GiftPairingException.class)
	public void testGetGiftingPairsWithOneMember() {
		Person p = new Person("Hackyou");
		family = giftingPairService.createFamily(p);
		giftingPairService.getGiftingPairs(family.getFamilyId());
		exception.expectMessage("Family does not have enough members to exchange gifts");
	}
	
	@Test
	public void testGetGiftingPairsForEvenSizeList() {
		family = giftingPairService.createDefaultFamily();
		assertTrue(family.getMembers().size() %2 == 0);
		Map<Person, Person> pairs = giftingPairService.getGiftingPairs(family.getFamilyId());
		assertEquals(family.getMembers().size(), pairs.size());
		checkSelfPairing(pairs);
	}
	
	@Test
	public void testGetGiftingPairsForOddSizeList() {
		Person p = new Person("Simba Boy");
		Family family = giftingPairService.createFamily(p);
		giftingPairService.addMember(family.getFamilyId(), new Person("Cocoa Girl"));
		giftingPairService.addMember(family.getFamilyId(), new Person("Judy Moody"));
		Map<Person, Person> pairs = giftingPairService.getGiftingPairs(family.getFamilyId());
		assertEquals(family.getMembers().size(), pairs.size());
		checkSelfPairing(pairs);
	}
	
	private void checkSelfPairing(Map<Person, Person> pairs) {
        Optional<Map.Entry<Person, Person>> selfMatchedMember = pairs.entrySet().stream()
                .filter(member -> member.getKey().equals(member.getValue()))
                .findFirst();
        assertFalse(selfMatchedMember.isPresent());
    }
	
	@Test
	public void testGetGiftingMembers() {
		family = giftingPairService.getFamilies().iterator().next();
		Map<String, List<String>> giftingMembers = giftingPairService.getGiftingPairsWithinImmediateFamily(family.getFamilyId());
		assertEquals(family.getMembers().size(), giftingMembers.size());
		family.getMembers().forEach(m -> {assertTrue(m.isAssigned());});
		
	}
	
	
}
