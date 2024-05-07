package rs.antileaf.alice.powers.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.doll.AbstractDoll;

public class DollPotion extends CustomPotion {
	public static final String SIMPLE_NAME = DollPotion.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
	
	private static final int POTENCY = 2;
	
	public DollPotion() {
		super(
				potionStrings.NAME,
				ID,
				PotionRarity.UNCOMMON,
				PotionSize.SPHERE,
				PotionColor.SWIFT
		);
		
		this.potency = this.getPotency();
		this.description = String.format(potionStrings.DESCRIPTIONS[0], this.potency);
		
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
	}
	
	@Override
	public void use(AbstractCreature target) {
		for (int i = 0; i < this.potency; i++)
			this.addToBot(new SpawnDollAction(AbstractDoll.getRandomDoll(), -1));
	}
	
	@Override
	public int getPotency(int i) {
		return POTENCY;
	}
	
	@Override
	public AbstractPotion makeCopy() {
		return new DollPotion();
	}
}
