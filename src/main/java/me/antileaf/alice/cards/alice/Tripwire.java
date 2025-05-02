package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class Tripwire extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Tripwire.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 3;
	private static final int UPGRADE_PLUS_DAMAGE = 1;
	
	public Tripwire() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int count = (int)DollManager.get().getDolls().stream()
				.filter(doll -> (doll instanceof EmptyDollSlot))
				.count();
		
		AbstractGameAction.AttackEffect[] effects = {
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
				AbstractGameAction.AttackEffect.SLASH_VERTICAL
		};
		
		for (int i = 0; i < count; i++)
			this.addToBot(new DamageAction(
					m,
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					effects[AbstractDungeon.miscRng.random(effects.length - 1)]
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
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
			this.initializeDescription();
		}
	}
}
