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
package games.stendhal.server.core.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import games.stendhal.server.core.rp.achievement.Achievement;
import games.stendhal.server.core.rp.achievement.Category;
import games.stendhal.server.core.rp.achievement.condition.KilledRareCreatureCondition;
import games.stendhal.server.core.rp.achievement.condition.KilledSharedAllCreaturesCondition;
import games.stendhal.server.core.rp.achievement.condition.KilledSoloAllCreaturesCondition;

import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.LevelGreaterThanCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasKilledNumberOfCreaturesCondition;
import games.stendhal.server.entity.npc.condition.PlayerLootedNumberOfItemsCondition;
import games.stendhal.server.entity.npc.condition.PlayerVisitedZonesInRegionCondition;

import games.stendhal.server.entity.npc.condition.QuestStateGreaterThanCondition;





public final class AchievementsXMLLoader extends DefaultHandler {

	/** the logger instance. */
	private static final Logger LOGGER = Logger.getLogger(AchievementsXMLLoader.class);

	/** base score for easy achievements */
	public static int EASY_BASE_SCORE = 1;

	/** base score for achievements of medium difficulty */
	public static int MEDIUM_BASE_SCORE = 2;

	/** base score for difficult achievements */
	public static int HARD_BASE_SCORE = 5;

	private String identifier;

	private String title;

	private String description;

	private String text;

	private int score;

	/** is this achievement visible? */
	private boolean active;
	
	private List<Achievement> list;
	
	private boolean conditionsTag;
	
	// New ===========================
	private List<String> params = new LinkedList<String>();
	
	private List<String> classes = new LinkedList<String>();
	
	private ChatCondition chatCondition;
	
	private Category category;



	public List<Achievement> load(final URI uri) throws SAXException {
		list = new LinkedList<Achievement>();
		// Use the default (non-validating) parser
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Parse the input
			final SAXParser saxParser = factory.newSAXParser();

			final InputStream is = AchievementsXMLLoader.class.getResourceAsStream(uri.getPath());

			if (is == null) {
				throw new FileNotFoundException("cannot find resource '" + uri
						+ "' in classpath");
			}
			try {
				saxParser.parse(is, this);
			} finally {
				is.close();
			}
		} catch (final ParserConfigurationException t) {
			LOGGER.error(t);
		} catch (final IOException e) {
			LOGGER.error(e);
			throw new SAXException(e);
		}

		return list;
	}

	@Override
	public void startDocument() {
		// do nothing
	}

	@Override
	public void endDocument() {
		// do nothing
	}
	
	@Override 
	public void startElement(final String namespaceURI, final String lName, final String qName,
			final Attributes attrs) {
		text = "";
		if (qName.equals("achievement")) {
			System.out.println("====== ACHIEVEMENT FOUND");
			identifier = attrs.getValue("identifier");
			System.out.println("Idnetifier: " + identifier.toString());
		} else if (qName.equals("score")) {
			System.out.println("====== SCORE FOUND");
			score = Integer.parseInt(attrs.getValue("value"));
		} else if (qName.equals("active")) {
			System.out.println("====== ACTIVE FOUND");
			active = Boolean.parseBoolean(attrs.getValue("value"));
		} else if (qName.equals("conditions")) {
			System.out.println("====== CONDITIONS FOUND");
			conditionsTag = true;
		} else if (conditionsTag) {
			if (qName.equals("classname")) { 
				System.out.println("====== CLASSNAME FOUND");
				classes.add(attrs.getValue(0)); 
				System.out.println("Classname: " + classes.get(0));
			}
			if (qName.equals("param")) { 
				System.out.println("====== PARAMS FOUND");
				params.add(attrs.getValue(0)); 
			}
		} 
	}
	
