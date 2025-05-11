package me.antileaf.alice.doll.dolls.deprecated;

import com.megacrit.cardcrawl.core.Settings;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.strings.AliceDollStrings;
import me.antileaf.alice.utils.AliceHelper;

@Deprecated
public class DEPRECATEDLondonDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = DEPRECATEDLondonDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 2;
	
	public DEPRECATEDLondonDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.NONE
		);
		
		this.passiveAmountType = DollAmountType.OTHERS;
		this.actAmountType = DollAmountType.DAMAGE;
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
//		int pos = -1;
//		for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
//			AbstractDoll doll = DollManager.get().getDolls().get(i);
//
//			if (doll == this)
//				break;
//
//			if (!(doll instanceof EmptyDollSlot)) {
//				pos = i;
//				break;
//			}
//		}
//
//		if (pos != -1) {
//			int nowpos = DollManager.get().getIndex(this), finalPos = pos;
//			AliceSpireKit.addActionToBuffer(new AnonymousAction(() -> {
//				AbstractDoll other = DollManager.get().getDolls().get(finalPos);
//				DollManager.get().getDolls().set(finalPos, this);
//				DollManager.get().getDolls().set(nowpos, other);
//
//				int temp1 = this.block, temp2 = other.block;
//				this.block = other.block = 0;
//				this.addBlock(temp2);
//				other.addBlock(temp1);
//			}));
//		}
//		else {
//			// Do not trigger Doll Ambush please
//			AliceSpireKit.addActionToBuffer(new RecycleDollAction(this, null, true));
			
//			AbstractMonster m = AbstractDungeon.getRandomMonster();
//			if (m != null) {
//				AliceSpireKit.addActionToBuffer(new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.0F));
//				AliceSpireKit.addActionToBuffer(new DamageAction(
//							m,
//							new DollDamageInfo(
//									this.actAmount,
//									this,
//									this.actAmountType,
//									DollAmountTime.ACT
//							)
//				));
//				this.highlightActValue();
//			}
//		}

		int index = DollManager.get().getIndex(this);
		AbstractDoll prev = index > 0 ? DollManager.get().getDolls().get(index - 1) : null;
		AbstractDoll next = index < DollManager.MAX_DOLL_SLOTS - 1 ? DollManager.get().getDolls().get(index + 1) : null;

//		AliceSpireKit.logger.info("prev: {}, next: {}",
//				prev != null ? prev.name : "null",
//				next != null ? next.name : "null");

		if (this.block > 0) {
			if (prev != null && !(prev instanceof EmptyDollSlot) && next != null && !(next instanceof EmptyDollSlot)) {
				AliceHelper.addActionToBuffer(new DollGainBlockAction(prev, this.block - this.block / 2));
				AliceHelper.addActionToBuffer(new DollGainBlockAction(next, this.block / 2));
			}
			else if (prev != null && !(prev instanceof EmptyDollSlot)) {
				AliceHelper.addActionToBuffer(new DollGainBlockAction(prev, this.block));
			}
			else if (next != null && !(next instanceof EmptyDollSlot)) {
				AliceHelper.addActionToBuffer(new DollGainBlockAction(next, this.block));
			}

			if ((prev != null && !(prev instanceof EmptyDollSlot)) ||
					(next != null && !(next instanceof EmptyDollSlot)))
				this.block = 0;
		}

		if (prev != null && !(prev instanceof EmptyDollSlot) && !(prev instanceof DEPRECATEDLondonDoll)) {
			AliceHelper.addActionToBuffer(new DollActAction(prev));
		}
		if (next != null && !(next instanceof EmptyDollSlot) && !(next instanceof DEPRECATEDLondonDoll)) {
			AliceHelper.addActionToBuffer(new DollActAction(next));
		}
	}
	
//	private void triggerPassiveEffect() {
//	}
	
//	@Override
//	public void postSpawn() {
//
//	}
	
	private void onRecycleOrDestroyed() {
		this.onAct(new DollActModifier());
	}
	
	@Override
	public void onRecycle() {
		this.onRecycleOrDestroyed();
	}
	
	@Override
	public void onDestroyed() {
		this.onRecycleOrDestroyed();
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = dollStrings.PASSIVE_DESCRIPTION;
		this.actDescription = dollStrings.ACT_DESCRIPTION;
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new DEPRECATEDLondonDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 10.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 28.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new DEPRECATEDLondonDoll()).desc();
	}
}
