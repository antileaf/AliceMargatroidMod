package rs.antileaf.alice.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.utils.AliceHelper;

public class WeavingPotion extends CustomPotion {
	public static final String SIMPLE_NAME = WeavingPotion.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
	
	private static final int POTENCY = 2;
	
	public WeavingPotion() {
		super(
				potionStrings.NAME,
				ID,
				PotionRarity.UNCOMMON,
				PotionSize.SPHERE,
				PotionColor.SWIFT
		);
		
		this.labOutlineColor = AliceMargatroidMod.ALICE_IMPRESSION_COLOR.cpy();
	}
	
	@Override
	public void initializeData() {
		this.potency = this.getPotency();
		this.description = String.format(potionStrings.DESCRIPTIONS[0], this.potency);
		
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
	}
	
	@Override
	public void use(AbstractCreature target) {
		if (AliceHelper.isInBattle()) {
			for (int i = 0; i < this.potency; i++)
				this.addToBot(new SpawnDollAction(AbstractDoll.getRandomDoll(), -1));
		}
	}
	
	@Override
	public int getPotency(int ascensionLevel) {
		return POTENCY;
	}
	
	@Override
	public AbstractPotion makeCopy() {
		return new WeavingPotion();
	}
}
