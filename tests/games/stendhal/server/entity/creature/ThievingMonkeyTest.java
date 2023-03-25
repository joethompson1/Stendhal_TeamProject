/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.creature;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.game.RPObject;
import utilities.PlayerTestHelper;
import utilities.RPClass.ThievingMonkeyTestHelper;

public class ThievingMonkeyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ThievingMonkeyTestHelper.generateRPClasses();
		MockStendlRPWorld.get();
	}

	List<String> foods = Arrays.asList("banana");

	/**
	 * Tests for ThievingMonkey.
	 */
	@Test
	public void testThievingMonkey() {
		final ThievingMonkey monkey = new ThievingMonkey();
		assertThat(monkey.getFoodNames(), is(foods));
	}

	/**
	 * Tests for thievingMonkeyPlayer.
	 */
	@Test
	public void testThievingMonkeyPlayer() {

		final StendhalRPZone zone = new StendhalRPZone("zone");
		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		zone.add(testPlayer);
		final ThievingMonkey monkey = new ThievingMonkey(testPlayer);

		assertThat(monkey.getFoodNames(), is(foods));
	}

	/**
	 * Tests for thievingMonkeyRPObjectPlayer.
	 */
	@Test
	public void testThievingMonkeyRPObjectPlayer() {
		RPObject template = new RPObject();
		template.put("hp", 30);
		final ThievingMonkey monkey = new ThievingMonkey(template, PlayerTestHelper.createPlayer("testPlayer"));
		assertThat(monkey.getFoodNames(), is(foods));
	}
	/**
	 * Test to make sure the thieving monkey initialise with correct stats including weight.
	 */
	@Test
	public void doesStatsInitialiseCorrectly() 
	{
		//set up the player,zone and pet for the test
		final StendhalRPZone zone = new StendhalRPZone("zone");
		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		zone.add(testPlayer);
		final ThievingMonkey monkey = new ThievingMonkey(testPlayer);
		
		//check all the stats are equal to the starting stats
		assertEquals(500, monkey.getHP());
		assertEquals(6, monkey.getLVCap());
		assertEquals(15, monkey.getAtk());
		assertEquals(40, monkey.getDef());
		assertEquals(100, monkey.getXP());
		assertEquals(0.9, monkey.getBaseSpeed(), 0);
		assertEquals(1, monkey.getWeight());

	}
	
	/**
	 * Tests for monkey successfully creating path to NPC entity, and stops stealing after reached.
	 */
	@Test
	public void testThievingMonkeyFindsPathToNPC() {
		final StendhalRPZone zone = new StendhalRPZone("zone", 10, 10);
		
		final Player player = PlayerTestHelper.createPlayer("player");
		zone.add(player);
		
		final ThievingMonkey monkey = new ThievingMonkey(player);
		Creature creature = SingletonRepository.getEntityManager().getCreature("rat");
		
		zone.add(player);
		zone.add(monkey);
		zone.add(creature);
		creature.setPosition(10, 10);
		monkey.setPosition(10, 5);
		player.setPosition(0, 0);
		
		// Run pet logic so it sets path
		monkey.logic();
					
		// Get monkey's final path node
		int pathLength = monkey.getPath().getNodes().length;
		int monkeyFinalNodeX = monkey.getPath().getNodes()[pathLength-1].getX();
	    int monkeyFinalNodeY = monkey.getPath().getNodes()[pathLength-1].getY();
	    
	    // Put monkey at final node
	    monkey.setPosition(monkeyFinalNodeX, monkeyFinalNodeY);
		
	    // Assert that this final position is next to entity
		assertTrue(monkey.nextTo(creature));
		
		// Run pet logic so monkey steals and leaves stealing state
		monkey.logic();
		monkey.logic();
		
		// Assert that once reached entity, he steals and stops wanting to steal
		assertTrue(monkey.getIdea() == null);

	}
	
	/**
	 * Tests monkey runs stealing logic when obtained target.
	 */
	@Test
	public void testMonkeyWantsToSteal() {
		final StendhalRPZone zone = new StendhalRPZone("zone", 10, 10);
		
		final Player player = PlayerTestHelper.createPlayer("player");
		zone.add(player);
		
		Creature creature = SingletonRepository.getEntityManager().getCreature("rat");
		zone.add(creature);
		
		final ThievingMonkey monkey = new ThievingMonkey(player);
		// Monkey zone set automatically
		
		creature.setPosition(6, 5);
		monkey.setPosition(3, 2);
		player.setPosition(3, 3);
		
		assertFalse(monkey.nextTo(creature));
		
		// Run pet logic so it sets path
		for (int i = 0; i < 1; i++) {
			monkey.logic();
		}
		
		// Assert that monkey tries to steal from corpse
		assertEquals(monkey.getIdea(), "stealing");

	}
	

	/**
	 * Tests super.logic() runs if target is out of range
	 */
	@Test
	public void testTargetOutOfRange() {
		final StendhalRPZone zone = new StendhalRPZone("zone", 30, 30);
		
		final Player player = PlayerTestHelper.createPlayer("player");
		zone.add(player);
		
		Creature creature = SingletonRepository.getEntityManager().getCreature("rat");
		zone.add(creature);
			
		final ThievingMonkey monkey = new ThievingMonkey(player);
		// Monkey zone set automatically
		
		creature.setPosition(30, 30);
		monkey.setPosition(0, 0);
		player.setPosition(0, 1);
		
		monkey.stealEnabled = 0;
		
		// Run monkey logic
		monkey.logic();
		
		// Assert monkey is set back to stealing state
//		assertTrue(monkey.stealEnabled == 1);

	}
	
	
	/**
	 * Tests monkey steals ring from player
	 */
	@Test
	public void testMonkeyStealingFromPlayer() {
		final StendhalRPZone zone = new StendhalRPZone("zone", 10, 10);
		
		final Player player = PlayerTestHelper.createPlayer("player");
		zone.add(player);
		
		final Player victim = PlayerTestHelper.createPlayer("victim");
		zone.add(victim);
		
		final ThievingMonkey monkey = new ThievingMonkey(player);
		// Monkey should be added to zone automatically
		
		// Equip ring to victim
		Item keyring = SingletonRepository.getEntityManager().getItem("keyring");
		victim.equipToInventoryOnly(keyring);
		Item ring = SingletonRepository.getEntityManager().getItem("emerald ring");
		victim.equipToInventoryOnly(ring);
		
		
		// C
		assertTrue(victim.getFirstEquipped("emerald ring") != null);
		
		victim.setPosition(2, 2);
		monkey.setPosition(3, 2);
		player.setPosition(3, 3);
		
		assertTrue(monkey.nextTo(victim));
		
		// Run pet logic so it sets path
		monkey.logic();
		
		// Assert that monkey takes ring from victim
		assertFalse(ring.getBoundTo() == victim.getName());
		assertTrue(player.getFirstEquipped("emerald ring") != null);
		
	}

}
