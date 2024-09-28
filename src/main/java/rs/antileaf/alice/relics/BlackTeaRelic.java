package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class BlackTeaRelic extends CustomRelic {
	public static final String SIMPLE_NAME = BlackTeaRelic.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = AliceSpireKit.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceSpireKit.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceSpireKit.getRelicLargeImgFilePath(SIMPLE_NAME);

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

		if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null &&
				AbstractDungeon.player.chosenClass == AbstractPlayerEnum.ALICE_MARGATROID) {
			this.flavorText = this.DESCRIPTIONS[1];

//			this.tips.clear();
//			this.tips.add(new PowerTip(this.name, this.description));
//			this.initializeTips();
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
