package me.antileaf.alice.cards.marisa;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class AliceOortCloud extends AbstractAliceMarisaCard implements SpawnModificationCard {
	public static final String SIMPLE_NAME = AliceOortCloud.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final String IMG = "oort";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 4;
	private static final int MAGIC2 = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC2 = 1;
	
	public AliceOortCloud() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getImgFilePath("Marisa/cards", IMG),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARISA_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
		
		this.setImages(IMG);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		boolean shouldUseExtraEnergy = (!this.freeToPlayOnce && !this.purgeOnUse);
		
		this.addToBot(new AnonymousAction(() -> {
			int platedArmor = this.magicNumber;
			int amp = shouldUseExtraEnergy ? 1 : 0;
			
			if (EnergyPanel.getCurrentEnergy() >= amp) {
				if (amp > 0)
					AbstractDungeon.player.energy.use(amp);
				
				platedArmor += this.secondaryMagicNumber;
			}
			
			this.addToTop(new ApplyPowerAction(p, p, new PlatedArmorPower(p, platedArmor)));
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new AliceOortCloud();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			this.initializeDescription();
		}
	}
}
