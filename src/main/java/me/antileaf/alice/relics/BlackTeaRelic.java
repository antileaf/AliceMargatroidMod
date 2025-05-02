package me.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import me.antileaf.alice.patches.enums.AbstractPlayerEnum;
import me.antileaf.alice.utils.AliceHelper;

public class BlackTeaRelic extends CustomRelic {
	public static final String SIMPLE_NAME = BlackTeaRelic.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);

	private static final String IMG = AliceHelper.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceHelper.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceHelper.getRelicLargeImgFilePath(SIMPLE_NAME);

	public static final int HEAL = 7;

	public BlackTeaRelic() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.SPECIAL,
				LandingSound.CLINK
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);

		this.checkFlavor();
	}

	public void checkFlavor() {
		if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null &&
				AbstractDungeon.player.chosenClass == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS) {
			this.flavorText = this.DESCRIPTIONS[1];

			// No need to initialize tips, as the tips are not being changed
		}
	}
	
	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}

	public void onEquip() {
		AbstractDungeon.player.increaseMaxHp(HEAL, true);
		AbstractDungeon.player.heal(HEAL, true);
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new BlackTeaRelic();
	}
}