//	<achievement identifier=""></achievement>
//		<title value=""></title>
//		<category value=""></category>
//		<description value=""></description>
//		<socre value=""></score>
//		<active value=""></active>
//		<conditions>
//			<classname value=""></classname>
//			<param value=""></param>
//			  .
//			  .
//			  .
//			<param value=""></param>
//		</conditions>
//  </achievement>
	
	
	@Override
	public void endElement(final String namespaceURI, final String sName, final String qName) {
		if (qName.equals("achievement")) {
			
			System.out.println("====== ACHIEVEMENT END FOUND");
			
			// Create chat condition (and also set category)
			createChatCondition(classes, params);
			
			// Create the achievement
			final Achievement achievement = new Achievement(identifier, title, category, description, score, active, chatCondition);
			
			// Add achievement to list 
			list.add(achievement);	
			
			// Clear 'classes' and 'params'
			classes.clear();
			params.clear();
			

		} else if (qName.equals("conditions")) {
			conditionsTag = false;
			
//			// Create chat condition (and also set category)
//			createChatCondition(classes, params);
//			
//			// Create the achievement
//			final Achievement achievement = new Achievement(identifier, title, category, description, score, active, chatCondition);
//			
//			// Add achievement to list 
//			list.add(achievement);
			
			
		} else if (qName.equals("title")) {
			System.out.println("====== TITLE FOUND (end)");
			if (text != null) {
				title = text.trim();
			}
			System.out.println("Title: " + title);
		} else if (qName.equals("description")) {
			System.out.println("====== DESCRIPTION FOUND (end)");
			if (text != null) {
				description = text.trim();
			}
			System.out.println("Description: " + description);
		}
	}		
	
	@Override
	public void characters(final char[] buf, final int offset, final int len) {
		text = text + (new String(buf, offset, len)).trim() + " ";
	}
	
	
	// Function that returns a ChatCondition and sets the category
	public ChatCondition createChatCondition(List<String> classes, List<String> params) {	
		
		System.out.print("\n==========================================");
		System.out.print("\ncreateChatCondition()");
		System.out.print("\n==========================================");
		
		System.out.print("\n==========================================");
		boolean check = false;
		if (classes == null) { check = true; }
		System.out.print("\nClasses is null: " + check);
		System.out.println("Classes is : " + classes.get(0));

		System.out.print("\n==========================================");
		
		for (String clas : classes) {
			System.out.print("\n==========================================");
			System.out.print("\nClass size: " + classes.size());
			System.out.print("\nCurrent clas: " + clas);
			System.out.print("\n==========================================");
			
			// Check if AndCondition and if yes combine and conditions
//			if (clas.equals("AndCondition")) { 
//				return createAndCondition();  
//			}
			
			// Check all other non-ChatCondition dependent conditions
			// TODO: add try-catches in case people configure XML wrong
			
			// =========== AdosItemQuestAchievementsFactory ===========
			if (clas.equals("QuestStateGreaterThanCondition")) {
				
				System.out.println("ADOS RAN =====================");
				
				// Set category
				category = Category.QUEST_ADOS_ITEMS;
				
				// Params: String, int, int
				String param1 = params.get(0);
				int param2 = Integer.parseInt(params.get(1));
				int param3 = Integer.parseInt(params.get(2));
				
				return new QuestStateGreaterThanCondition(param1, param2, param3);
								
			}
			
			// =========== ExperienceAchievementFactory ===========
			else if (clas.equals("LevelGreaterThanCondition")) {
				// Set category
				category = Category.EXPERIENCE;
				
				// Params: int
				return new LevelGreaterThanCondition(Integer.parseInt(params.get(0)));
			}
			
			// =========== FightingAchievementFactory ===========
			else if (clas.equals("PlayerHasKilledNumberOfCreaturesCondition")) {
				// Set category
				category = Category.FIGHTING;;
				
				// Params: String, int
				// Params: int, String... (do not need, just use HashMap constructor)
				// Params: Map<String, Integer>
				if (params.size() == 2) {
					int param1 = Integer.parseInt(params.get(0));
					String param2 = params.get(1);
					return new PlayerHasKilledNumberOfCreaturesCondition(param1, param2);
				}
				else {
					int param1 = Integer.parseInt(params.get(0));
					
					HashMap<String, Integer> kills = new HashMap<String, Integer>();
					for (int i = 1; i < params.size() - 1; i++) {
						kills.put(params.get(i), new Integer(param1));
					}
					
					return new PlayerHasKilledNumberOfCreaturesCondition(kills);
				}
			}
			else if (clas.equals("KilledSoloAllCreaturesCondition")) {
				// Set category
				category = Category.FIGHTING;;
				
				// Params: n/a
				return new KilledSoloAllCreaturesCondition();
			}
			else if (clas.equals("KilledRareCreatureCondition")) {
				// Set category
				category = Category.FIGHTING;;
				
				return new KilledRareCreatureCondition();
			}
			else if (clas.equals("KilledSharedAllCreaturesCondition")) {
				// Set category
				category = Category.FIGHTING;;
				
				return new KilledSharedAllCreaturesCondition();
			}
			
			// =========== InteriorZoneAchievementFactory ===========
			else if (clas.equals("PlayerVisitedZonesInRegionCondition")) {
				// Set category
				category = Category.INTERIOR_ZONE;
				
				String param1 = params.get(0);
				boolean param2 = Boolean.parseBoolean(params.get(1));
				boolean param3 = Boolean.parseBoolean(params.get(2));
				return new PlayerVisitedZonesInRegionCondition(param1, param2, param3);
			}
			
			// =========== ItemAchievementFactory ===========
			else if (clas.equals("PlayerLootedNumberOfItemsCondition")) {
				// Set category
				category = Category.ITEM;
				
				int param1 = Integer.parseInt(params.get(0));
				
				LinkedList<String> looted = new LinkedList<String>();
				for (int i = 1; i < params.size() - 1; i++) {
					looted.add(params.get(i));
				}
				
				return new PlayerLootedNumberOfItemsCondition(param1, looted);
			}
			
			// =========== KirdnehItemAchievementFactory ===========
			else if (clas.equals("QuestStateGreaterThanCondition")) {
				// Set category
				category = Category.QUEST_KIRDNEH_ITEM;
				
				// Params: String, int, int
				String param1 = params.get(0);
				int param2 = Integer.parseInt(params.get(1));
				int param3 = Integer.parseInt(params.get(2));
				return new QuestStateGreaterThanCondition(param1, param2, param3);
			}


		}
		
		System.out.println("==========================================");
		System.out.println("   NULL CODE RAN, NO CHAT COND RETURNED   ");
		System.out.println("==========================================");
		return null;
	}
	
	public ChatCondition createAndCondition() {
		
		// Code to combine all the remaining conditions
			// Need to remove classes[0]
			// Need to remove params for classes[0] 
			//   (the number of conditions for AndCondition)
		
		return new AndCondition();
	}
	
	
}
