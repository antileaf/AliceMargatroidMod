package me.antileaf.alice.cards.marisa;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class AliceSprinkleStarSeal extends AbstractAliceMarisaCard {
	public static final String SIMPLE_NAME = AliceSprinkleStarSeal.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final String IMG = "sprinkleSeal";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int MAGIC = 99;
	
	public AliceSprinkleStarSeal() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getImgFilePath("Marisa/cards", IMG),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARISA_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;
		
		this.setImages(IMG);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(m, p,
				new WeakPower(m, this.magicNumber, false),
				this.magicNumber, true));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new AliceSprinkleStarSeal();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
