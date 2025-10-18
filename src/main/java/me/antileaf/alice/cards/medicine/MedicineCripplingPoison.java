package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.green.CripplingPoison;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineCripplingPoison extends CripplingPoison {
	public static final String SIMPLE_NAME = MedicineCripplingPoison.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	public int upgradedCount = 0;
	
	public MedicineCripplingPoison() {
		super();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) { // should not be called in normal way
		if (this.upgradedCount < 2)
			super.use(p, m);
		else {
			if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
				for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
					if (!mo.isDead && !mo.isDying) {
						this.addToBot(new ApplyPowerAction(mo, p, new PoisonPower(mo, p, this.magicNumber), this.magicNumber));
						this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, 2, false), 2));
						this.addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, 2, false), 2));
					}
				}
			}
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
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
		
		this.upgradedCount++;
	}
}
