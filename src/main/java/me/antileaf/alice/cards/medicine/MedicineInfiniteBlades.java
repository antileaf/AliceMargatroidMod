package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.cards.AbstractMedicineCard;
import me.antileaf.alice.monsters.MedicineMelancholy;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineInfiniteBlades extends AbstractMedicineCard {
	public static final String SIMPLE_NAME = MedicineInfiniteBlades.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	
	public MedicineInfiniteBlades() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR, // TODO
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
	}
	
	@Override
	public void onUsedByMedicine(MedicineMelancholy medicine) {
		// TODO
	}
	
	@Override
	public CardIntent getIntent() {
		return new CardIntent().buff();
	}
	
	@Override
	public boolean canUpgrade() {
		return false;
	}
	
	@Override
	public void upgrade() {
	}
}
