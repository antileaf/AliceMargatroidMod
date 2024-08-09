package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.NetherlandsDoll;
import rs.antileaf.alice.doll.targeting.DollOrEmptySlotTargeting;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;

public class RedHairedNetherlandsDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = RedHairedNetherlandsDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int BLOCK = 4;
//	private static final int MAGIC = 2;
	
	public RedHairedNetherlandsDoll() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT
		);
		
		this.block = this.baseBlock = BLOCK;
//		this.magicNumber = this.baseMagicNumber = MAGIC;
		
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll target = DollOrEmptySlotTargeting.getTarget(this);
		int index = DollManager.get().getDolls().indexOf(target);
		
		AbstractDoll doll = new NetherlandsDoll();
		this.addToBot(new SpawnDollAction(doll, index));
		
		if (this.upgraded) {
			this.addToBot(new DollActAction(doll));
			
			this.addToBot(new AnonymousAction(() -> {
				this.applyPowers();
				this.addToTop(new GainBlockAction(p, p, this.block));
			}));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new RedHairedNetherlandsDoll();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
