package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.powers.unique.FuturisticBunrakuPower;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.utils.AliceHelper;

public class FuturisticBunraku extends AbstractAliceCard {
	public static final String SIMPLE_NAME = FuturisticBunraku.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;

	public FuturisticBunraku() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTargetEnum.DOLL
		);

		this.exhaust = true;

		this.isCommandCard = true;
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = this.getTargetedDoll();

		if (doll != null)
			this.addToBot(new ApplyPowerAction(p, p, new FuturisticBunrakuPower(doll)));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new FuturisticBunraku();
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
