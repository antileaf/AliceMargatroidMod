package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import me.antileaf.alice.cards.AbstractMedicineCard;
import me.antileaf.alice.monsters.MedicineMelancholy;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class MedicinePoisonedStab extends AbstractMedicineCard {
	public static final String SIMPLE_NAME = MedicinePoisonedStab.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 6;
	private static final int MAGIC = 3;
	private static final int UPGRADE_PLUS_DMG = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public MedicinePoisonedStab() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath("medicine/poisoned_stab"),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MEDICINE_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void onUsedByMedicine(MedicineMelancholy medicine) {
		// TODO
	}
	
	@Override
	public CardIntent getIntent() {
		return new CardIntent()
				.damage(this.damage)
				.poison(this.magicNumber);
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
