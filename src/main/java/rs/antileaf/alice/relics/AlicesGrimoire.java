package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.HouraiDoll;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
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
//		this.tips.add(new PowerTip(this.DESCRIPTIONS[2], this.DESCRIPTIONS[1]));
	}
	
	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0] + " NL " + this.DESCRIPTIONS[1];
	}
	
	@Override
	public void atBattleStart() {
		this.flash();
		this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		
		this.addToBot(new SpawnDollAction(new ShanghaiDoll(), -1));
		this.addToBot(new AnonymousAction(() -> {
			for (int i = DollManager.MAX_DOLL_SLOTS - 1; i >= 0; i--)
				if (DollManager.get().getDolls().get(i) instanceof EmptyDollSlot) {
					this.addToTop(new SpawnDollAction(new HouraiDoll(), i));
					break;
				}
		}));
	}
	
	@Override
	public void onRightClick() {
		if (AliceSpireKit.isInBattle() && !AbstractDungeon.actionManager.turnHasEnded)
			AliceTutorialHelper.openTutorial();
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new AlicesGrimoire();
	}
}
