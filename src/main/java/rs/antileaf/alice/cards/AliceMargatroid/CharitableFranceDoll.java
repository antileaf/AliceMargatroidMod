package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.FranceDoll;
import rs.antileaf.alice.doll.dolls.NetherlandsDoll;
import rs.antileaf.alice.doll.targeting.DollOrEmptySlotTargeting;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class CharitableFranceDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = CharitableFranceDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int BLOCK = 12;
	
	public CharitableFranceDoll() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT
		);
		
		this.baseBlock = this.block = BLOCK;
		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll target = DollOrEmptySlotTargeting.getTarget(this);
		int index = DollManager.get().getDolls().indexOf(target);
		
		AbstractDoll doll = new FranceDoll();
		this.addToBot(new SpawnDollAction(doll, index));
		
		if (!this.upgraded)
			this.addToBot(new DollGainBlockAction(doll, this.block));
		else
			this.addToBot(new AnonymousAction(() -> {
				for (AbstractDoll d : DollManager.get().getDolls())
					if (d instanceof FranceDoll)
						AliceSpireKit.addActionToBuffer(new DollGainBlockAction(d, this.block));
				
				AliceSpireKit.commitBuffer();
			}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CharitableFranceDoll();
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
