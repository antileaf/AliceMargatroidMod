package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.powers.unique.TauntedPower;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.utils.AliceHelper;

public class SoldierOfCross extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SoldierOfCross.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int BLOCK = 9;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_BLOCK = 3;

	public SoldierOfCross() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_NONE
		);
		
		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.isCommandCard = true;
		this.tags.add(CardTagEnum.ALICE_COMMAND);

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
		AbstractDoll doll = this.getTargetedDoll();
		
		if (doll != null) {
			this.addToBot(new DollGainBlockAction(doll, this.block));

			int amount2 = DollManager.get().getIndex(doll) + 1;

			for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
				if (!mo.isDeadOrEscaped())
					this.addToBot(new ApplyPowerAction(mo, p,
							new TauntedPower(mo, this.magicNumber, amount2)));
		}
		else
			this.addToBot(new GainBlockAction(p, this.block));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SoldierOfCross();
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
