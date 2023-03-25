package games.stendhal.server.maps.quests;


import games.stendhal.server.entity.npc.*;
//import games.stendhal.server.entity.npc.action.*;
//import games.stendhal.server.entity.npc.condition.*;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;

import java.util.*;


public class LonJathamRecruitingStudents extends AbstractQuest {
	public static final String QUEST_SLOT = "LonJathamRecruitingStudents";
	
	
	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		int x;
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I have talked to Lon Jatham.");
		final String questState = player.getQuest(QUEST_SLOT);
		
		if ("rejected".equals(questState)) {
			res.add("I do not want to help Lon Jatham find 3 students");
		}
		if (player.isQuestInState(QUEST_SLOT, "start", "done")) {
			res.add("I promised to find 3 students for Lon Jatham");
		}
		if (player.isQuestInState(QUEST_SLOT, "student1Signed")) {
			x = 1;
			res.add("Students found: " + x);
		}
		if (player.isQuestInState(QUEST_SLOT, "student2Signed")) {
			x = 2;
			res.add("Students found: " + x);
		}
		if (player.isQuestInState(QUEST_SLOT, "student3Signed")) {
			x = 3;
			res.add("Students found: " + x);
		}
		return res;
	}
	
	private void prepareRequestingStep() {
		// get a reference to the Lon Jatham NPC
		final SpeakerNPC npc = npcs.get("Lon Jatham");
		
		
		// if the player asks for a quest, got to state QUEST_OFFERED
		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				new QuestNotCompletedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED,
				"I only need another 3 students to fill my course, will you help me find the remaining 3",
				null);
		
		// Player declines quest
		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES,
				new QuestNotCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Oh, well forget it then, I guess I can't impart some of my wisdom onto all these beautiful young students.",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));
		
		// in state QUEST_OFFERED, accept 'yes' and got to state QUEST_STARTED
		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES,
				new QuestNotCompletedCondition(QUEST_SLOT),
				ConversationStates.QUEST_STARTED,
				"Thanks! I'll be right here, waiting, waiting for all those beautiful students.",
				new MultipleActions(new  SetQuestAction(QUEST_SLOT, "start"), new EquipItemAction("prospectus"), new EquipItemAction("prospectus"), new EquipItemAction("prospectus")));
		
		
		// Quest already completed
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Thanks all the same, but I have got enough students on my hands, even for me. Come back next year to help me again",
				null);
		
		
		
	}
	
	private void student1() {
		SpeakerNPC npc = npcs.get("Jacob");
		
		
		npc.add(ConversationStates.IDLE,
				ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new QuestInStateCondition(QUEST_SLOT,"start")),
				ConversationStates.ATTENDING,
				"Hello! I sure wish there was a technology #course available nearby",
				null);
		
		npc.add(ConversationStates.ATTENDING,
				"course",
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Oh there is? Can you find out the answer to 'what type of AI will we study?'",
				null);
		
		npc.add(ConversationStates.ATTENDING,
				"symbolic AI",
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new PlayerHasItemWithHimCondition("prospectus"), new QuestInStateCondition(QUEST_SLOT, "start")),
				ConversationStates.IDLE,
				"Great! Sign me up!",
				new MultipleActions(new SetQuestAction(QUEST_SLOT, "student1Signed"), new DropItemAction("prospectus")));
		
		npc.add(ConversationStates.ATTENDING,
				"symbolic AI",
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new NotCondition(new PlayerHasItemWithHimCondition("prospectus"))),
				ConversationStates.IDLE,
				"Great, I'd love to sign up but  I do need a prospectus",
				null);
		
	}
	
	private void student2() {
		SpeakerNPC npc = npcs.get("Michael");
		
		
		npc.add(ConversationStates.IDLE,
				ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new QuestInStateCondition(QUEST_SLOT,"student1Signed")),
				ConversationStates.ATTENDING,
				"Hey, I'm bored, I wish I could learn #programming somewhere...",
				null);
		
		npc.add(ConversationStates.ATTENDING,
				"programming",
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"I can? Fab can you find out 'Do we program in python?' and if not what we do learn!",
				null);
		
		npc.add(ConversationStates.ATTENDING,
				"java",
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new PlayerHasItemWithHimCondition("prospectus")),
				ConversationStates.IDLE,
				"Thats fine, Sign me up.",
				new MultipleActions(new SetQuestAction(QUEST_SLOT, "student2Signed"), new DropItemAction("prospectus")));	
		
		npc.add(ConversationStates.ATTENDING,
				"symbolic AI",
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new NotCondition(new PlayerHasItemWithHimCondition("prospectus"))),
				ConversationStates.IDLE,
				"That's fine, I'd sign up but  I do need a prospectus",
				null);
		
	}
	
	private void student3() {
		SpeakerNPC npc = npcs.get("James");
		
		
		npc.add(ConversationStates.IDLE,
				ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new QuestInStateCondition(QUEST_SLOT,"student2Signed")),
				ConversationStates.ATTENDING,
				"Hiya! I wonder where I could learn #computer things",
				null);
		
		npc.add(ConversationStates.ATTENDING,
				"computer",
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Oh cool, I'm just a bit worried about one thing. Can you find out 'Are the hawaiian shirts optional?'",
				null);
		
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.YES_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new PlayerHasItemWithHimCondition("prospectus")),
				ConversationStates.IDLE,
				"Awesome, sign me up!",
				new MultipleActions(new SetQuestAction(QUEST_SLOT, "student3Signed"), new DropItemAction("prospectus")));	
		
		npc.add(ConversationStates.ATTENDING,
				"symbolic AI",
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new NotCondition(new PlayerHasItemWithHimCondition("prospectus"))),
				ConversationStates.IDLE,
				"Awesome, all I need now is a prospectus!",
				null);
		
	}
	private void question1() {
		SpeakerNPC npc = npcs.get("Lon Jatham");
		
		// Question 1
		npc.add(ConversationStates.IDLE,
				"What type of AI will we study?",
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.IDLE,
				"We will be learning symbolic AI. Now hurry off and find me those students",
				null);
		
	}
	
	private void question2() {
		SpeakerNPC npc = npcs.get("Lon Jatham");
		
		// Question 2
		npc.add(ConversationStates.IDLE,
				"Are the hawaiian shirts optional?",
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.IDLE,
				"Of course they are optional. Its called FASHUN, look it up!",
				null);
	}
	
	private void question3() {
		SpeakerNPC npc = npcs.get("Lon Jatham");
		
		// Question 3
		npc.add(ConversationStates.IDLE,
				"Do we program in python?",
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.IDLE,
				"No! We will learn Java! The far more superior language, now hurry off and find me those students",
				null);
	}
	
	private void prepareBringingStep() {
		SpeakerNPC npc = npcs.get("Lon Jatham");
		
		// if the player says "student log" and 3 students names are on the log, we set the quest slot to "done"
		npc.add(ConversationStates.IDLE,
				"finished",
				new AndCondition(new QuestActiveCondition(QUEST_SLOT), new QuestInStateCondition(QUEST_SLOT,"student3Signed")),
				ConversationStates.ATTENDING,
				"Ahhhh, you found enough students to fill up my course, trust me when I say these students will come in handy!",
				new MultipleActions(new SetQuestAction(QUEST_SLOT, "done"), new EquipItemAction("pizza", 25), new IncreaseXPAction(50), new IncreaseKarmaAction(10)));
		
	}
	
	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Lon Jatham Recruiting Students",
				"Find 3 students who want to enroll onto Lon Jathams course.",
				false);
		prepareRequestingStep();
		student1();
		student2();
		student3();
		question1();
		question2();
		question3();
		prepareBringingStep();
	}
	
	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	
	@Override
	public String getName() {
		return "LonJathamRecruitingStudents";
	}
	
	public String getTitle() {
		return "Recruiting Students for Lon Jatham";
	}
	
	@Override
	public int getMinLevel() {
		return 0;
	}
	
	@Override
	public String getNPCName() {
		return "Lon Jatham";
	}
}






































