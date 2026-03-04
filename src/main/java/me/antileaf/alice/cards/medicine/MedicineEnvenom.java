package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.green.CripplingPoison;
import com.megacrit.cardcrawl.cards.green.Envenom;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineEnvenom extends Envenom {
	public static final String SIMPLE_NAME = MedicineEnvenom.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	public int upgradedCount = 0;
	
	public MedicineEnvenom() {
		super();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) { // should not be called in normal way
		if (this.upgradedCount < 2)
			super.use(p, m);
		else {
			this.addToBot(new ApplyPowerAction(p, p, new EnvenomPower(p, 2)));
		}
	}
	
	@Override
	public boolean canUpgrade() {
		return this.upgradedCount < 2;
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded)
			super.upgrade();
		else if (this.upgradedCount < 2) {
			this.upgradedCount++;
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
