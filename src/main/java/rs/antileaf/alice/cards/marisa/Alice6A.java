package rs.antileaf.alice.cards.marisa;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class Alice6A extends AbstractAliceMarisaCard implements SpawnModificationCard {
	public static final String SIMPLE_NAME = Alice6A.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 5;
	private static final int UPGRADE_PLUS_DAMAGE = 2;
	
	public Alice6A() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getImgFilePath("Marisa/cards", SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARISA_COLOR,
				CardRarity.COMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		
		this.setImages(SIMPLE_NAME);
	}
	
	@Override
	public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
		return false;
	}
	
	@Override
	public boolean canSpawnShop(ArrayList<AbstractCard> currentShopCards) {
		return false;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageCallbackAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
				(amount) -> {
					if (amount > 0)
						AbstractDungeon.player.addBlock(amount);
				}
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Alice6A();
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
