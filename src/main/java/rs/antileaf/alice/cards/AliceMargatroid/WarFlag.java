package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.targeting.DollTargeting;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class WarFlag extends AbstractAliceCard {
	public static final String SIMPLE_NAME = WarFlag.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	
	public WarFlag() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL
		);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);
		
		if (doll == null)
			return;
		
		if (this.upgraded)
			this.addToBot(new DollActAction(doll));
		
		this.addToBot(new AnonymousAction(() -> {
			if (DollManager.get().contains(doll)) {
				int block = doll.block / 2;
				
				if (block > 0) {
					for (AbstractDoll other : DollManager.get().getDolls()) {
						if (other != doll)
							AliceSpireKit.addActionToBuffer(new DollGainBlockAction(other, block));
					}
//					int index = DollManager.get().getDolls().indexOf(doll);
//					if (index > 0)
//						AliceSpireKit.addActionToBuffer(new DollGainBlockAction(
//								DollManager.get().getDolls().get(index - 1), block));
//					if (index < DollManager.MAX_DOLL_SLOTS - 1)
//						AliceSpireKit.addActionToBuffer(new DollGainBlockAction(
//								DollManager.get().getDolls().get(index + 1), block));
					
					AliceSpireKit.commitBuffer();
				}
			}
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new WarFlag();
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
