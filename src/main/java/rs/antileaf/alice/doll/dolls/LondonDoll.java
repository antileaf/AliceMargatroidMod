package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.Settings;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceSpireKit;

public class LondonDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = LondonDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 1;
	
	public LondonDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceSpireKit.getOrbImgFilePath(SIMPLE_NAME),
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
	public void onAct() {
		int pos = -1;
		for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
			AbstractDoll doll = DollManager.get().getDolls().get(i);
			
			if (doll == this)
				break;
			
			if (!(doll instanceof EmptyDollSlot)) {
				pos = i;
				break;
			}
		}
		
		if (pos != -1) {
			int nowpos = DollManager.get().getIndex(this), finalPos = pos;
			AliceSpireKit.addActionToBuffer(new AnonymousAction(() -> {
				AbstractDoll other = DollManager.get().getDolls().get(finalPos);
				DollManager.get().getDolls().set(finalPos, this);
				DollManager.get().getDolls().set(nowpos, other);
				
				int temp1 = this.block, temp2 = other.block;
				this.block = other.block = 0;
				this.addBlock(temp2);
				other.addBlock(temp1);
			}));
		}
		else {
			AliceSpireKit.addActionToBuffer(new RecycleDollAction(this));
			
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
		}
	}
	
	private void triggerPassiveEffect() {
		if (this.block > 0) {
			int index = DollManager.get().getIndex(this);
			AbstractDoll prev = index > 0 ? DollManager.get().getDolls().get(index - 1) : null;
			AbstractDoll next = index < DollManager.MAX_DOLL_SLOTS - 1 ? DollManager.get().getDolls().get(index + 1) : null;
			
			if (prev != null && next != null) {
				if (!(prev instanceof EmptyDollSlot))
					AliceSpireKit.addActionToBuffer(new DollGainBlockAction(prev, this.block - this.block / 2));
				if (!(next instanceof EmptyDollSlot))
					AliceSpireKit.addActionToBuffer(new DollGainBlockAction(next, this.block / 2));
			}
			else if (prev != null) {
				if (!(prev instanceof EmptyDollSlot))
					AliceSpireKit.addActionToBuffer(new DollGainBlockAction(prev, this.block));
			}
			else if (next != null) {
				if (!(next instanceof EmptyDollSlot))
					AliceSpireKit.addActionToBuffer(new DollGainBlockAction(next, this.block));
			}
			
			this.block = 0;
			
			if (prev != null && !(prev instanceof EmptyDollSlot))
				AliceSpireKit.addActionToBuffer(new DollActAction(prev));
			if (next != null && !(next instanceof EmptyDollSlot))
				AliceSpireKit.addActionToBuffer(new DollActAction(next));
		}
	}
	
	@Override
	public void postSpawn() {
		this.triggerPassiveEffect();
		
//		this.addToBot(new AnonymousAction(() -> {
//			int index = -1;
//			for (int i = DollManager.MAX_DOLL_SLOTS - 1; i >= 0; i--) {
//				AbstractDoll doll = DollManager.get().getDolls().get(i);
//
//				if (doll instanceof EmptyDollSlot) {
//					index = i;
//					break;
//				}
//			}
//
//			if (index == -1)
//				AliceSpireKit.log(this.getClass(), "No empty doll slot found");
//
//			// Here addToTop() is necessary, because this block is wrapped in an AnonymousAction.
//			// Using addActionToBuffer() will cause the new doll to be spawned at an unexpected time.
//			this.addToTop(new SpawnDollAction(AbstractDoll.getRandomDollExcept(LondonDoll.ID), index));
//		}));
	}
	
	private void onRecycleOrDestroyed() {
		this.triggerPassiveEffect();
		
//		this.addToBot(new AnonymousAction(() -> {
//			int index = -1;
//			for (int i = DollManager.MAX_DOLL_SLOTS - 1; i >= 0; i--) {
//				AbstractDoll doll = DollManager.get().getDolls().get(i);
//
//				if ((doll instanceof EmptyDollSlot) || doll == this) {
//					index = i;
//					break;
//				}
//			}
//
//			if (index == -1)
//				AliceSpireKit.log(this.getClass(), "No empty doll slot found");
//
//			// Here addToTop() is necessary, because of the same reason as in postSpawn().
//			this.addToTop(new SpawnDollAction(AbstractDoll.getRandomDollExcept(LondonDoll.ID), index));
//		}));
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
		return new LondonDoll();
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
		return getHpDescription(MAX_HP) + " NL " + (new LondonDoll()).desc();
	}
}
