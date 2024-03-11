package rs.antileaf.alice.cards;

import rs.antileaf.alice.AliceMagtroidMod;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.utils.AliceSpireKit;

import static rs.antileaf.alice.AliceMagtroidMod.ALICE_PUPPETEER_FLAVOR;

public abstract class AbstractAliceCard extends CustomCard
		implements SpawnModificationCard {
	protected static final Color CYAN_COLOR = new Color(0f, 204f / 255f, 0f, 1f);

	public boolean cantBePlayed = false;
	public boolean isSupplement = false;
	public int tempHP = 0;
	public int baseTempHP = 0;
	
	public AbstractAliceCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			AbstractCard.CardType type,
			AbstractCard.CardColor color,
			AbstractCard.CardRarity rarity,
			AbstractCard.CardTarget target
	) {
		super(
				id,
				name,
				AliceSpireKit.getCardImgFilePath("Th123Cirno"),
				cost,
				rawDescription,
				type,
				color,
				rarity,
				target
		);
		
		FlavorText.AbstractCardFlavorFields.boxColor.set(this, ALICE_PUPPETEER_FLAVOR);
	}

	@Override
	public void triggerWhenDrawn() {
		if (this.isSupplement)
			this.addToTop(new DrawCardAction(1));

		super.triggerWhenDrawn();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
	}

	@Override
	 public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return !this.cantBePlayed && super.canUse(p, m);
	}

	@Override
	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractAliceCard card = (AbstractAliceCard) super.makeStatEquivalentCopy();

		card.cantBePlayed = this.cantBePlayed;
		card.isSupplement = this.isSupplement;

		return card;
	}

	public void triggerOnLeaveHand(boolean isExhaust, boolean isEndOfTurn) {

	}

	public void triggerOnLeaveHand(boolean isExhaust) {
		this.triggerOnLeaveHand(isExhaust, false);
	}

	@Override
	public void triggerOnExhaust() {
		this.triggerOnLeaveHand(true);
		super.triggerOnExhaust();
	}

	@Override
	public void triggerOnManualDiscard() {
		this.triggerOnLeaveHand(false, false);
		super.triggerOnManualDiscard();
	}

	@Override
	public void triggerOnEndOfPlayerTurn() {
		if (!this.retain)
			this.triggerOnLeaveHand(false, true);
		
		super.triggerOnEndOfPlayerTurn();
	}

	public void addActionsToTop(AbstractGameAction... actions) {
		AliceSpireKit.addActionsToTop(actions);
	}

	public String bracketedName() {
		return "(" + this.name + ")";
	}
}
