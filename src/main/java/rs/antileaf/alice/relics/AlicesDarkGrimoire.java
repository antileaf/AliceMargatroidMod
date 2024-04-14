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

public class AlicesDarkGrimoire extends CustomRelic implements ClickableRelic {
	public static final String SIMPLE_NAME = AlicesDarkGrimoire.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = AliceSpireKit.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceSpireKit.getRelicOutlineImgFilePath(SIMPLE_NAME);

	public AlicesDarkGrimoire() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.BOSS,
				LandingSound.MAGICAL
		);
	}
	
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void obtain() {
		if (AbstractDungeon.player.hasRelic(AlicesGrimoire.ID)) {
			for (AbstractRelic relic : AbstractDungeon.player.relics) {
				if (relic instanceof AlicesGrimoire) {
					this.instantObtain(AbstractDungeon.player,
							AbstractDungeon.player.relics.indexOf(relic),
							false);
					break;
				}
			}
		} else {
			super.obtain();
		}
	}
	
	@Override
	public void atBattleStart() {
		this.flash();
		this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		
//		for (int i = 0; i < 2; i++)
		for (int i = 0; i < 3; i++)
			this.addToBot(new SpawnDollAction(AbstractDoll.getRandomDoll(), -1));
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
		if (AliceSpireKit.isInBattle())
			AliceTutorialHelper.openTutorial();
	}
}
