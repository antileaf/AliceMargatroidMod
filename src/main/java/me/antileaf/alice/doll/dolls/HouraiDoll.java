package me.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.strings.AliceDollStrings;
import me.antileaf.alice.utils.AliceHelper;

import java.util.stream.Stream;

public class HouraiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = HouraiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 4;
	private static final int PASSIVE_AMOUNT = 1;
	private static final int ACT_AMOUNT = 1;
	
	public HouraiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.NONE
		);
		
		this.passiveAmountType = DollAmountType.OTHERS;
		this.actAmountType = DollAmountType.OTHERS;

		this.tipImg = ImageMaster.loadImage(AliceHelper.getImgFilePath("UI", "Icon_CardDraw"));
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
	public void onAct(DollActModifier modifier) {
		AliceHelper.addActionToBuffer(new DrawCardAction(this.actAmount));
	}
	
	@Override
	public boolean skipActWaiting() {
		return AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID) ||
				(AbstractDungeon.player.drawPile.isEmpty() && AbstractDungeon.player.discardPile.isEmpty());
	}
	
	@Override
	public void onRecycle() {
		AliceHelper.addActionToBuffer(new GainEnergyAction(this.passiveAmount));
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.PASSIVE_DESCRIPTION,
				Stream.generate(() -> "[E]")
						.limit(this.passiveAmount)
						.reduce((a, b) -> a + " " + b)
						.orElse(""));
		
		this.actDescription = String.format(dollStrings.ACT_DESCRIPTION, this.coloredActAmount());
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new HouraiDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new HouraiDoll()).desc();
	}
}
