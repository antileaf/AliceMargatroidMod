package me.antileaf.alice.cards.colorless;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.RecycleDollAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.utils.AliceHelper;

public class Retrace extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Retrace.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int DRAW = 2;
	private static final int UPGRADE_PLUS_DRAW = 1;

	public Retrace() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS, // AbstractCardEnum.ALICE_MARGATROID_DERIVATION_COLOR,
				CardRarity.SPECIAL,
				CardTargetEnum.DOLL
		);
		
		this.magicNumber = this.baseMagicNumber = DRAW;
		this.retain = this.selfRetain = true;
		this.exhaust = true;

		this.isCommandCard = true;
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = this.getTargetedDoll();
		
		if (doll != null) {
			this.addToBot(new RecycleDollAction(doll));
			this.addToBot(new DrawCardAction(this.magicNumber));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Retrace();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_DRAW);
			this.initializeDescription();
		}
	}
}
