package rs.antileaf.alice.powers.unique;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.cards.loli.*;
import rs.antileaf.alice.powers.AbstractAlicePower;

public class DollWorkshopPower extends AbstractAlicePower {
	public static final String POWER_ID = DollWorkshopPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public static final String[] CHOICES = new String[] {
			VivaciousShanghaiDoll_Loli.ID,
			QuietHouraiDoll_Loli.ID,
			SpringKyotoDoll_Loli.ID,
			RedHairedNetherlandsDoll_Loli.ID,
			CharitableFranceDoll_Loli.ID,
			MistyLondonDoll_Loli.ID,
			CharismaticOrleansDoll_Loli.ID
	};
	
	public DollWorkshopPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		if (this.amount <= 1)
			this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
		else
			this.description = String.format(powerStrings.DESCRIPTIONS[1], this.amount);
	}
	
	@Override
	public void atStartOfTurn() {
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.flash();
			
			for(int i = 0; i < this.amount; i++) {
				AbstractCard card = CardLibrary.getCard(
						CHOICES[AbstractDungeon.cardRandomRng.random(CHOICES.length - 1)]).makeCopy();
				CardModifierManager.addModifier(card, new ExhaustMod());

				this.addToBot(new MakeTempCardInHandAction(card, 1, false));
			}
		}
	}
}
