package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.common.AliceFatalAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.powers.common.AliceExtraTurnPower;
import me.antileaf.alice.utils.AliceHelper;

public class CollapsingWorlds extends AbstractAliceCard /* implements ConditionalExhaustCard */ {
	public static final String SIMPLE_NAME = CollapsingWorlds.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 16;

//	public boolean exhaustThisTime = false;
	
	public CollapsingWorlds() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AliceFatalAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.FIRE,
				(fatal) -> {
					if (fatal)
						AliceHelper.addToTop(new ApplyPowerAction(p, p, new AliceExtraTurnPower(1), 1));

//					this.exhaustThisTime = fatal;
					// The logic of conditional exhaust is implemented in patches
				},
				true
		));
	}

//	@Override
//	public boolean shouldExhaust() {
//		boolean res = this.exhaustThisTime;
//		this.exhaustThisTime = false;
//		return res;
//	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CollapsingWorlds();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
//			this.upgradeBaseCost(UPGRADED_COST);
			this.retain = this.selfRetain = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
