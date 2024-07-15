package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.utils.AliceSpireKit;
import rs.antileaf.alice.utils.AliceTutorialHelper;

public class AlicesGrimoire extends CustomRelic implements ClickableRelic {
	public static final String SIMPLE_NAME = AlicesGrimoire.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = AliceSpireKit.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceSpireKit.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceSpireKit.getRelicLargeImgFilePath(SIMPLE_NAME);

	public AlicesGrimoire() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.STARTER,
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
		this.flash();
		this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		
//		for (int i = 0; i < 2; i++)
		this.addToBot(new SpawnDollAction(AbstractDoll.getRandomDoll(), -1));
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new AlicesGrimoire();
	}
	
	@Override
	public void onRightClick() {
		if (AliceSpireKit.isInBattle())
			AliceTutorialHelper.openTutorial();
	}
}
