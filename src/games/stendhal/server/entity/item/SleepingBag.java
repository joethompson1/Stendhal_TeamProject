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
package games.stendhal.server.entity.item;


import java.util.Map;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.status.SleepStatus;


public class SleepingBag extends Item {


	public SleepingBag(final String name, final String clazz,
            final String subclass, final Map<String, String> attributes) {
        super(name, clazz, subclass, attributes);
 
    }
	
	@Override
	public void put(final String attribute, final double value) {
		super.put(attribute, value);
	}
	
	
    public SleepingBag(final SleepingBag item) {
        super(item);
    }

    @Override
	public boolean onUsed(final RPEntity user) {
    	//drops the item to ground
    	user.drop(this);
		this.setPosition(user.getX(), user.getY());
		//gets a new item to put the item on the ground
		Item sleepingBag = SingletonRepository.getEntityManager().getItem("sleeping bag");
		sleepingBag.setPosition(user.getX(), user.getY());
		user.getZone().add(sleepingBag);
    	//inflict the sleep status
		SleepStatus status = new SleepStatus();
		return user.getStatusList().inflictStatus(status, null);

    }
}
