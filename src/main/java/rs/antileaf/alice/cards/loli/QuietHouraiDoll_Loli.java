package rs.antileaf.alice.cards.loli;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractLoliCard;
import rs.antileaf.alice.cards.alice.QuietHouraiDoll;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.HouraiDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceSpireKit;

public class QuietHouraiDoll_Loli extends AbstractLoliCard<QuietHouraiDoll> {
	public static final String SIMPLE_NAME = QuietHouraiDoll_Loli.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int BLOCK = 4;
	private static final int UPGRADE_PLUS_BLOCK = 1;

	public QuietHouraiDoll_Loli() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(QuietHouraiDoll.SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_LOLI_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.block = this.baseBlock = BLOCK;

		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.SLOT);
		this.targetIcons.add(AliceTargetIcon.NONE);
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

		this.addToBot(new AnonymousAction(() -> {
			for (AbstractDoll d : DollManager.get().getDolls())
				if (!(d instanceof EmptyDollSlot))
					AliceSpireKit.addActionToBuffer(new DollGainBlockAction(d, this.block));

			AliceSpireKit.commitBuffer();
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new QuietHouraiDoll_Loli();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}