package rs.antileaf.alice.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AliceRayHelper {
	private static AliceRayHelper instance = null;
	
	public static AliceRayHelper get() {
		if (instance == null)
			instance = new AliceRayHelper();
		
		return instance;
	}
	
	private ArrayList<AbstractCard> rayCardPlayedThisTurn = new ArrayList<>();
	private ArrayList<AbstractCard> rayCardPlayedThisCombat = new ArrayList<>();
	
	AliceRayHelper() {}
	
	public void addCard(AbstractCard card) {
		this.rayCardPlayedThisTurn.add(card);
		this.rayCardPlayedThisCombat.add(card);
	}
	
	public void clearTurn() {
		this.rayCardPlayedThisTurn.clear();
	}
	
	public void clearCombat() {
		this.rayCardPlayedThisCombat.clear();
	}
	
	public boolean hasPlayedThisTurn() {
		return !this.rayCardPlayedThisTurn.isEmpty();
	}
	
	public ArrayList<AbstractCard> getRayCardPlayedThisTurn() {
		return this.rayCardPlayedThisTurn;
	}
	
	public ArrayList<AbstractCard> getRayCardPlayedThisCombat() {
		return this.rayCardPlayedThisCombat;
	}
	
	public AbstractCard getLastRayCardPlayedThisTurn() {
		if (this.rayCardPlayedThisTurn.isEmpty())
			return null;
		
		return this.rayCardPlayedThisTurn.get(this.rayCardPlayedThisTurn.size() - 1);
	}
	
	public AbstractCard getLastRayCardPlayedThisCombat() {
		if (this.rayCardPlayedThisCombat.isEmpty())
			return null;
		
		return this.rayCardPlayedThisCombat.get(this.rayCardPlayedThisCombat.size() - 1);
	}
}
