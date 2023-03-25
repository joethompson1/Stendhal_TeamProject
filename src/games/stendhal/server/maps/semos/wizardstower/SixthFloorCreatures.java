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
package games.stendhal.server.maps.semos.wizardstower;

import java.util.HashMap;
import java.util.Map;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.rule.EntityManager;
import games.stendhal.server.entity.creature.Creature;

public class SixthFloorCreatures implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildSixthFloor(zone);
	}

	private void buildSixthFloor(final StendhalRPZone zone) {
		final EntityManager manager = SingletonRepository.getEntityManager();

		final Creature creature = manager.getCreature("littlefairy");
		final Creature creature1 = manager.getCreature("pegasus");
		final Creature creature2 = manager.getCreature("unicorn");
		final Creature creature3 = manager.getCreature("archangel");

		creature.setAIProfiles(new HashMap<String, String>());
		creature1.setAIProfiles(new HashMap<String, String>());
		creature2.setAIProfiles(new HashMap<String, String>());
		creature3.setAIProfiles(new HashMap<String, String>());

		creature.clearDropItemList();
		creature1.clearDropItemList();
		creature2.clearDropItemList();
		creature3.clearDropItemList();

		creature.setXP(0);
		creature1.setXP(0);
		creature2.setXP(0);
		creature3.setXP(0);

		creature.setPosition(15,28);
		creature1.setPosition(29,15);
		creature2.setPosition(1,15);
		creature3.setPosition(14,2);

		creature.setDirection(Direction.UP);
		creature1.setDirection(Direction.LEFT);
		creature2.setDirection(Direction.RIGHT);

		// Claim these spawned creatures to hide them from
		// /script EntitySearch.class nonrespawn
		creature.setRespawned(true);
		creature1.setRespawned(true);
		creature2.setRespawned(true);
		creature3.setRespawned(true);

		zone.add(creature);
		zone.add(creature1);
		zone.add(creature2);
		zone.add(creature3);
	}
}
