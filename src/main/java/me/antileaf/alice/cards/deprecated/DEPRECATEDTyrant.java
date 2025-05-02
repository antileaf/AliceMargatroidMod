package me.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.powers.deprecated.TyrantPower;

@Deprecated
public class DEPRECATEDTyrant extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDTyrant.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = -1;
	
	public DEPRECATEDTyrant() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.SELF
		);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int amount = this.energyOnUse;
		if (this.upgraded)
			amount += 1;
		
		if (p.hasRelic(ChemicalX.ID))
			amount += ChemicalX.BOOST;
		
		if (amount > 0) {
			amount *= 2;
			
			this.addToBot(new ApplyPowerAction(
					p,
					p,
					new TyrantPower(amount),
					amount
			));
			
			this.addToBot(new AnonymousAction(() -> {
				if (!this.freeToPlayOnce)
					p.energy.use(this.energyOnUse);
			}));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDTyrant();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
