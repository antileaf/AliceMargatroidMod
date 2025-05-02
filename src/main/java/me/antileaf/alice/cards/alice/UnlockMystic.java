package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.powers.unique.UnlockMysticPower;
import me.antileaf.alice.utils.AliceHelper;

public class UnlockMystic extends AbstractAliceCard {
	public static final String SIMPLE_NAME = UnlockMystic.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;

	public UnlockMystic() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(p, p, new UnlockMysticPower()));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new UnlockMystic();
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
