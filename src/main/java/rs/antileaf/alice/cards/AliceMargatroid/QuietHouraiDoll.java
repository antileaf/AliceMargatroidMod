package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.HouraiDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class QuietHouraiDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = QuietHouraiDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 2;
	
	public QuietHouraiDoll() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		if (this.upgraded && slot instanceof HouraiDoll) {
			for (int i = 0; i < this.magicNumber; i++)
				this.addToBot(new DollActAction(slot));
		}
		
		int index = DollManager.get().getDolls().indexOf(slot);
		
		AbstractDoll doll = new HouraiDoll();
		this.addToBot(new SpawnDollAction(doll, index));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new QuietHouraiDoll();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
