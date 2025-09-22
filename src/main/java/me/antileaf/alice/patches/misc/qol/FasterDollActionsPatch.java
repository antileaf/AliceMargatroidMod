package me.antileaf.alice.patches.misc.qol;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.ArrayList;
import java.util.WeakHashMap;

public class FasterDollActionsPatch {
	private static final WeakHashMap<AbstractGameAction, Boolean> dollActions = new WeakHashMap<>();
	
	public static void addAll(ArrayList<AbstractGameAction> actions) {
		for (AbstractGameAction action : actions)
			dollActions.put(action, true);
	}
	
	
}
