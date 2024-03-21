package rs.antileaf.alice.cards.AliceMargatroid;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.powers.unique.FrostRayPower;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class SevenColoredPuppeteer extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SevenColoredPuppeteer.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 7;
	private static final int DAMAGE = 7;
	private static final int MAGIC = 7;
	
	private boolean gold = false;
	
	public SevenColoredPuppeteer() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
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
				this.setCostForTurn(0);
				this.gold = true;
			}
			else {
				this.setCostForTurn(this.cost);
				this.isCostModifiedForTurn = false;
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
