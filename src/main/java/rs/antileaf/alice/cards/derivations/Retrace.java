package rs.antileaf.alice.cards.derivations;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Retrace extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Retrace.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 0;
	private static final int DRAW = 2;
	private static final int UPGRADE_PLUS_DRAW = 1;

	public Retrace() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_DERIVATION_COLOR,
				CardRarity.SPECIAL,
				CardTargetEnum.DOLL
		);
		
		this.magicNumber = this.baseMagicNumber = DRAW;
		this.retain = this.selfRetain = true;
		this.exhaust = true;
		
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