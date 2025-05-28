package me.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.targeting.handlers.DollOrNoneTargeting;
import me.antileaf.alice.utils.AliceHelper;

@Deprecated
public class ProtectiveSpell extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ProtectiveSpell.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int BLOCK = 10;
	private static final int UPGRADE_PLUS_BLOCK = 4;
	
	public ProtectiveSpell() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_NONE
		);
		
		this.block = this.baseBlock = BLOCK;

		this.dollTarget = true;

		this.targetIcons.add(AliceTargetIcon.ALICE);
		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (slot == null || slot instanceof EmptyDollSlot)
			return AliceHoveredTargets.PLAYER;
		
		return AliceHoveredTargets.NONE;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		final AbstractDoll target = DollOrNoneTargeting.getTarget(this);
		
		if (target == null)
			this.addToBot(new AnonymousAction(() -> {
				if (p.currentBlock < this.block)
					this.addToTop(new GainBlockAction(p, p, this.block - p.currentBlock));
			}));
		else
			this.addToBot(new AnonymousAction(() -> {
				AliceHelper.log(SIMPLE_NAME, "ProtectiveMagic: current block = " + target.block);
				
				if (!DollManager.get().contains(target))
					AliceHelper.log(this.getClass(), "ProtectiveMagic: target not in DollManager");
				else if (target.block < this.block)
					this.addToTop(new DollGainBlockAction(target, this.block - target.block));
			}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ProtectiveSpell();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.initializeDescription();
		}
	}
}
