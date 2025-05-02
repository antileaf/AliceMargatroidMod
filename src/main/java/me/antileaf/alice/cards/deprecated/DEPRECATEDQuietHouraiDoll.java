package me.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.doll.SpawnDollAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.deprecated.DEPRECATEDHouraiDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.utils.AliceHelper;

@Deprecated
public class DEPRECATEDQuietHouraiDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDQuietHouraiDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int MAGIC = 2;
	
	public DEPRECATEDQuietHouraiDoll() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.SLOT);
		this.targetIcons.add(AliceTargetIcon.NONE);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		if (this.upgraded && slot instanceof DEPRECATEDHouraiDoll) {
			for (int i = 0; i < this.magicNumber; i++)
				this.addToBot(new DollActAction(slot));
		}
		
		int index = DollManager.get().getDolls().indexOf(slot);
		
		AbstractDoll doll = new DEPRECATEDHouraiDoll();
		this.addToBot(new SpawnDollAction(doll, index));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDQuietHouraiDoll();
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
