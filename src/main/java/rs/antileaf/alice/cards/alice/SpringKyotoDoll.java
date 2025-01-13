package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.KyotoDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceHelper;

public class SpringKyotoDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SpringKyotoDoll.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
	
	public SpringKyotoDoll() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.SLOT);
//		this.targetIcons.add(AliceTargetIcon.NONE);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getDolls().indexOf(slot);
		
		AbstractDoll doll = new KyotoDoll();
		this.addToBot(new SpawnDollAction(doll, index));
		
		if (this.upgraded) {
//			this.addToBot(new AnonymousAction(() -> {
//				doll.maxHP += this.secondaryMagicNumber; // * doll.getDarkGrimoireBaseHP();
//				doll.HP += this.secondaryMagicNumber; // * doll.getDarkGrimoireBaseHP();
//				doll.healthBarUpdatedEvent();
//			}));

			this.addToBot(new GainEnergyAction(this.magicNumber));
		}
		
//		this.addToBot(new ApplyPowerAction(p, p, new PerfectCherryBlossomPower(this.magicNumber), this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SpringKyotoDoll();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
//			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
