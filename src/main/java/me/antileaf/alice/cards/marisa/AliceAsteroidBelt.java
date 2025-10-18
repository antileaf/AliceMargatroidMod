package me.antileaf.alice.cards.marisa;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class AliceAsteroidBelt extends AbstractAliceMarisaCard {
	public static final String SIMPLE_NAME = AliceAsteroidBelt.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int BLOCK = 8;
	private static final int UPGRADE_PLUS_BLOCK = 3;
	
	public AliceAsteroidBelt() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getImgFilePath("Marisa/cards", SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARISA_COLOR,
				CardRarity.COMMON,
				CardTarget.SELF
		);
		
		this.block = this.baseBlock = BLOCK;
		
		this.setImages(SIMPLE_NAME);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainBlockAction(p, p, this.block));
		
		boolean shouldUseExtraEnergy = (!this.freeToPlayOnce && !this.purgeOnUse);
		int block = this.block;
		
		this.addToBot(new AnonymousAction(() -> {
			int amp = shouldUseExtraEnergy ? 1 : 0;
			
			if (EnergyPanel.getCurrentEnergy() >= amp) {
				if (amp > 0)
					AbstractDungeon.player.energy.use(amp);
				else
					AliceHelper.log("AliceAsteroidBelt: Free to amplify.");
				
				this.addToTop(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block), block));
			}
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new AliceAsteroidBelt();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.initializeDescription();
		}
	}
}
