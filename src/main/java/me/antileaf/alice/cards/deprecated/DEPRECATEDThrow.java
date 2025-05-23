package me.antileaf.alice.cards.deprecated;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PersistFields;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.RecycleDollAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.handlers.DollTargeting;
import me.antileaf.alice.utils.AliceHelper;

@Deprecated
public class DEPRECATEDThrow extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDThrow.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 8;
	private static final int UPGRADE_PLUS_DAMAGE = 3;
	private static final int MAGIC = 3;
	
	public DEPRECATEDThrow() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		PersistFields.setBaseValue(this, this.magicNumber);
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
	}
	
	@Override
	public void applyPowers() {
		this.rawDescription = cardStrings.DESCRIPTION;
		if (AliceHelper.isInBattle() && AbstractDungeon.player.hand.contains(this))
			this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
		
		this.initializeDescription();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);
		
		if (doll != null) {
			this.addToBot(new RecycleDollAction(doll));
			this.addToBot(new DamageRandomEnemyAction(
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.BLUNT_LIGHT
			));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDThrow();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
			this.initializeDescription();
		}
	}
}
