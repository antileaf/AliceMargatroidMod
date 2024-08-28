package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.MoveDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.handlers.DollOrNoneTargeting;
import rs.antileaf.alice.utils.AliceSpireKit;

public class LittleLegion extends AbstractAliceCard {
	public static final String SIMPLE_NAME = LittleLegion.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 6;
	private static final int UPGRADE_PLUS_DMG = 3;
	
	public LittleLegion() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTargetEnum.DOLL_OR_NONE
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.isMultiDamage = true;
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		return AliceHoveredTargets.allMonsters();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollOrNoneTargeting.getTarget(this);
		
		this.calculateCardDamage(null);
		this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		
		if (doll != null) {
			this.addToBot(new MoveDollAction(doll, 0));
			this.addToBot(new DollActAction(doll));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new LittleLegion();
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
