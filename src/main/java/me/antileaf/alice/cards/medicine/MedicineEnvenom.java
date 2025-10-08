package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.cards.AbstractMedicineCard;
import me.antileaf.alice.monsters.MedicineMelancholy;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineEnvenom extends AbstractMedicineCard {
	public static final String SIMPLE_NAME = MedicineEnvenom.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public int upgradedCount = 0;
	
	public MedicineEnvenom() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath("medicine/envenom"),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MEDICINE_COLOR,
				CardRarity.RARE,
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
	public boolean canUpgrade() {
		return this.upgradedCount < 2;
	}
	
	@Override
	public void upgrade() {
		if (this.upgradedCount < 2) {
			this.upgradeName();
			this.upgradedCount++;
			
			if (this.upgradedCount <= 1)
				this.upgradeBaseCost(UPGRADED_COST);
			else
				this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
		}
	}
}
