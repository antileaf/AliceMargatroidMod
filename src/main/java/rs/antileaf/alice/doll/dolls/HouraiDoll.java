package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceHelper;

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
		AliceHelper.addActionToBuffer(new DrawCardAction(this.actAmount));
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
