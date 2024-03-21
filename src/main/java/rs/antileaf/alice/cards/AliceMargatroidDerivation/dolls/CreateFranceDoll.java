package rs.antileaf.alice.cards.AliceMargatroidDerivation.dolls;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.dolls.FranceDoll;
import rs.antileaf.alice.doll.dolls.LondonDoll;

public class CreateFranceDoll extends AbstractCreateDoll {
	public static final String SIMPLE_NAME = CreateFranceDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = -2;
	
	public int index;
	
	public CreateFranceDoll() {
		super(
				ID,
				cardStrings.NAME,
				null,
				COST,
				cardStrings.DESCRIPTION
		);
	}
	
	@Override
	public AbstractDoll getDoll() {
		return new FranceDoll();
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CreateFranceDoll();
	}
}
