package rs.antileaf.alice.cards.alice;

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
import rs.antileaf.alice.action.common.AliceDiscoverAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class Lantern extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Lantern.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 4;
	private static final int UPGRADE_PLUS_DMG = 3;
	
	public Lantern() {
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
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.FIRE));
		
		this.addToBot(new AliceDiscoverAction(
				() -> {
					ArrayList<AbstractCard> cards = new ArrayList<>();
					for (AbstractCard c : p.drawPile.group)
						if (c.type == CardType.ATTACK)
							cards.add(c);
					
					ArrayList<AbstractCard> result = new ArrayList<>();
					for (int i = 0; i < 3; i++)
						if (!cards.isEmpty()) {
							AbstractCard c = cards.remove(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
							result.add(c);
						}
					
					return result;
				},
				(card) -> {
					if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
						AbstractDungeon.player.drawPile.moveToDiscardPile(card);
						AbstractDungeon.player.createHandIsFullDialog();
					} else {
						AbstractDungeon.player.drawPile.moveToHand(card);
						card.setCostForTurn(card.costForTurn - 1);
					}
				},
				cardStrings.EXTENDED_DESCRIPTION[0],
				false
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Lantern();
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
