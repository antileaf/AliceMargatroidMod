package me.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.action.doll.DollLoseBlockAction;
import me.antileaf.alice.action.doll.MoveDollAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.strings.AliceDollStrings;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FranceDoll extends AbstractDoll {
	private static final Logger logger = LogManager.getLogger(FranceDoll.class.getName());

	public static final String SIMPLE_NAME = FranceDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 10;
	public static final int ACT_AMOUNT = 5;

	private boolean duringSpecialAct = false;
	
	public FranceDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				ACT_AMOUNT,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.ACT
		);
		
//		this.passiveAmountType = DollAmountType.BLOCK;
		this.actAmountType = DollAmountType.BLOCK;

		this.tipImg = ImageMaster.loadImage(AliceHelper.getImgFilePath("UI", "Icon_Defend"));
	}
	
	@Override
	public AliceDollStrings getDollStrings() {
		return dollStrings;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public int getBaseHP() {
		return MAX_HP;
	}

	@Override
	public void onAct() {
		if (!this.duringSpecialAct) { // 如果不是人偶伏兵引起的行动，才会试图移动
			int dest = -1;
			if (this.getOverflowedDamage() <= 0) {
				for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
					AbstractDoll doll = DollManager.get().getDolls().get(i);
					if (!(doll instanceof FranceDoll) && doll.getOverflowedDamage() > 0) {
						if (doll.calcTotalDamageAboutToTake() > this.calcTotalDamageAboutToTake()) {
							if (dest == -1 || doll.calcTotalDamageAboutToTake() >
									DollManager.get().getDolls().get(dest).calcTotalDamageAboutToTake()) {
								dest = i;
							}
						}
					}
				}
			}

			if (dest != -1) {
				if (DollManager.get().getDolls().get(dest) != this)
					AliceHelper.addActionToBuffer(new MoveDollAction(this, dest));
				else
					logger.warn("FranceDoll.onAct: dest == this");
			}
		}

		AliceHelper.addActionToBuffer(new DollGainBlockAction(this, this.actAmount));
		this.highlightActValue();
	}

	@Override
	public void onSpecialAct(DollActModifier modifier) { // 正常来说只有即将被回收时触发人偶伏兵才会是特殊行动
		if (modifier.dollAmbush)
			this.duringSpecialAct = true;

		this.onAct();

		if (modifier.dollAmbush)
			this.duringSpecialAct = false;
	}
	
	@Override
	public void onStartOfTurn() {
		this.addToBot(new DollLoseBlockAction(this, this.block));

		if (AbstractDungeon.player.currentBlock > 0)
			this.addToBot(new DollGainBlockAction(this, AbstractDungeon.player.currentBlock));
	}
	
//	public void triggerPassiveEffect(int amount) {
//		this.addToBot(new DollGainBlockAction(this, amount));
//	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new FranceDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 14.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 30.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new FranceDoll()).desc();
	}
}
