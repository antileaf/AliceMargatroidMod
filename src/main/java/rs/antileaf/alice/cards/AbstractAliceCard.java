package rs.antileaf.alice.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

import static rs.antileaf.alice.AliceMargatroidMod.ALICE_PUPPETEER_FLAVOR;

public abstract class AbstractAliceCard extends CustomCard
		implements SpawnModificationCard {
//	protected static final CardStrings cardStrings =
//			CardCrawlGame.languagePack.getCardStrings("AbstractAliceCard");
	protected static final Color CYAN_COLOR = new Color(0f, 204f / 255f, 0f, 1f);

	public boolean cantBePlayed = false;
	public boolean isSupplement = false;
	public int tempHP = 0;
	public int baseTempHP = 0;
	public int secondaryMagicNumber = -1;
	public int baseSecondaryMagicNumber = -1;
	public boolean upgradedSecondaryMagicNumber = false;
	public boolean isSecondaryMagicNumberModified = false;
	
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
				img != null ? img :
						(type == CardType.ATTACK ? AliceSpireKit.getCardImgFilePath("Attack") :
								type == CardType.SKILL ? AliceSpireKit.getCardImgFilePath("Skill") :
										AliceSpireKit.getCardImgFilePath("Power")),
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
		if (this.cantBePlayed || !super.canUse(p, m))
			return false;
		
		if (this.target == CardTargetEnum.DOLL) {
			if (!DollManager.get().hasDoll()) {
				this.cantUseMessage = CardCrawlGame.languagePack.getCardStrings("AbstractAliceCard")
						.EXTENDED_DESCRIPTION[0];
				return false;
			}
		}
		
		return true;
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractAliceCard card = (AbstractAliceCard) super.makeStatEquivalentCopy();

		card.cantBePlayed = this.cantBePlayed;
		card.isSupplement = this.isSupplement;
		card.tempHP = this.tempHP;
		card.baseTempHP = this.baseTempHP;
		card.secondaryMagicNumber = this.secondaryMagicNumber;
		card.baseSecondaryMagicNumber = this.baseSecondaryMagicNumber;

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
	
	@Override
	public void displayUpgrades() {
		super.displayUpgrades();
		
		if (this.upgradedSecondaryMagicNumber) {
			this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
			this.isSecondaryMagicNumberModified = true;
		}
	}
	
	public void upgradeSecondaryMagicNumber(int amount) {
		this.baseSecondaryMagicNumber += amount;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
		this.upgradedSecondaryMagicNumber = true;
	}

	public void addActionsToTop(AbstractGameAction... actions) {
		AliceSpireKit.addActionsToTop(actions);
	}

	public String bracketedName() {
		return "(" + this.name + ")";
	}
}
