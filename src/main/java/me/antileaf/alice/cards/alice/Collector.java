package me.antileaf.alice.cards.alice;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.strings.AliceCardNoteStrings;
import me.antileaf.alice.strings.AliceLanguageStrings;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.utils.AliceHelper;

public class Collector extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Collector.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final AliceCardNoteStrings cardNoteStrings = AliceCardNoteStrings.get(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 4;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public Collector() {
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
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		
//		if (AliceHelper.isInBattle())
//			this.applyPowers();

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
	public void applyPowers() {
		this.baseBlock = DollManager.get().getStats().placedThisCombat.size() * this.magicNumber;
		super.applyPowers();

		this.rawDescription = cardStrings.DESCRIPTION + " NL " + cardStrings.EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	@Override
	public void onMoveToDiscard() {
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}
	
	@Override
	public TooltipInfo getNote() {
		if (!AliceHelper.isInBattle())
			return null;
//			return new TooltipInfo(cardNoteStrings.TITLE, cardNoteStrings.DESCRIPTION);
		
		if (cardNoteStrings == null) {
//			logger.warn("getNote(): No note found for ID: {}!", ID);
			return null;
		}
		
		DollManager.Stats stats = DollManager.get().getStats();
		
		StringBuilder sb = new StringBuilder(
				String.format(cardNoteStrings.EXTENDED_DESCRIPTION[0], stats.placedThisCombat.size()));
		if (stats.placedThisCombat.isEmpty())
			sb.append(AliceLanguageStrings.PERIOD);
		else {
			sb.append(cardNoteStrings.EXTENDED_DESCRIPTION[1]);
			
			for (String id : stats.placedThisCombat) {
				sb.append(" NL ");
				sb.append(AbstractDoll.getName(id));
			}
		}

//		AliceSpireKit.logger.info("SevenColoredPuppeteer.getNote(): {}", sb);
		
		return new TooltipInfo(cardNoteStrings.TITLE, sb.toString());
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = this.getTargetedDoll();
		
		if (doll != null)
			this.addToBot(new DollGainBlockAction(doll, this.block));
		else
			this.addToBot(new GainBlockAction(p, p, this.block));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Collector();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
//			if (AliceHelper.isInBattle())
//				this.applyPowers();
			this.initializeDescription();
		}
	}
}
