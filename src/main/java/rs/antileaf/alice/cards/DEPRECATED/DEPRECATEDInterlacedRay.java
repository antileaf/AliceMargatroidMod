package rs.antileaf.alice.cards.DEPRECATED;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;

@Deprecated
public class DEPRECATEDInterlacedRay extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDInterlacedRay.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 7;
	private static final int UPGRADE_PLUS_DAMAGE = 3;
	
	public DEPRECATEDInterlacedRay() {
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
		this.tags.add(CardTagEnum.ALICE_RAY);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(true);
		if (monster != null) {
			this.addToBot(new DamageAction(
					monster,
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL
			));
			
			if (AbstractDungeon.getMonsters().monsters.stream()
					.filter(mon -> !mon.isDeadOrEscaped()).count() == 1)
				return;
			
			AbstractMonster another = AbstractDungeon.getMonsters().getRandomMonster(monster, true);
			if (another != null)
				this.addToBot(new DamageAction(
						another,
						new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL
				));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDInterlacedRay();
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
