package me.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.utils.AliceHelper;

public class SacrificialDoll extends CustomRelic {
	public static final String SIMPLE_NAME = SacrificialDoll.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);

	private static final String IMG = AliceHelper.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceHelper.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceHelper.getRelicLargeImgFilePath(SIMPLE_NAME);

	public SacrificialDoll() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.COMMON,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void onPlayerEndTurn() {
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (doll instanceof EmptyDollSlot && doll.calcTotalDamageAboutToTake() != -1) {
				this.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 3));
				this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
				break;
			}
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new SacrificialDoll();
	}
}
