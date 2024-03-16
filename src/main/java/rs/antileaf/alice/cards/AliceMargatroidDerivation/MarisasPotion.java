package rs.antileaf.alice.cards.AliceMargatroidDerivation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rs.antileaf.alice.action.unique.KirisameMahoutenAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class MarisasPotion extends AbstractAliceCard {
	public static final String SIMPLE_NAME = MarisasPotion.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = -2;
	
	public AbstractPotion potion;
	
	public MarisasPotion(AbstractPotion potion) {
		super(
				ID,
				potion == null ? cardStrings.NAME : potion.name,
				null,
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_DERIVATION_COLOR,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);
		
		this.potion = potion;
		this.exhaust = true; // This card should not be gettable in any way.
	}
	
	public MarisasPotion() {
		this(null);
	}
	
	@Override
	public void onChoseThisOption() {
		if (this.potion != null) {
			AbstractDungeon.getCurrRoom().addPotionToRewards(this.potion.makeCopy());
			this.addToTop(new KirisameMahoutenAction(this.potion.name));
		}
		else
			AliceSpireKit.log("Marisa's Potion: Potion is null.");
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.onChoseThisOption();
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new MarisasPotion(this.potion);
	}
	
	@Override
	public boolean canUpgrade() {
		return false;
	}
	
	@Override
	public void upgrade() {
		// No upgrade.
	}
}
