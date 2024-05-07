package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;

public class Tripwire extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Tripwire.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int DAMAGE = 4;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public Tripwire() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.isMultiDamage = true;
	}
	
	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
		
		this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (!m.isDeadOrEscaped() &&
					m.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF))
				this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.calculateCardDamage(null);
		
		for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
			if (!mon.isDeadOrEscaped() &&
					mon.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF))
				this.multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(mon)] *= this.magicNumber;
		
		this.addToBot(new DamageAllEnemiesAction(
				p,
				this.multiDamage,
				this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Tripwire();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
