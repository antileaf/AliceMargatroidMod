package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.targeting.DollTargeting;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Masterpiece extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Masterpiece.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int BLOCK = 3;
	private static final int DRAW = 1;
	private static final int UPGRADE_PLUS_BLOCK = 2;
	
	public Masterpiece() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL
		);
		
		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = DRAW;
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);
		
		if (doll != null) {
			this.addToBot(new RecycleDollAction(doll));
			
			this.addToBot(new GainBlockAction(p, p, this.block));
			this.addToBot(new DrawCardAction(p, this.magicNumber));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Masterpiece();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.initializeDescription();
		}
	}
}
