package me.antileaf.alice.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.monsters.MedicineMelancholy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractMedicineCard extends AbstractAliceCard {
	private static final Logger logger = LogManager.getLogger(AbstractMedicineCard.class);
	
	public AbstractMedicineCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			CardType type,
			CardColor color,
			CardRarity rarity,
			CardTarget target
	) {
		super(
				id,
				name,
				img,
				cost,
				rawDescription,
				type,
				color,
				rarity,
				target
		);
	}
	
	@Override
	public final void use(AbstractPlayer p, AbstractMonster m) {
		// Should not be used.
		logger.warn("AbstractMedicineCard.use called. This is not supposed to happen.");
	}
	
	public abstract void onUsedByMedicine(MedicineMelancholy medicine);
	
	public abstract CardIntent getIntent();
	
	@Override
	public final void calculateCardDamage(AbstractMonster mo) {}
	
	@Override
	public final void applyPowers() {}
	
	public void medicineApplyPowers(MedicineMelancholy medicine) {
		if (this.baseDamage != -1) {
			this.damage = medicine.calculateCardDamage(this.baseDamage);
			this.isDamageModified = this.damage != this.baseDamage;
		}
	}
	
	public static class CardIntent {
		public int damage = -1;
		public int multiAmt = -1;
		public int poison = -1;
		public boolean buff = false;
		public boolean debuff = false;
		public boolean strongDebuff = false;
		
		public CardIntent() {}
		
		public CardIntent damage(int damage) {
			this.damage = damage;
			return this;
		}
		
		public CardIntent multiAmt(int multiAmt) {
			this.multiAmt = multiAmt;
			return this;
		}
		
		public CardIntent poison(int poison) {
			this.poison = poison;
			return this;
		}
		
		public CardIntent buff() {
			this.buff = true;
			return this;
		}
		
		public CardIntent debuff() {
			this.debuff = true;
			return this;
		}
		
		public CardIntent strongDebuff() {
			this.strongDebuff = true;
			return this;
		}
	}
}
