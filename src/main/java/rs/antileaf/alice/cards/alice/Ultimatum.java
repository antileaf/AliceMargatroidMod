package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.HashSet;

public class Ultimatum extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Ultimatum.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 8;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public Ultimatum() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (mon != null)
			return new AliceHoveredTargets() {{
				this.monsters = AbstractDungeon.getMonsters().monsters.stream()
						.filter(m -> !m.isDeadOrEscaped())
						.filter(m -> m != mon)
						.filter(m -> m.name.equals(mon.name))
						.toArray(AbstractMonster[]::new);
			}};
		
		return AliceHoveredTargets.NONE;
	}
	
	@Override
	public void triggerOnGlowCheck() {
		boolean golden = false;
		HashSet<String> names = new HashSet<>();
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (!m.isDeadOrEscaped()) {
				if (names.contains(m.name)) {
					golden = true;
					break;
				}
				names.add(m.name);
			}
		
		if (golden)
			this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
	}
	
	private void dealOn(AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		this.addToBot(new ApplyPowerAction(
				m,
				AbstractDungeon.player,
				new WeakPower(m, this.magicNumber, false),
				this.magicNumber
		));
		this.addToBot(new ApplyPowerAction(
				m,
				AbstractDungeon.player,
				new VulnerablePower(m, this.magicNumber, false),
				this.magicNumber
		));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.dealOn(m);
		
		for (AbstractMonster other : AbstractDungeon.getMonsters().monsters)
			if (!other.isDeadOrEscaped() && other != m && other.name.equals(m.name))
				this.dealOn(other);
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Ultimatum();
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
