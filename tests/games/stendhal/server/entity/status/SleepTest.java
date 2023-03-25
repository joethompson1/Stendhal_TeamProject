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
package games.stendhal.server.entity.status;

//import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;


import games.stendhal.server.core.engine.SingletonRepository;

import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.CreatureTestHelper;

public class SleepTest {
	
	private StendhalRPZone zone;

	
	/**
	 * initialisation
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		MockStendlRPWorld.get();
		CreatureTestHelper.generateRPClasses();

	}

	/**
	 * Tests for sleeping bag.
	 */
	@Test
	public void testSleepingBag() {

		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		Item sleepingBag = SingletonRepository.getEntityManager().getItem("sleeping bag");
		
		assertTrue(testPlayer.equipToInventoryOnly(sleepingBag));
	}
	

	/**
	 * Tests for sleeping status.
	 */
	@Test
	public void testSleep() {
		//create player
		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		//make sure it doesnt have full hp
		testPlayer.setHP(50);
		testPlayer.setPosition(10, 10);
		Item sleepingBag = SingletonRepository.getEntityManager().getItem("sleeping bag");
		
		testPlayer.equip("bag", sleepingBag);
		zone = new StendhalRPZone("zone");
		zone.add(testPlayer);
		
		//use the sleeping bag
		sleepingBag.onUsed(testPlayer);
		//check if he is sleeping
		assertTrue(testPlayer.hasStatus(StatusType.SLEEPING));
		
	}
	
	/**
	 * Test that the sleeping bag is dropped on use.
	 */
	@Test
	public void testSleepingBagDropOnUsed() {
		
		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		testPlayer.setPosition(10, 10);
		Item sleepingBag = SingletonRepository.getEntityManager().getItem("sleeping bag");
		
		testPlayer.equip("bag", sleepingBag);
		zone = new StendhalRPZone("testzone");
		zone.add(testPlayer);
		
		sleepingBag.onUsed(testPlayer);
		//move the player so there is only one entity at that position
		testPlayer.setPosition(10, 11);
		Entity testItem = zone.getEntityAt(10, 10);
				
		assertTrue(sleepingBag.getName() == testItem.getName());
	}
	
	/**
	 * Tests for sleeping status.
	 */
	@Test
	public void testSleepOnFllHP() {


		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		testPlayer.damage(50, null);
		testPlayer.setPosition(10, 10);
		Item sleepingBag = SingletonRepository.getEntityManager().getItem("sleeping bag");
		testPlayer.equip("bag", sleepingBag);
		zone = new StendhalRPZone("zone");
		zone.add(testPlayer);
		
		sleepingBag.onUsed(testPlayer);
		//check to make sure the player is asleep
		assertTrue(testPlayer.hasStatus(StatusType.SLEEPING));
		
		//create a listener for this player
		SleepStatusTurnListener listener = new SleepStatusTurnListener(testPlayer.getStatusList());
		//make sure he has time to reach full hp
		while (testPlayer.getHP() < testPlayer.getBaseHP())
			listener.onTurnReached(2);
		//take one more loop to trigger his full hp boolean
		listener.onTurnReached(2);

		assertFalse(testPlayer.hasStatus(StatusType.SLEEPING));

	}
	
	/**
	 * Tests for sleeping status.
	 */
	@Test
	public void testSleepOnFullAmmount() {


		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		testPlayer.damage(75, null);
		testPlayer.setPosition(10, 10);
		Item sleepingBag = SingletonRepository.getEntityManager().getItem("sleeping bag");
		testPlayer.equip("bag", sleepingBag);
		zone = new StendhalRPZone("zone");
		zone.add(testPlayer);
		
		sleepingBag.onUsed(testPlayer);
		//check to make sure the player is asleep
		assertTrue(testPlayer.hasStatus(StatusType.SLEEPING));
		
		//create a listener for this player
		SleepStatusTurnListener listener = new SleepStatusTurnListener(testPlayer.getStatusList());
		//make sure he has time to reach full hp
		int ammount = listener.getAmmount();
		int frequency = listener.getFrequency();
		System.out.println("" + testPlayer.getHP() + " " + ammount);

		//loop depending on the frequency and max heal ammount
		while (ammount > 0) {
			listener.onTurnReached(frequency);
			ammount--;

		}
		//check to make sure the player isnt sleeping anymore
		assertFalse(testPlayer.hasStatus(StatusType.SLEEPING));

	}
	
	/**
	 * Tests for sleeping status.
	 */
	@Test
	public void testPlayerNotAbleToMoveWhileAsleep() {

		final Player testPlayer = PlayerTestHelper.createPlayer("testPlayer");
		testPlayer.setHP(50);
		testPlayer.setPosition(10, 10);
		Item sleepingBag = SingletonRepository.getEntityManager().getItem("sleeping bag");
		
		testPlayer.equip("bag", sleepingBag);
		zone = new StendhalRPZone("zone");
		zone.add(testPlayer);
		
		sleepingBag.onUsed(testPlayer);
		assertTrue(testPlayer.hasStatus(StatusType.SLEEPING));
		
		//create a path for the player and make the player move
		final List<Node> nodes = new LinkedList<Node>();
			nodes.add(new Node(10, 10));
			nodes.add(new Node(10, 9));
		testPlayer.setSpeed(10);
		testPlayer.setPath(new FixedPath(nodes, false));
		testPlayer.applyMovement();
		zone.modify(testPlayer);
		
		//check if he is still as the start position
		assertTrue(testPlayer.getX() == 10);
		assertTrue(testPlayer.getY() == 10);
		
	}
	
	
}
