package rs.antileaf.alice.doll.dolls.deprecated;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;
import java.util.Comparator;

@Deprecated
public class DEPRECATEDHouraiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = DEPRECATEDHouraiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 3;
	public static final int PASSIVE_AMOUNT = 3;
	public static final int ACT_AMOUNT = 3;
	
//	private int additionalPassiveAmount = 0;
	
	public DEPRECATEDHouraiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.BLOCK;
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
		AliceHelper.addActionToBuffer(new DollGainBlockAction(this, this.actAmount));
		AliceHelper.addActionToBuffer(new AnonymousAction(() -> {
			int total = 0;
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot)) {
					total += doll.block;
					doll.block = 0;
				}
			
			AliceHelper.logger.info("HouraiDoll: total = {}", total);
			
			int slot_count = DollManager.get().getDolls().size();
			ArrayList<AbstractDoll> dolls = DollManager.get().getDolls();
			
			int[] need = new int[slot_count], received = new int[slot_count];
			ArrayList<Integer> ids = new ArrayList<>();
			
			for (int i = 0; i < slot_count; i++) {
				AbstractDoll doll = dolls.get(i);
				need[i] = doll.calcTotalDamageAboutToTake();
				if (!(doll instanceof EmptyDollSlot) && need[i] > 0)
					ids.add(i);
			}
			
			ids.sort(Comparator.comparingInt(id -> need[id]));
			
			for (int i = 0, last = 0; i < ids.size(); i++) {
				int cur = need[ids.get(i)];
				
				if (total < (cur - last) * (ids.size() - i)) {
					int div = total / (ids.size() - i), mod = total % (ids.size() - i);
					for (int j = i; j < ids.size(); j++)
						received[ids.get(j)] += div;
					
					ids.subList(i, ids.size()).stream()
							.sorted()
							.limit(mod)
							.forEach(id -> received[id]++);
					
					total = 0;
					break;
				}
				
				received[ids.get(i)] = cur;
				total -= (cur - last) * (ids.size() - i);
				last = cur;
			}
			
			AliceHelper.logger.info("HouraiDoll: total = {}", total);
			
			int aliceReceived = 0;
			if (total > 0) {
				int alice = dolls.stream()
						.mapToInt(AbstractDoll::getOverflowedDamage)
						.sum();
				
				aliceReceived = Math.min(total, alice);
				total -= aliceReceived;
			}
			
			AliceHelper.logger.info("HouraiDoll: aliceReceived = {}", aliceReceived);
			
			if (total > 0) {
				AbstractDoll[] nonEmptyDolls = dolls.stream()
						.filter(doll -> !(doll instanceof EmptyDollSlot))
						.toArray(AbstractDoll[]::new);
				
				if (nonEmptyDolls.length > 0) {
					int div = total / nonEmptyDolls.length, mod = total % nonEmptyDolls.length;
					for (AbstractDoll doll : nonEmptyDolls)
						received[dolls.indexOf(doll)] += div;
					
					for (int i = 0; i < mod; i++)
						received[dolls.indexOf(nonEmptyDolls[i])]++;
				}
			}
			
			
			for (int i = 0; i < slot_count; i++)
				if (received[i] > 0)
					AliceHelper.addActionToBuffer(new DollGainBlockAction(dolls.get(i), received[i]));
			
			if (aliceReceived > 0)
				AliceHelper.addActionToBuffer(new GainBlockAction(AbstractDungeon.player, aliceReceived));
			
			AliceHelper.commitBuffer();
		}));
		
//		AliceSpireKit.commitBuffer();
		
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
		this.passiveDescription = String.format(dollStrings.PASSIVE_DESCRIPTION,
				this.coloredPassiveAmount());
		
//		if (!this.dontShowHPDescription)
//			this.passiveDescription += String.format(dollStrings.EXTENDED_DESCRIPTION[0],
//					DollManager.get().getTotalHouraiPassiveAmount());
		
		this.actDescription = String.format(dollStrings.ACT_DESCRIPTION, this.coloredActAmount());
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractOrb makeCopy() {
		return new DEPRECATEDHouraiDoll();
	}
	
	@Override
	public void playChannelSFX() {
		// TODO
	}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 10.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 28.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new DEPRECATEDHouraiDoll()).desc();
	}
}
