package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.DollLoseBlockAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.handlers.DollTargeting;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Relay extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Relay.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int MAGIC = 1;
	
	public Relay() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);
		
		if (doll != null) {
			int total = 0;
			for (AbstractDoll other : DollManager.get().getDolls())
				if (other != doll) {
					total += other.block;
					this.addToBot(new DollLoseBlockAction(other, other.block));
				}
			
			this.addToBot(new DollGainBlockAction(doll, total));
		}
		
		this.addToBot(new DrawCardAction(this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Relay();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
