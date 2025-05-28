package me.antileaf.alice.powers.unique;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnCreateCardInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.cardmodifier.ForbiddenMagicVFXCardModifier;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class ForbiddenMagicPower extends AbstractAlicePower implements OnCreateCardInterface {
	public static final String SIMPLE_NAME = ForbiddenMagicPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public static final Texture ICON = new Texture(AliceHelper.getImgFilePath("UI", "ForbiddenMagic"));

	public ForbiddenMagicPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard ac = (AbstractAliceCard) card;

			if (ac.dollTarget && ac.getTargetedDoll() != null) {
				this.flash();

				if (!ac.hardCodedForbiddenMagic) {
					AbstractDoll doll = ac.getTargetedDoll();

					for (int i = 0; i < this.amount; i++)
						this.addToBot(new DollActAction(doll));
				}

				action.exhaustCard = true;
			}
		}
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer)
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}

	@Override
	public void onInitialApplication() {
		for (CardGroup group : new CardGroup[] {
				AbstractDungeon.player.hand,
				AbstractDungeon.player.drawPile,
				AbstractDungeon.player.discardPile,
				AbstractDungeon.player.exhaustPile,
				AbstractDungeon.player.limbo
		})
			for (AbstractCard card : group.group)
				if (card instanceof AbstractAliceCard && ((AbstractAliceCard) card).dollTarget)
					CardModifierManager.addModifier(card, new ForbiddenMagicVFXCardModifier());
	}

	@Override
	public void onCreateCard(AbstractCard card) {
		if (card instanceof AbstractAliceCard && ((AbstractAliceCard) card).dollTarget)
			CardModifierManager.addModifier(card, new ForbiddenMagicVFXCardModifier());
	}
}
