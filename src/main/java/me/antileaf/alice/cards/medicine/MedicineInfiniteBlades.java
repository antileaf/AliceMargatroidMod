package me.antileaf.alice.cards.medicine;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.green.CripplingPoison;
import com.megacrit.cardcrawl.cards.green.InfiniteBlades;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineInfiniteBlades extends InfiniteBlades {
	public static final String SIMPLE_NAME = MedicineInfiniteBlades.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	public MedicineInfiniteBlades() {
		super();
		
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			super.upgrade();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
