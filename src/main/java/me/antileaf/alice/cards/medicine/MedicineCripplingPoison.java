package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.cards.AbstractMedicineCard;
import me.antileaf.alice.monsters.MedicineMelancholy;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineCripplingPoison extends AbstractMedicineCard {
	public static final String SIMPLE_NAME = MedicineCripplingPoison.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int MAGIC = 4;
	private static final int MAGIC2 = 2;
	private static final int UPGRADE_PLUS_MAGIC = 3;
	
	public int upgradedCount = 0;
	
	public MedicineCripplingPoison() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath("medicine/crippling_poison"),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MEDICINE_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}
	
	@Override
	public void onUsedByMedicine(MedicineMelancholy medicine) {
		// TODO
	}
	
	@Override
	public CardIntent getIntent() {
		CardIntent intent = new CardIntent().poison(this.magicNumber);
		return this.upgradedCount < 2 ? intent.debuff() : intent.strongDebuff();
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
				this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			else
				this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			
			this.initializeDescription();
		}
	}
}
