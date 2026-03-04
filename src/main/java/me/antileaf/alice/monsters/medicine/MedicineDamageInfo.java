package me.antileaf.alice.monsters.medicine;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class MedicineDamageInfo extends DamageInfo {
	public int index;
	
	public MedicineDamageInfo(int index, AbstractCreature source, int base) {
		super(source, base);
		
		this.index = index;
	}
}
