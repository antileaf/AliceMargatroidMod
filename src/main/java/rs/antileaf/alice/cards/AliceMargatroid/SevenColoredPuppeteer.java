package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class SevenColoredPuppeteer extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SevenColoredPuppeteer.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 7;
	private static final int DAMAGE = 7;
	private static final int MAGIC = 7;
	
	private boolean gold = false;
	
	private int costBackup;
	private int costForTurnBackup;
	
	public SevenColoredPuppeteer() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.costBackup = this.costForTurnBackup = this.cost;
	}
	
	private void wrap(Runnable func) {
		int tmpCost = this.cost, tmpCostForTurn = this.costForTurn;
		this.cost = this.costBackup;
		this.costForTurn = this.costForTurnBackup;
		
		func.run();
		
		this.costBackup = this.cost;
		this.costForTurnBackup = this.costForTurn;
		
		this.cost = tmpCost;
		this.costForTurn = tmpCostForTurn;
		
		func.run();
	}
	
	@Override
	public void updateCost(int amt) {
		this.wrap(() -> super.updateCost(amt));
	}
	
	@Override
	public void setCostForTurn(int amt) {
		this.wrap(() -> super.setCostForTurn(amt));
	}
	
	@Override
	public void modifyCostForCombat(int amt) {
		this.wrap(() -> super.modifyCostForCombat(amt));
	}
	
	@Override
	public void resetAttributes() {
		this.costForTurnBackup = this.costBackup;
	}
	
	@Override
	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
		
		if (this.gold)
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		
		if (AliceSpireKit.isInBattle()) {
			boolean ok = true;
			
			for (AbstractDoll doll : DollManager.get().getDolls()) {
				if (doll instanceof EmptyDollSlot) {
					ok = false;
					break;
				}
				
				else if (!AbstractDoll.isBaseDoll(doll)) {
					ok = false;
					break;
				}
				
				for (AbstractDoll doll2 : DollManager.get().getDolls()) {
					if (doll2 == doll)
						break;
					
					if (doll2.ID.equals(doll.ID)) {
						ok = false;
						break;
					}
				}
			}
			
			if (ok) {
				this.costForTurn = this.cost = 0;
				this.isCostModifiedForTurn = true;
				this.gold = true;
			}
			else {
				this.cost = this.costBackup;
				this.costForTurn = this.costForTurnBackup;
				this.isCostModifiedForTurn = (this.costForTurn != this.cost);
				this.gold = false;
			}
			
			this.triggerOnGlowCheck();
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.calculateCardDamage(null);
		
		for (int i = 0; i < this.magicNumber; i++)
			this.addToBot(new DamageAllEnemiesAction(
					p,
					this.multiDamage,
					this.damageTypeForTurn,
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
					true
			));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SevenColoredPuppeteer();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.retain = this.selfRetain = true;
//			AlwaysRetainField.alwaysRetain.set(this, true);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
