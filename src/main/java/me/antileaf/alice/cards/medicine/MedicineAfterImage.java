package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.cards.AbstractMedicineCard;
import me.antileaf.alice.monsters.MedicineMelancholy;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineAfterImage extends AbstractMedicineCard {
	public static final String SIMPLE_NAME = MedicineAfterImage.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
	
	public MedicineAfterImage() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath("medicine/after_image"),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MEDICINE_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.secondaryMagicNumber = MAGIC;
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
