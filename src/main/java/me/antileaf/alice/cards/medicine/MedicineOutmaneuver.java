package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.cards.AbstractMedicineCard;
import me.antileaf.alice.monsters.MedicineMelancholy;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineOutmaneuver extends AbstractMedicineCard {
	public static final String SIMPLE_NAME = MedicineOutmaneuver.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public MedicineOutmaneuver() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath("medicine/outmaneuver"),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MEDICINE_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
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
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
