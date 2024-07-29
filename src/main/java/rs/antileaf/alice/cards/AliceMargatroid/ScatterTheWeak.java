package rs.antileaf.alice.cards.AliceMargatroid;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.function.Consumer;

public class ScatterTheWeak extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ScatterTheWeak.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 5;
	private static final int UPGRADE_PLUS_DAMAGE = 3;
	
	public ScatterTheWeak() {
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
		this.exhaust = true;
		this.isMultiDamage = true;
	}
	
	public static Consumer<Integer> getDamageCallback(AbstractMonster monster) {
		return (amount) -> {
			if (amount > 0 &&
					!monster.isDeadOrEscaped() &&
					!monster.halfDead &&
					monster.hasPower(MinionPower.POWER_ID))
				
				AliceSpireKit.addToTop(new InstantKillAction(monster));
		};
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.calculateCardDamage(null);
		
		for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
			if (!monster.isDeadOrEscaped() && !monster.halfDead) {
				int dmg = this.multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(monster)];
				
				this.addToBot(new DamageCallbackAction(
						monster,
						new DamageInfo(p, dmg, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.FIRE,
						getDamageCallback(monster)
				));
			}
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ScatterTheWeak();
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
