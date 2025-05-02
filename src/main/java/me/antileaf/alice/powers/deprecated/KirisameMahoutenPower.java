package me.antileaf.alice.powers.deprecated;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.powers.AbstractAlicePower;

import java.util.ArrayList;
import java.util.Collections;

public class KirisameMahoutenPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = KirisameMahoutenPower.class.getSimpleName();
	public static final String POWER_ID = SIMPLE_NAME;
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	ArrayList<String> potionNames;
	
	public KirisameMahoutenPower(String potionName) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		
		this.potionNames = new ArrayList<>(Collections.singleton(potionName));
		this.amount = this.potionNames.size();
		this.updateDescription();
		this.initializeImage(null);
	}
	
	public void addPotion(String potionName) {
		this.potionNames.add(potionName);
		this.amount = this.potionNames.size();
		this.updateDescription();
	}
	
	@Override
	public void updateDescription() {
		StringBuilder sb = new StringBuilder(powerStrings.DESCRIPTIONS[0]);
		
		for (String s: this.potionNames) {
			if (s != this.potionNames.get(0)) // It is correct to compare references here.
				sb.append(powerStrings.DESCRIPTIONS[1]);
			sb.append(s);
		}
		
		this.description = sb.append(powerStrings.DESCRIPTIONS[2]).toString();
	}
}
