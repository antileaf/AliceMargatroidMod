package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class ReturningDolls extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ReturningDolls.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DRAW = 1;
	private static final int UPGRADE_PLUS_DRAW = 1;
	
	public ReturningDolls() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT
		);
		
		this.magicNumber = this.baseMagicNumber = DRAW;
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
			
			return AliceHoveredTargets.fromDolls(targets.stream()
					.filter(doll -> !(doll instanceof EmptyDollSlot))
					.toArray(AbstractDoll[]::new));
		}
		
		return AliceHoveredTargets.NONE; // 正常情况下没有目标就不会调用
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = getTargetedSlot();
		
		if (slot != null) {
			int draw = 0;
			if (!(slot instanceof EmptyDollSlot)) {
				draw += this.magicNumber;
				this.addToBot(new RecycleDollAction(slot));
			}
			
			int index = DollManager.get().getDolls().indexOf(slot);
			
			if (index > 0) {
				AbstractDoll left = DollManager.get().getDolls().get(index - 1);
				if (!(left instanceof EmptyDollSlot)) {
					draw += this.magicNumber;
					this.addToBot(new RecycleDollAction(left));
				}
			}
			if (index < DollManager.get().getDolls().size() - 1) {
				AbstractDoll right = DollManager.get().getDolls().get(index + 1);
				if (!(right instanceof EmptyDollSlot)) {
					draw += this.magicNumber;
					this.addToBot(new RecycleDollAction(right));
				}
			}
			
			if (draw > 0)
				this.addToBot(new DrawCardAction(draw));
		}
		else
			AliceSpireKit.logger.info("ReturningDolls.use(): slot is null!");
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ReturningDolls();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_DRAW);
			this.initializeDescription();
		}
	}
}
