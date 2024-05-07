package rs.antileaf.alice.cards.Marisa;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceSpireKit;

public class AliceSpark extends CustomCard {
	public static final String SIMPLE_NAME = AliceSpark.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String IMG = AliceSpireKit.getImgFilePath("Marisa/cards", SIMPLE_NAME);
	
	private static final int COST = 0;
	private static final int DAMAGE = 4;
	private static final int UPGRADE_PLUS_DAMAGE = 2;
	
	public AliceSpark() {
		super(
				ID,
				cardStrings.NAME,
				IMG,
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARISA_COLOR,
				CardRarity.SPECIAL,
				CardTarget.ENEMY
		);
		
		this.exhaust = true;
		this.damage = this.baseDamage = DAMAGE;
		
		if (!AliceConfigHelper.enableAlternativeMarisaCardImage()) {
			this.textureImg = AliceSpireKit.getImgFilePath("Marisa/cards", SIMPLE_NAME + "_original");
			this.loadCardImage(this.textureImg);
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new AliceSpark();
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
