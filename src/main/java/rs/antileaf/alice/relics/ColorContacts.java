package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ColorContacts extends CustomRelic {
	public static final String SIMPLE_NAME = ColorContacts.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = AliceSpireKit.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceSpireKit.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceSpireKit.getRelicLargeImgFilePath(SIMPLE_NAME);

	public ColorContacts() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.BOSS,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atPreBattle() {
		this.setCounter(0);
		this.grayscale = false;
	}
	
	@Override
	public void atTurnStart() {
		if (this.counter < 0)
			return;
		
		this.setCounter(this.counter + 1);
		
		if (this.counter <= 2) {
			this.flash();
			this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			this.addToBot(new GainEnergyAction(1));
			this.addToBot(new DrawCardAction(1));
		}
	}
	
	@Override
	public void onPlayerEndTurn() {
		if (this.counter >= 2) {
			this.setCounter(-1);
			this.grayscale = true;
		}
	}
	
	@Override
	public void onVictory() {
		this.setCounter(-1);
		this.grayscale = false;
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new ColorContacts();
	}
}
