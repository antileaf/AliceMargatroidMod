package rs.antileaf.alice.cards.colorless;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SetDontTriggerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.utils.AliceSpireKit;

public class PoisonousSweet extends AbstractAliceCard {
	public static final String SIMPLE_NAME = PoisonousSweet.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = -2;
	private static final int MAGIC = 2;
	
	public PoisonousSweet() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.CURSE,
				CardColor.CURSE,
				CardRarity.SPECIAL,
				CardTarget.SELF
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.dontTriggerOnUseCard)
			this.addToTop(new ApplyPowerAction(p, p, new PoisonPower(p, p, this.magicNumber), this.magicNumber));
	}
	
	@Override
	public void triggerWhenDrawn() {
		this.addToBot(new SetDontTriggerAction(this, false));
	}
	
	@Override
	public void triggerOnEndOfTurnForPlayingCard() {
		this.dontTriggerOnUseCard = true;
		AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new PoisonousSweet();
	}
	
	@Override
	public void upgrade() {
		// Curses can't be upgraded.
	}
}
