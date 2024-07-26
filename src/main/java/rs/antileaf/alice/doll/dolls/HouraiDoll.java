package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class HouraiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = HouraiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 2;
	public static final int PASSIVE_AMOUNT = 1;
	public static final int ACT_AMOUNT = 3;
	
//	private int additionalPassiveAmount = 0;
	
	public HouraiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("white"),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.BLOCK;
	}
	
	@Override
	public AbstractOrb makeCopy() {
		return new HouraiDoll();
	}
	
	@Override
	public void playChannelSFX() {
		// TODO
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
		AliceSpireKit.addActionToBuffer(new DollGainBlockAction(this, this.actAmount));
		AliceSpireKit.addActionToBuffer(new AnonymousAction(() -> {
			if (this.block > 0) {
				ArrayList<AbstractDoll> receivers = new ArrayList<>();
				for (AbstractDoll doll : DollManager.get().getDolls())
					if (!(doll instanceof EmptyDollSlot) && doll.calcTotalDamageAboutToTake() > 0)
						receivers.add(doll);
				
				if (!receivers.isEmpty()) {
					int amount = this.block;
					this.block = 0;
					
					int[] gain = new int[receivers.size()];
					for (int i = 0; i < receivers.size(); i++) {
						AbstractDoll doll = receivers.get(i);
						if (doll.block < doll.calcTotalDamageAboutToTake()) {
							int amt = Math.min(amount, doll.calcTotalDamageAboutToTake() - doll.block);
							gain[i] = amt;
							amount -= amt;
						}
					}
					
					if (amount > 0) {
						int avg = amount / receivers.size();
						for (int i = 0; i < receivers.size(); i++) {
							gain[i] += avg;
							if (i < amount % receivers.size())
								gain[i]++;
						}
					}
					
					for (int i = 0; i < receivers.size(); i++)
						AliceSpireKit.addActionToBuffer(new DollGainBlockAction(receivers.get(i), gain[i]));
					
					AliceSpireKit.commitBuffer();
				}
			}
		}));
		
		AliceSpireKit.commitBuffer();
		
		this.highlightActValue();
//		this.applyPower();
	}
	
//	@Override
//	public void applyPower() {
//		this.passiveAmount = this.basePassiveAmount =
//				DollManager.getInstance(AbstractDungeon.player).getTotalHouraiPassiveAmount();
//
//		this.updateDescription();
//	}
	
	@Override
	public void updateDescriptionImpl() {
		if (this.dontShowHPDescription)
			this.passiveDescription = String.format(dollStrings.DESCRIPTION[0], this.coloredPassiveAmount());
		else {
//			this.passiveDescription = AliceMiscKit.join(
//					dollStrings.DESCRIPTION[1],
//					this.coloredPassiveAmount(),
//					dollStrings.DESCRIPTION[2],
//					"" + DollManager.get().getTotalHouraiPassiveAmount(),
//					dollStrings.DESCRIPTION[3]
//			);
			this.passiveDescription = String.format(dollStrings.DESCRIPTION[1],
					this.coloredPassiveAmount(), DollManager.get().getTotalHouraiPassiveAmount());
		}
		
		this.actDescription = String.format(dollStrings.DESCRIPTION[2], this.coloredActAmount());
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new HouraiDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
