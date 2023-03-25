/***************************************************************************
 *                   (C) Copyright 2003-2016 - Marauroa                    *
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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.core.events.TurnNotifier;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.creature.impl.DropItem;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;
import marauroa.common.game.SyntaxException;

/**
 * A Thieving monkey is a domestic animal that can be owned by a player.
 * <p>
 * It eats bananas from the ground.
 * <p>
 * They move much faster than sheep
 * <p>
 * Thieving monkey attack animals which attack them
 * Thieving monkeys also steal items from other players and creatures
 *
 * @author Abdullah (based on sheep by Daniel Herding)
 *
 */
public class ThievingMonkey extends Pet {

	/** the logger instance. */
	private static final Logger logger = Logger.getLogger(ThievingMonkey.class);

	@Override
	void setUp() {
		//initial stats
		HP = 500;
		incHP = 6;
		lv_cap = 6;
		ATK = 15;
		DEF = 40;
		XP = 100;
		baseSpeed = 0.9;
		weight = 1;

		setAtk(ATK);
		setDef(DEF);
		setXP(XP);
		setBaseHP(HP);
		setHP(HP);
		setWeight(weight);


	}

	public static void generateRPClass() {
		try {
			final RPClass thieving_monkey = new RPClass("thieving_monkey");
			thieving_monkey.isA("pet");
		} catch (final SyntaxException e) {
			logger.error("cannot generate RPClass", e);
		}
	}

	/**
	 * Creates a new wild thieving monkey.
	 */
	public ThievingMonkey() {
		this(null);
	}

	/**
	 * Creates a new thieving monkey that may be owned by a player.
	 * @param owner The player who should own the thieving monkey
	 */
	public ThievingMonkey(final Player owner) {
		super();
		setOwner(owner);
		setUp();
		setRPClass("thieving_monkey");
		put("type", "thieving_monkey");

		if (owner != null) {
			// add pet to zone and create RPID to be used in setPet()
			owner.getZone().add(this);
			owner.setPet(this);
		}

		update();
	}

	/**
	 * Creates a thieving monkey based on an existing pet RPObject, and assigns it
	 * to a player.
	 *
	 * @param object object containing the data for the monkey
	 * @param owner The player who should own the thieving monkey
	 */
	public ThievingMonkey(final RPObject object, final Player owner) {
		super(object, owner);

		setRPClass("thieving_monkey");
		put("type", "thieving_monkey");

		update();
	}

	@Override
	protected
	List<String> getFoodNames() {
		return Arrays.asList("banana");
	}

	// Boolean that controls whether the monkey is ready to steal
	int stealEnabled = 1;

	@Override
	public void logic() {

		// Update whether the monkey is ready to steal
		updateStealEnabled();

		// Get closest entity within movement range (pass owner too exclude from results)
		RPEntity entity = getNearestRPEntity(getMovementRange(), owner);

		// If steal is enabled and there is an NPC targeted, go to and/or steal from them
		if ((stealEnabled == 1) && entity != null) {
			this.setIdea("stealing");
			this.setMovement(entity, 0, 0, getMovementRange());
			this.applyMovement();
			notifyWorldAboutChanges();

			// If the monkey is next to the NPC then steal, disable stealing
			if (nextTo(entity)) {
				steal(entity);
				stealEnabled = 0;
				entity = null;
				this.setIdea(null);
			}
		}
		else { super.logic(); } // If can't move to a target, then super.logic to do something else

	}

	// Updates steal enabled to be true after x time (or turns)
	public void updateStealEnabled() {
		if(stealEnabled == 0) {

			stealEnabled = 2;

			TurnNotifier.get().notifyInSeconds(8, new TurnListener() {
				@Override
				public void onTurnReached(int currentTurn) {
					stealEnabled = 1;
//					TurnNotifier.get().dontNotify(this);
				}
			});
		}
	}

	// Manages how the monkey steals from different entities
	public void steal(RPEntity entity) {
		if (entity instanceof Creature) {
			// Steals an item from the creatures drop-able loot
			List<DropItem> itemList = ((Creature) entity).dropsItems;
			Random rand = new Random();
			String itemName = itemList.get(rand.nextInt(itemList.size())).name;
			Item item = SingletonRepository.getEntityManager().getItem(itemName);
			checkEquipped(owner.equipToInventoryOnly(item), item);
		}
		else if (entity instanceof Player) {
			// Steals the 1st item in bag with 1% chance
//			int num = (new Random()).nextInt(100);
//			if (num == 1) {
			Item ring = (Item) entity.getSlot("bag").getFirst();
			if (ring != null) {
				entity.drop(ring);
				ring.getName();
				ring.removeFromWorld();
				checkEquipped(owner.equipToInventoryOnly(ring), ring);
			}
//				}
					
		}
		else if (!(entity instanceof Creature)) {
			// Steals money from NPCs
			Item money = SingletonRepository.getEntityManager().getItem("money");
			checkEquipped(owner.equipToInventoryOnly(money), money);
		}
	}
	
	public void checkEquipped(boolean equipped, Item item) {
		if(equipped) {
			owner.sendPrivateText(this.getName() + " stole " + item.getName() + " and put it in your inventory!");
		}
		else {
			owner.sendPrivateText(this.getName() + " stole " + item.getName() + " but it doesn't fit in your inventory!");
		}
	}
}

