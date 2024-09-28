package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollCremation extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollCremation.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int DAMAGE = 22;
	private static final int UPGRADE_PLUS_DMG = 8;
	
	public DollCremation() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.isEthereal = true;
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
		this.targetIcons.add(AliceTargetIcon.ENEMY);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (mon != null) {
			AbstractDoll rightmost = DollManager.get().getDolls().stream()
					.filter(d -> !(d instanceof EmptyDollSlot))
					.findFirst()
					.orElse(null);
			
			if (rightmost != null)
				return new AliceHoveredTargets() {{
					this.dolls = new AbstractDoll[] {rightmost};
				}};
			else
				return AliceHoveredTargets.NONE;
		}
		else if (slot != null && !(slot instanceof EmptyDollSlot))
			return AliceHoveredTargets.allMonsters();
		
		return AliceHoveredTargets.NONE;
	}
	
	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		if (!super.canUse(p, m))
			return false;
		
		if (!DollManager.get().hasDoll()) {
			this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
			return false;
		}
		
		return true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = this.getTargetedDoll();
		if (doll == null)
			doll = DollManager.get().getDolls().stream()
					.filter(d -> !(d instanceof EmptyDollSlot))
					.findFirst()
					.orElse(null);
		
		if (doll != null) {
			this.addToBot(new RecycleDollAction(doll));
			
			AbstractMonster mon = this.getTargetedEnemy();
			if (mon == null)
				this.addToBot(new DamageRandomEnemyAction(new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.FIRE));
			else
				this.addToBot(new DamageAction(mon, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.FIRE));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollCremation();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.initializeDescription();
		}
	}
}
