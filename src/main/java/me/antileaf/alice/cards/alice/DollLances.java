package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.ShanghaiDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class DollLances extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollLances.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 6;
	private static final int UPGRADE_PLUS_DAMAGE = 2;
//	private static final int MAGIC = 0;
//	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public DollLances() {
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
//		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
//	@Override
//	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
//		AliceHoveredTargets o = new AliceHoveredTargets();
//		o.dolls = DollManager.get().getDolls().stream()
//				.filter(doll -> doll != slot)
//				.filter(doll -> doll instanceof ShanghaiDoll)
//				.toArray(AbstractDoll[]::new);
//
//		if (!this.upgraded)
//			return o;
//
//		if (mon == null)
//			o.monsters = AbstractDungeon.getMonsters().monsters.toArray(new AbstractMonster[0]);
//
//		return o;
//	}
	
//	public AbstractGameAction getAction() {
//		return new AnonymousAction(() -> {
//			for (AbstractDoll doll : DollManager.get().getDolls())
//				if (doll instanceof ShanghaiDoll)
//					AliceSpireKit.addActionToBuffer(new DollActAction(doll));
//			AliceSpireKit.commitBuffer();
//		});
//	}

	@Override
	public void applyPowers() {
		this.baseMagicNumber = this.magicNumber = 1 + (int) DollManager.get().getDolls().stream()
				.filter(doll -> doll instanceof ShanghaiDoll)
				.count();

		super.applyPowers();

		this.rawDescription = cardStrings.DESCRIPTION + " NL " + cardStrings.EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	@Override
	public void onMoveToDiscard() {
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
//		if (!this.upgraded) {
//			this.addToBot(new DamageAction(
//					m,
//					new DamageInfo(p, this.damage, this.damageTypeForTurn),
//					AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//		}
//		else {
//			AbstractMonster mon = this.getTargetedEnemy();
//			if (mon != null)
//				this.addToBot(new DamageAction(
//						mon,
//						new DamageInfo(p, this.damage, this.damageTypeForTurn),
//						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//			else
//				this.addToBot(new DamageRandomEnemyAction(
//						new DamageInfo(p, this.damage, this.damageTypeForTurn),
//						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//
//			AbstractDoll slot = this.getTargetedSlot();
//			int index = DollManager.get().getDolls().indexOf(slot);
//
//			for (int i = 0; i < this.magicNumber; i++)
//				this.addToBot(new SpawnDollAction(new ShanghaiDoll(), index));
//		}
//
//		this.addToBot(this.getAction());

		int count = 1 + (int) DollManager.get().getDolls().stream()
				.filter(doll -> doll instanceof ShanghaiDoll)
				.count();

		for (int i = 0; i < count; i++)
			this.addToBot(new DamageAction(
					m,
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollLances();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
//			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
//			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
//			this.target = CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE;
			this.initializeDescription();
		}
	}
}
