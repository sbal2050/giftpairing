package com.example.giftpairing.service;


import static org.junit.Assert.assertTrue;

import java.util.Map;
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
		giftingPairService.getGiftingPairs(family.getFamilyId());
		family.getMembers().forEach(m -> {
			assertTrue(m.isAssigned());
		});
	}
	
	@Test
	public void testGetGiftingPairsForOddSizeList() {
		Person p = new Person("Simba Boy");
		Family family = giftingPairService.createFamily(p);
		giftingPairService.addMember(family.getFamilyId(), new Person("Cocoa Girl"));
		giftingPairService.addMember(family.getFamilyId(), new Person("Judy Moody"));
		giftingPairService.getGiftingPairs(family.getFamilyId());
		family.getMembers().forEach(m -> {
			assertTrue(m.isAssigned());
		});
	}
}
