package rs.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.utils.AliceHelper;

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
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				potion == null ? cardStrings.DESCRIPTION : potion.description,
				CardType.SKILL,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);
		
		this.potion = potion;
		this.exhaust = true; // This card should not be gettable in any way.
//		this.initializeDescription();
		
		if (this.potion != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < this.potion.description.length(); i++) {
				if (this.potion.description.charAt(i) == '#') {
					i++;
					continue;
				} else if (Character.isUpperCase(this.potion.description.charAt(i))) {
					sb.append(Character.toLowerCase(this.potion.description.charAt(i)));
				}
				else
					sb.append(this.potion.description.charAt(i));
			}
			
			this.rawDescription = sb.toString();
			this.initializeDescription();
		}
	}
	
	public MarisasPotion() {
		this(null);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}
	
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
