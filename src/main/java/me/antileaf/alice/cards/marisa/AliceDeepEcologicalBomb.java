package me.antileaf.alice.cards.marisa;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import me.antileaf.alice.action.unique.MarisaWasteBombAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class AliceDeepEcologicalBomb extends AbstractAliceMarisaCard {
	public static final String SIMPLE_NAME = AliceDeepEcologicalBomb.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final String IMG = "DeepEcoBomb";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 7;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_DAMAGE = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public AliceDeepEcologicalBomb() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getImgFilePath("Marisa/cards", IMG),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARISA_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		
		this.setImages(IMG);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		boolean shouldUseExtraEnergy = (!this.freeToPlayOnce && !this.purgeOnUse);
		
		this.addToBot(new AnonymousAction(() -> {
			int amp = shouldUseExtraEnergy ? 1 : 0;
			int time = 1;
			
			if (EnergyPanel.getCurrentEnergy() >= amp) {
				if (amp > 0)
					AbstractDungeon.player.energy.use(amp);
				
				time += 1;
			}
			
			this.addToTop(new MarisaWasteBombAction(AbstractDungeon.getMonsters().getRandomMonster(true),
					this.damage, time, this.magicNumber));
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new AliceDeepEcologicalBomb();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
