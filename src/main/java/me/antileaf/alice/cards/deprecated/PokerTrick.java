package me.antileaf.alice.cards.deprecated;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.effects.common.AlicePokerCardsDroppingEffect;
import me.antileaf.alice.effects.utils.AlicePokerHelper;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class PokerTrick extends AbstractAliceCard {
	public static final String SIMPLE_NAME = PokerTrick.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	public static final String[] Suffix = {
			"",
			"_Spade",
			"_Heart",
			"_Diamond",
			"_Club"
	};
	
	private static final int COST = 1;
	private static final int DAMAGE = 8;
	private static final int UPGRADE_PLUS_DMG = 2;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	private static final int MAGIC2 = 15;
	private static final int UPGRADE_PLUS_MAGIC2 = 5;
	
	public int suit = 0;
	
	public PokerTrick(int suit, boolean dontAddPreview) {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
		this.exhaust = true;
		
		if (suit != 0)
			this.changeSuit(suit);
		
		if (suit == 0 && !dontAddPreview) {
			for (int i = 1; i <= 4; i++)
//				if (i != suit)
					MultiCardPreview.add(this, new PokerTrick(i, true));
		}
	}
	
	public PokerTrick() {
		this(0, false);
	}
	
	private void changeSuit(int suit) {
		this.suit = suit;
		this.name = cardStrings.NAME;
		
		if (this.suit == 0) {
			this.rawDescription = cardStrings.DESCRIPTION;
			
			FlavorText.AbstractCardFlavorFields.flavor.set(this,
					FlavorText.CardStringsFlavorField.flavor.get(cardStrings));
		}
		else {
			this.name += "(" + cardStrings.EXTENDED_DESCRIPTION[(this.suit - 1) * 2] + ")";
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[(this.suit - 1) * 2 + 1];
			
			FlavorText.AbstractCardFlavorFields.flavor.set(this,
					cardStrings.EXTENDED_DESCRIPTION[this.suit + 7]);
		}
		
		this.initializeDescription();
		
//		this.textureImg = AliceSpireKit.getCardImgFilePath("Attack");
//		this.loadCardImage(this.textureImg);
		
		MultiCardPreview.clear(this);
	}
	
	@Override
	public void triggerWhenDrawn() {
		this.changeSuit(AbstractDungeon.cardRng.random(3) + 1);
		this.addToBot(new ShowCardAction(this));
		this.flash();
	}
	
	private AbstractGameAction getAction(AbstractMonster m) {
		return new DamageAction(
				m,
				new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT
		);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.suit == 0) {
			this.addToBot(this.getAction(m));
		}
		else if (this.suit == 1) { // Spade
			this.addToBot(this.getAction(m));
			this.addToBot(new ApplyPowerAction(
					m, p,
					new WeakPower(m, this.magicNumber, false),
					this.magicNumber));
			this.addToBot(new ApplyPowerAction(
					m, p,
					new VulnerablePower(m, this.magicNumber, false),
					this.magicNumber
			));
		}
		else if (this.suit == 2) { // Heart
			this.addToBot(this.getAction(m));
			this.addToBot(this.getAction(m));
		}
		else if (this.suit == 3) { // Diamond
			int gold = this.secondaryMagicNumber;
			this.addToBot(new AnonymousAction(() -> {
				AbstractDungeon.player.gainGold(gold);
				for (int i = 0; i < gold; i++)
					AliceHelper.addEffect(new GainPennyEffect(
								p,
								m.hb.cX,
								m.hb.cY,
								p.hb.cX,
								p.hb.cY,
							true
					));
			}));
			this.addToBot(this.getAction(m));
		}
		else if (this.suit == 4) { // Club
			this.addToBot(new DamageCallbackAction(
					m,
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
					(amount) -> {
						if (amount > 0)
							this.addToTop(new AddTemporaryHPAction(p, p, amount));
					}
			));
		}
		
		AlicePokerHelper.Suit poker = AlicePokerHelper.Suit.JOKER_SMALL;
		if (this.suit != 0)
			poker = AlicePokerHelper.Suit.values()[this.suit - 1];
		else if (MathUtils.random(1, 50) == 1)
			poker = AlicePokerHelper.Suit.JOKER_BIG;
		
		for (int i = (this.suit == 2 ? 0 : 1); i < 2; i++) {
//			AbstractDungeon.effectList.add(new PokerTrickEffect(
//					poker,
//					p.drawX,
//					p.drawY,
//					m.drawX,
//					m.drawY,
//					0.6F
//			));
			
			AlicePokerCardsDroppingEffect effect = new AlicePokerCardsDroppingEffect(0.6F,
					2F, false, 1200.0F);
			
			for (int j = 0; j < 8; j++)
				effect.add(poker,
						m.drawX, m.drawY,
						MathUtils.random(-300.0F, 300.0F),
						MathUtils.random(100.0F, 600.0F),
						MathUtils.random(0.0F, 360.0F),
						MathUtils.random(-360.0F, 360.0F));
			
			AbstractDungeon.effectList.add(effect);
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new PokerTrick();
	}
	
	@Override
	public AbstractCard makeStatEquivalentCopy() {
		PokerTrick copy = (PokerTrick) super.makeStatEquivalentCopy();
		copy.suit = this.suit;
		copy.initializeDescription();
		return copy;
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			for (AbstractCard preview : MultiCardPreview.multiCardPreview.get(this))
				preview.upgrade();
			this.initializeDescription();
		}
	}
}
