package rs.antileaf.alice.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;

public class ConcentrationPotion extends CustomPotion {
	public static final String SIMPLE_NAME = ConcentrationPotion.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
	
	private static final int POTENCY = 1;
	
	public ConcentrationPotion() {
		super(
				potionStrings.NAME,
				ID,
				PotionRarity.RARE,
				PotionSize.H,
				PotionColor.WEAK
		);
		
		this.labOutlineColor = CardHelper.getColor(255,215,0);
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
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot))
				for (int i = 0; i < this.potency; i++)
					this.addToBot(new DollActAction(doll));
	}
	
	@Override
	public int getPotency(int ascensionLevel) {
		return POTENCY;
	}
	
	@Override
	public AbstractPotion makeCopy() {
		return new ConcentrationPotion();
	}
}
