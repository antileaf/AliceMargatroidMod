package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class Housework extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Housework.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	
	public Housework() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT
		);
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.SLOT);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (slot != null) {
			int index = DollManager.get().getDolls().indexOf(slot);
			
			ArrayList<AbstractDoll> targets = new ArrayList<>();
			if (index > 0)
				targets.add(DollManager.get().getDolls().get(index - 1));
			if (index < DollManager.get().getDolls().size() - 1)
				targets.add(DollManager.get().getDolls().get(index + 1));
			
			return AliceHoveredTargets.fromDolls(targets.toArray(new AbstractDoll[0]));
		}
		
		return AliceHoveredTargets.NONE;
	} //  (del AliceMargatroid.jar) -xor (jar -cvf AliceMargatroid.jar .) -xor (cp AliceMargatroid.jar D:\steam\steamapps\common\SlayTheSpire\mods\)
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = getTargetedSlot();
		
		if (slot != null) {
			if (!(slot instanceof EmptyDollSlot))
				this.addToBot(new DollActAction(slot));
			
			int index = DollManager.get().getDolls().indexOf(slot);
			if (index > 0)
				this.addToBot(new DollActAction(DollManager.get().getDolls().get(index - 1)));
			if (index < DollManager.get().getDolls().size() - 1)
				this.addToBot(new DollActAction(DollManager.get().getDolls().get(index + 1)));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Housework();
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
