package com.example.giftpairing.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.giftpairing.model.Family;
import com.example.giftpairing.model.Person;
import com.example.giftpairing.service.GiftingPairService;

@RestController
public class GiftPairingController {
	
	@Autowired
	private GiftingPairService service;

	@RequestMapping("/greeting")
	public String greeting() {
		return "Welcome to Gifting Pair app";
	}
	
	@RequestMapping("/giftingpairs")
	public String getGiftPairs() {
		Map<Person, Person> pairs = service.getGiftingPairs(service.getFamilies().stream().findFirst().get().getFamilyId());
		return "Gifting Pairs : " + pairs;
	}
	
	@RequestMapping("/giftingfamilies")
	public List<Family> getAllFamilies() {
		return service.getFamilies();
	}
	
	@RequestMapping("/giftingfamilies/{familyId}")
	public Family getFamily(@PathVariable UUID familyId) {
		return service.getFamily(familyId);
	}
	
	@RequestMapping("/giftingfamilies/{familyId}/giftingpairs")
	public Map<Person, Person> getGiftingPairs(@PathVariable UUID familyId) {
		return service.getGiftingPairs(familyId);
	}
	
	@RequestMapping("/giftingfamilies/{familyId}/giftingmembers")
	public Map<String, List<String>> getGiftingDonorRecipients(@PathVariable UUID familyId, @RequestParam(defaultValue = "true")  boolean reset) {
		
		Map<String, List<String>> matches = service.getGiftingPairsWithinImmediateFamily(familyId);
		if (reset) service.resetAssignment(familyId);
		return matches;
	}
}
