package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.SpawnDollAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.doll.dolls.NetherlandsDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.powers.unique.FuturisticBunrakuPower;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.targeting.handlers.DollTargeting;
import me.antileaf.alice.utils.AliceHelper;

public class DollArmy extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollArmy.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;
	private static final int MAGIC = 3;
	
	public DollArmy() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;

		this.isCommandCard = true;
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);

		boolean futuristic = !(doll instanceof NetherlandsDoll) &&
				p.powers.stream()
						.anyMatch(power -> power instanceof FuturisticBunrakuPower &&
								((FuturisticBunrakuPower) power).doll == doll);
		
		if (doll != null && !(doll instanceof EmptyDollSlot)) {
//			int count = this.magicNumber;
//
//			for (int i = 0; i < DollManager.get().getDolls().size(); i++)
//				if (DollManager.get().getDolls().get(i) != doll
//						&& DollManager.get().getDolls().get(i) instanceof EmptyDollSlot) {
//					int pos = i;
//					this.addToBot(new AnonymousAction(() -> {
//						if (DollManager.get().getDolls().get(pos) instanceof EmptyDollSlot) {
//							AbstractDoll copy = doll.makeStatEquivalentCopy();
//
//							this.addToTop(new SpawnDollAction(copy, pos));
//
//							if (futuristic)
//								this.addToBot(new ApplyPowerAction(p, p,
//										new FuturisticBunrakuPower(copy)));
//						}
//					}));
//
//					if (--count <= 0)
//						break;
//				}

			for (int i = 0; i < this.magicNumber; i++) {
				AbstractDoll copy = doll.makeStatEquivalentCopy();
				this.addToBot(new SpawnDollAction(copy, -1, futuristic ?
						new AnonymousAction(() -> {
							if (DollManager.get().getDolls().contains(copy))
								this.addToTop(new ApplyPowerAction(p, p,
										new FuturisticBunrakuPower(copy)));
						}) : null));
			}
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollArmy();
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
