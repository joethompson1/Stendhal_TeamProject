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

import games.stendhal.common.NotificationType;
import games.stendhal.server.core.events.TurnNotifier;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.RPEntity;

/**
 * handles eating
 */
public class SleepStatusHandler implements StatusHandler<SleepStatus> {

	
	/**
	 * inflicts a status
	 *
	 * @param status Status to inflict
	 * @param statusList StatusList
	 * @param attacker the attacker
	 */
	@Override
	public void inflict(SleepStatus status, StatusList statusList, Entity attacker) {
		
		
		
		
		if (!statusList.hasStatus(status.getStatusType())) {
			RPEntity entity = statusList.getEntity();
			if (entity != null) {
				if (attacker == null) {
					entity.sendPrivateText(NotificationType.SCENE_SETTING, "You have fallen asleep.");
				} else {
					entity.sendPrivateText(NotificationType.SCENE_SETTING, "You have fallen asleep.");
				}
			}
		}
		
		int count = statusList.countStatusByType(status.getStatusType());
		if (count <= 6) {
			statusList.addInternal(status);
		}

		if (count == 0) {
			statusList.activateStatusAttribute("status_" + status.getName());
			TurnNotifier.get().notifyInSeconds(60, new StatusRemover(statusList, status));
			TurnNotifier.get().notifyInTurns(0, new SleepStatusTurnListener(statusList));
		}
		

		// activate the turnListener, if this is the first instance of this status
		// note: the turnListener is called one last time after the last instance was consumed to cleanup attributes.
		// So even with count==1, there might still be a listener which needs to be removed
		
	}

	/**
	 * removes a status
	 *
	 * @param status Status to inflict
	 * @param statusList StatusList
	 */
	@Override
	public void remove(SleepStatus status, StatusList statusList) {
		statusList.removeInternal(status);

		RPEntity entity = statusList.getEntity();
		if (entity == null) {
			return;
		}

		Status nextStatus = statusList.getFirstStatusByClass(SleepStatus.class);
		if (nextStatus != null) {
			TurnNotifier.get().notifyInSeconds(60, new StatusRemover(statusList, nextStatus));
		} else {
			entity.sendPrivateText(NotificationType.SCENE_SETTING, "You are no asleep.");
			entity.remove("status_" + status.getName());
		}
	}
}
