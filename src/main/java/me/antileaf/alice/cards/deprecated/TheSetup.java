package me.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.utils.AliceHelper;

@Deprecated
public class TheSetup extends AbstractAliceCard {
	public static final String SIMPLE_NAME = TheSetup.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	
	public TheSetup() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		return AliceHoveredTargets.fromDolls(DollManager.get().getDolls().stream()
				.filter(doll -> doll != slot)
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.filter(doll -> {
					if (!this.upgraded)
						return doll.calcTotalDamageAboutToTake() != -1;
					else
						return DollManager.get().damageTarget.values()
								.stream()
								.anyMatch(i -> DollManager.get().getDolls().get(i) == doll);
				})
				.toArray(AbstractDoll[]::new));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot)) {
				boolean flag = false;

				if (!this.upgraded)
					flag = doll.calcTotalDamageAboutToTake() != -1;
				else
					flag = DollManager.get().damageTarget.values()
							.stream()
							.anyMatch(i -> DollManager.get().getDolls().get(i) == doll);

				if (flag)
					this.addToBot(new DollActAction(doll, AbstractDoll.DollActModifier.setup()));
			}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new TheSetup();
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
