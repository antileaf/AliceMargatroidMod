package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.targeting.handlers.DollTargeting;
import rs.antileaf.alice.utils.AliceHelper;

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
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);
		
		if (doll != null && !(doll instanceof EmptyDollSlot)) {
			String id = doll.getID();
			int count = this.magicNumber;
			
			for (int i = 0; i < DollManager.get().getDolls().size(); i++)
				if (DollManager.get().getDolls().get(i) != doll
						&& DollManager.get().getDolls().get(i) instanceof EmptyDollSlot) {
					int pos = i;
					this.addToBot(new AnonymousAction(() -> {
						if (DollManager.get().getDolls().get(pos) instanceof EmptyDollSlot)
							this.addToTop(new SpawnDollAction(AbstractDoll.newInst(id), pos));
					}));
					
					if (--count <= 0)
						break;
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
