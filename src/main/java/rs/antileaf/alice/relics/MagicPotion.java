package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.utils.AliceHelper;

public class MagicPotion extends CustomRelic {
	public static final String SIMPLE_NAME = MagicPotion.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);

	private static final String IMG = AliceHelper.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceHelper.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceHelper.getRelicLargeImgFilePath(SIMPLE_NAME);

	public MagicPotion() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.UNCOMMON,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public void atBattleStart() {
		this.counter = 0;
	}

	@Override
	public void atTurnStart() {
		if (!this.grayscale)
			this.counter++;

		if (this.counter == 2) {
			this.flash();
			this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			this.addToBot(new GainEnergyAction(2));
			this.counter = -1;
			this.grayscale = true;
		}
	}

	@Override
	public void onVictory() {
		this.counter = -1;
		this.grayscale = false;
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new MagicPotion();
	}
}
