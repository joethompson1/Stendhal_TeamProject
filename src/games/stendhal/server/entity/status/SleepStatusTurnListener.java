/***************************************************************************
 *                (C) Copyright 2005-2013 - Faiumoni e. V.                 *
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

import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.core.events.TurnNotifier;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;

/**
 * eating turn listener
 */
public class SleepStatusTurnListener implements TurnListener {
	private StatusList statusList;
	
	private int frequency = 2 ;
	private int regen = 1;
	private int ammount = 50;
	private int hpRestored = 0;


	/**
	 * EatStatusTurnListener
	 *
	 * @param statusList StatusList
	 */
	public SleepStatusTurnListener(StatusList statusList) {
		this.statusList = statusList;
	}

	@Override
	public void onTurnReached(int turn) {
		RPEntity entity = statusList.getEntity();
		SleepStatus status = statusList.getFirstStatusByClass(SleepStatus.class);
		
		
		// check that the entity exists
		if (entity == null || status == null) {
			return;
		}
		//check if full hp
		boolean isFullHP = false;
		
		
		// make sure the player cannot move while asleep
		if (entity instanceof Player) {
			((Player) entity).forceStop();
		} else {
			entity.stop();
		}
		entity.clearPath();
		
		
		//heal every frequency number of turns
		if (turn % frequency == 0) {
			
			int currentRestore = entity.heal(regen);
			hpRestored += currentRestore;
			if (currentRestore == 0 )
				isFullHP = true;
			

		}
		
		// if you get attacked you wake up or are full HP or you have reached the total amomount it heals
		if (!entity.getAttackingRPEntities().isEmpty() || isFullHP || hpRestored >= ammount) {
			statusList.remove(status);
			entity.remove("status_" + status.getName());
			
		}




		TurnNotifier.get().notifyInTurns(0, this);
	}
	
	public int getAmmount() {
		return ammount;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public int getRegen() {
		return regen;
	}

	

	@Override
	public int hashCode() {
		return statusList.hashCode() * 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		SleepStatusTurnListener other = (SleepStatusTurnListener) obj;
		return statusList.equals(other.statusList);
	}

}
