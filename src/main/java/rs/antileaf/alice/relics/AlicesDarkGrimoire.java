package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.utils.AliceHelper;
import rs.antileaf.alice.utils.AliceTutorialHelper;

public class AlicesDarkGrimoire extends CustomRelic implements ClickableRelic, OnDollOperateHook {
	public static final String SIMPLE_NAME = AlicesDarkGrimoire.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);

	private static final String IMG = AliceHelper.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceHelper.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceHelper.getRelicLargeImgFilePath(SIMPLE_NAME);
	
	public static final int MULTIPLIER = 4;

	public AlicesDarkGrimoire() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.BOSS,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
//		this.tips.add(new PowerTip(this.DESCRIPTIONS[2], this.DESCRIPTIONS[1]));
	}
	
	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0] + " NL " + this.DESCRIPTIONS[1];
	}
	
	@Override
	public void obtain() {
		if (AbstractDungeon.player.hasRelic(AlicesGrimoire.ID)) {
			this.instantObtain(AbstractDungeon.player,
					AbstractDungeon.player.relics.indexOf(
							AbstractDungeon.player.getRelic(AlicesGrimoire.ID)),
					false);
		} else {
			super.obtain();
		}
	}
	
	@Override
	public void preSpawnDoll(AbstractDoll doll) {
		int count = (int)DollManager.get().getDolls().stream()
				.filter(other -> other != doll && !(other instanceof EmptyDollSlot))
				.count();
		
		doll.maxHP += count * MULTIPLIER;
		doll.HP += count * MULTIPLIER;
		
		this.flash();
	}
	
	@Override
	public boolean canSpawn() {
		return AbstractDungeon.player.hasRelic(AlicesGrimoire.ID);
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new AlicesDarkGrimoire();
	}
	
	@Override
	public void onRightClick() {
		if (AliceHelper.isInBattle() && !AbstractDungeon.actionManager.turnHasEnded)
			AliceTutorialHelper.openTutorial();
	}
}
