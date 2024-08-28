package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollLances extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollLances.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 7;
	private static final int MAGIC = 0;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public DollLances() {
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
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		AliceHoveredTargets o = new AliceHoveredTargets();
		o.dolls = DollManager.get().getDolls().stream()
				.filter(doll -> doll != slot)
				.filter(doll -> doll instanceof ShanghaiDoll)
				.toArray(AbstractDoll[]::new);
		
		if (!this.upgraded)
			return o;
		
		if (mon == null)
			o.monsters = AbstractDungeon.getMonsters().monsters.toArray(new AbstractMonster[0]);
		
		return o;
	}
	
	public AbstractGameAction getAction() {
		return new AnonymousAction(() -> {
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (doll instanceof ShanghaiDoll)
					AliceSpireKit.addActionToBuffer(new DollActAction(doll));
			AliceSpireKit.commitBuffer();
		});
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!this.upgraded) {
			this.addToBot(new DamageAction(
					m,
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}
		else {
			AbstractMonster mon = this.getTargetedEnemy();
			if (mon != null)
				this.addToBot(new DamageAction(
						mon,
						new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
			else
				this.addToBot(new DamageRandomEnemyAction(
						new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
			
			AbstractDoll slot = this.getTargetedSlot();
			int index = DollManager.get().getDolls().indexOf(slot);
			
			for (int i = 0; i < this.magicNumber; i++)
				this.addToBot(new SpawnDollAction(new ShanghaiDoll(), index));
		}
		
		this.addToBot(this.getAction());
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollLances();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.target = CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE;
			this.initializeDescription();
		}
	}
}
