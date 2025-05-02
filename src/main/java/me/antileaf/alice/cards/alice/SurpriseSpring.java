package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class SurpriseSpring extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SurpriseSpring.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int DRAW = 1;
	private static final int HEAL = 2;
	private static final int UPGRADE_PLUS_DRAW = 1;
	private static final int UPGRADE_PLUS_HEAL = 1;
	
	public SurpriseSpring() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.SELF
		);
		
		this.magicNumber = this.baseMagicNumber = HEAL;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = DRAW;
		this.exhaust = true;
		this.tags.add(CardTags.HEALING);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainEnergyAction(this.upgraded ? 2 : 1));
		this.addToBot(new HealAction(p, p, this.magicNumber));
		this.addToBot(new DrawCardAction(
				this.secondaryMagicNumber,
				new AnonymousAction(() -> {
					if (!DrawCardAction.drawnCards.isEmpty()) {
						for (AbstractCard card : DrawCardAction.drawnCards)
							this.addToTop(new UpgradeSpecificCardAction(card));
					}
				})
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SurpriseSpring();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_DRAW);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
