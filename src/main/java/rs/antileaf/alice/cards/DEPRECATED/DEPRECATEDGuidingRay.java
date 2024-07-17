package rs.antileaf.alice.cards.DEPRECATED;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;

import java.util.ArrayList;

@Deprecated
public class DEPRECATEDGuidingRay extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDGuidingRay.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int DAMAGE = 8;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public DEPRECATEDGuidingRay() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.tags.add(CardTagEnum.ALICE_RAY);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		
		this.addToBot(new AnonymousAction(() -> {
			ArrayList<AbstractCard> rayCards = new ArrayList<>();
			for (AbstractCard c : p.drawPile.group)
				if (c.hasTag(CardTagEnum.ALICE_RAY))
					rayCards.add(c);
			
			for (int i = 0; i < this.magicNumber && !rayCards.isEmpty(); i++) {
				if (AbstractDungeon.player.hand.size() == BaseMod.MAX_HAND_SIZE) {
					AbstractDungeon.player.createHandIsFullDialog();
					break;
				}
				
				AbstractCard c = rayCards.get(AbstractDungeon.cardRandomRng.random(rayCards.size() - 1));
				rayCards.remove(c);
				p.drawPile.moveToHand(c, p.drawPile);
				c.setCostForTurn(c.costForTurn - 1);
			}
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDGuidingRay();
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
