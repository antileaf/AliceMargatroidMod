package me.antileaf.alice.cards.deprecated;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class DEPRECATEDSevenColoredPuppeteer extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDSevenColoredPuppeteer.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 7;
	private static final int DAMAGE = 7;
	private static final int MAGIC = 7;
	
//	private int costBackup;
//	private int costForTurnBackup;
	
	public DEPRECATEDSevenColoredPuppeteer() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
//		this.costBackup = this.costForTurnBackup = this.cost;
	}
	
//	private void wrap(Runnable func) {
//		int tmpCost = this.cost, tmpCostForTurn = this.costForTurn;
//		this.cost = this.costBackup;
//		this.costForTurn = this.costForTurnBackup;
//
//		func.run();
//
//		this.costBackup = this.cost;
//		this.costForTurnBackup = this.costForTurn;
//
//		this.cost = tmpCost;
//		this.costForTurn = tmpCostForTurn;
//
//		func.run();
//	}
//
//	@Override
//	public void updateCost(int amt) {
//		this.wrap(() -> super.updateCost(amt));
//	}
//
//	@Override
//	public void setCostForTurn(int amt) {
//		this.wrap(() -> super.setCostForTurn(amt));
//	}
//
//	@Override
//	public void modifyCostForCombat(int amt) {
//		this.wrap(() -> super.modifyCostForCombat(amt));
//	}
//
//	@Override
//	public void resetAttributes() {
//		super.resetAttributes();
//		this.costForTurnBackup = this.costBackup;
//	}
	
	@Override
	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
		
		if (AliceHelper.isInBattle()) {
			if (!AliceHelper.hasDuplicateCards(AbstractDungeon.player.hand.group, null))
				this.glowColor = GOLD_BORDER_GLOW_COLOR;
			else
				this.glowColor = BLUE_BORDER_GLOW_COLOR;
		}
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		
		if (AliceHelper.isInBattle()) {
			if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
				this.costForTurn = 0;
//				this.freeToPlayOnce = true;
				this.isCostModifiedForTurn = true;
			}
			else {
//				this.cost = this.costBackup;
				this.costForTurn = this.cost;
//				this.freeToPlayOnce = false;
				this.isCostModifiedForTurn = false;
			}
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.calculateCardDamage(null);
		
		if (!AliceHelper.hasDuplicateCards(AbstractDungeon.player.hand.group, null)) {
			for (int i = 0; i < this.magicNumber; i++)
				this.addToBot(new DamageAllEnemiesAction(
						p,
						this.multiDamage,
						this.damageTypeForTurn,
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
						true
				));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDSevenColoredPuppeteer();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.retain = this.selfRetain = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
