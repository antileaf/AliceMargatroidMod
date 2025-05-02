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
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.doll.dolls.deprecated.DEPRECATEDLondonDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.AliceTargetIcon;

@Deprecated
public class DEPRECATEDMistyLondonDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDMistyLondonDoll.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 3;
	
	public DEPRECATEDMistyLondonDoll() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.targetIcons.add(AliceTargetIcon.SLOT);
		this.targetIcons.add(AliceTargetIcon.NONE);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getDolls().indexOf(slot);
		
		if (this.upgraded && slot != null && !(slot instanceof EmptyDollSlot)) {
			for (int i = 0; i < this.magicNumber; i++)
				this.addToBot(new DollActAction(slot));
		}
		else {
			AbstractDoll doll = new DEPRECATEDLondonDoll();
			this.addToBot(new SpawnDollAction(doll, index));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDMistyLondonDoll();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();

			this.targetIcons.add(0, AliceTargetIcon.DOLL);
		}
	}
}
