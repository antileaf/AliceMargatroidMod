package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.common.AliceMoveToHandAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class TheSouthernCross extends AbstractAliceCard {
	public static final String SIMPLE_NAME = TheSouthernCross.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int DAMAGE = 3;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_DMG = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public TheSouthernCross() {
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
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		
		this.addToBot(new AliceMoveToHandAction(
				p.drawPile,
				this.magicNumber,
				(card) -> card.type == CardType.ATTACK && card.costForTurn > 0,
				(cards) -> {
					for (AbstractCard card : cards)
						AliceHelper.addActionToBuffer(new UpgradeSpecificCardAction(card));
					
					AliceHelper.commitBuffer();
				}
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new TheSouthernCross();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
