package rs.antileaf.alice.doll;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.MoveDollAction;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.action.doll.SpawnDollInternalAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AliceMargatroid.DollMagic;
import rs.antileaf.alice.characters.AliceMargatroid;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.FranceDoll;
import rs.antileaf.alice.doll.dolls.HouraiDoll;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.powers.unique.ArtfulChanterPower;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DollManager {
	public static final int MAX_DOLL_SLOTS = 7;
	
	private static final HashMap<AbstractPlayer, DollManager> instances = new HashMap<>();
	
	public static DollManager getInstance(AbstractPlayer p) {
		assert instances != null;
		
		if (!instances.containsKey(p))
			instances.put(p, new DollManager(p));
		return instances.get(p);
	}
	
	public static DollManager get() {
		return DollManager.getInstance(AbstractDungeon.player);
	}
	
	public static void clearInstances() {
		instances.clear();
	}
	
//	public static final String SIMPLE_NAME = DollManager.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(DollManager.SIMPLE_NAME);
//	private static final UIStrings uiStrings = AliceSpireKit.getUIString(DollManager.ID);
//	public static final String[] TEXT = uiStrings.TEXT;
	
	private final AbstractPlayer owner;
	private final ArrayList<AbstractDoll> dolls;
	
	private int preservedBlock = 0;
	
	private boolean shown = false;
	private int totalHouraiPassiveAmount = 0;
	
	public HashMap<AbstractMonster, Integer> damageTarget = new HashMap<>();
	public boolean isDamageTargetLocked = false;
	
	public DollManager(AbstractPlayer p) {
		AliceSpireKit.log(this.getClass(), "DollManager constructor");
		
		this.owner = p;
		this.dolls = new ArrayList<>();
	}
	
	public void initPreBattle() {
		this.shown = (this.owner instanceof AliceMargatroid);
		
		this.isDamageTargetLocked = false;
		this.damageTarget.clear();
		
		this.dolls.clear();
		for (int i = 0; i < MAX_DOLL_SLOTS; i++)
			this.dolls.add(new EmptyDollSlot());
		
//		this.formation = Formation.NORMAL;
		
		AliceSpireKit.log(this.getClass(), "DollManager.initPreBattle done");
		this.debug();
	}
	
	public void debug() {
		AliceSpireKit.logger.info("DollManager.Dolls: {}",
				this.dolls.stream().map(doll -> doll.getClass().getSimpleName())
						.reduce((a, b) -> a + ", " + b).orElse("None"));
	}
	
	public void clearPostBattle() {
		this.dolls.clear();
		this.shown = false;
		
		this.isDamageTargetLocked = false;
		this.damageTarget.clear();
		
		this.preservedBlock = 0;
		this.totalHouraiPassiveAmount = 0;
	}
	
	public boolean isShown() {
		return this.shown;
	}
	
	private void activateInternal() {
		this.shown = true;
	}
	
	public void addBlock(AbstractDoll doll, int block) {
		doll.addBlock(block);
		
		for (AbstractRelic relic : AbstractDungeon.player.relics)
			if (relic instanceof OnDollOperateHook)
				((OnDollOperateHook) relic).postDollGainedBlock(doll, block);
		
		for (AbstractPower power : AbstractDungeon.player.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).postDollGainedBlock(doll, block);
	}
	
	public void loseBlock(AbstractDoll doll, int block) {
		doll.loseBlock(block);
	}
	
	public void updatePreservedBlock() {
		this.preservedBlock = 0;
//		for (AbstractDoll doll : this.dolls)
//			if (doll instanceof KyotoDoll) {
//				this.preservedBlock += doll.passiveAmount;
//			}
	}
	
	public int getPreservedBlock() {
		return this.preservedBlock;
	}
	
	public void startOfTurnClearBlock(int playerBlock) {
		if (this.preservedBlock != 0)
			AliceSpireKit.log("DollManager", "Preserved block is not 0: " + this.preservedBlock);
		
		for (AbstractDoll doll : this.dolls) {
			doll.clearBlock(this.preservedBlock);
			
			if (doll instanceof FranceDoll)
				((FranceDoll) doll).triggerPassiveEffect(playerBlock);
		}
	}
	
	public void updateDamageAboutToTake() {
		if (this.dolls == null || this.dolls.isEmpty())
			return;
		
		if (this.isDamageTargetLocked) {
			for (AbstractDoll doll : this.dolls)
				doll.updateOverflowedDamage();
			
			return;
		}
		
		for (AbstractDoll doll : this.dolls)
			doll.updateDamageAboutToTake(-1, 0);
		
		this.damageTarget.clear();
		for (int i = 0; i < MAX_DOLL_SLOTS; i++) {
			AbstractMonster monster = AliceSpireKit.getMonsterByIndex(i);
			
			if (monster == null)
				continue;
			
			int index = i;
			if (monster instanceof SpireShield)
				index = MAX_DOLL_SLOTS - 1;
			else if (monster instanceof SpireSpear)
				index = 0;
			
			this.damageTarget.put(monster, index);
		}
		
		if (!AliceSpireKit.isInBattle()) {
			AliceSpireKit.log(this.getClass(), "Not in battle. Maybe you used some debugging tools?");
			return;
		}
		
		for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
			if (monster == null || monster.isDeadOrEscaped())
				continue;
			
			if (!this.damageTarget.containsKey(monster)) {
				AliceSpireKit.log("DollManager", "Failed to get target index for " + monster.name);
				continue;
			}
			
			int index = this.damageTarget.get(monster);
			int damage = -1, count = 0;
			
			if (monster.intent == AbstractMonster.Intent.ATTACK ||
					monster.intent == AbstractMonster.Intent.ATTACK_BUFF ||
					monster.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
					monster.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
				damage = Math.max(monster.getIntentDmg(), 0);
				try {
					count = ReflectionHacks.getPrivate(monster,
							AbstractMonster.class, "intentMultiAmt");
					if (count < 1)
						count = 1;
				}
				catch (NullPointerException e) {
					AliceSpireKit.log(this.getClass(), " updateDamageAboutToTake() Failed to get intentMultiAmt.");
					count = 1;
				}
			}
			
			if (this.dolls.get(index) != null)
				this.dolls.get(index).updateDamageAboutToTake(damage, count);
			else
				AliceSpireKit.log(this.getClass(), "doll is null");
		}
	}
	
//	@Deprecated
//	public void startOfTurnResetHouraiPassiveAmount() {
//		this.updateTotalHouraiPassiveAmount();
//		int cur = this.totalHouraiPassiveAmount;
//
//		for (AbstractDoll doll : this.dolls)
//			if (doll instanceof HouraiDoll)
//				((HouraiDoll) doll).resetPassiveAmount();
//
//		this.applyPowers();
//
//		int diff = this.totalHouraiPassiveAmount - cur;
//		if (diff != 0) {
//			for (AbstractDoll doll : this.dolls)
//				if (doll instanceof NetherlandsDoll)
//					((NetherlandsDoll) doll).onStartOfTurnUpdate(diff);
//		}
//	}
	
	public void onStartOfTurn() {
		for (AbstractDoll doll : this.dolls)
			doll.onStartOfTurn();
		
		AliceSpireKit.addToBot(new AnonymousAction(() -> {
			this.isDamageTargetLocked = false;
		}));
	}
	
	// Deprecated
	public void postEnergyRecharge() {
		for (AbstractDoll doll : this.dolls)
			doll.postEnergyRecharge();
	}
	
	public void onEndOfTurnLockDamageTarget() {
		this.isDamageTargetLocked = true;
	}
	
	// The trigger is implemented in the doll mechanics patch
	public void onEndOfTurn() {
		for (AbstractDoll doll : this.dolls)
			doll.onEndOfTurn();
		
		AliceSpireKit.commitBuffer();
		
//		AliceSpireKit.addToBot(new AnonymousAction(this::onEndOfTurnLockDamageTarget));
	}
	
	public void applyPowers() {
		this.updateTotalHouraiPassiveAmount();
		for (AbstractDoll doll : this.dolls)
			doll.applyPower();
		
		this.updatePreservedBlock();
	}
	
	public void update() {
		this.updateDamageAboutToTake();
		
		for (AbstractDoll doll : this.dolls)
			doll.update();
	}
	
	public void render(SpriteBatch sb) {
		if (!this.shown)
			return;
		
//		AliceSpireKit.log(this.getClass(), "DollManager.render");
		for (AbstractDoll doll : this.dolls)
			doll.render(sb);
	}
	
	public void updateHealthBar() {
		for (AbstractDoll doll : this.dolls)
			doll.updateHealthBar();
	}
	
	public void spawnDoll(AbstractDoll doll, int index) {
		this.activateInternal();
		
		if (index == -1) {
			for (int i = 0; i < MAX_DOLL_SLOTS; i++)
				if (this.dolls.get(i) instanceof EmptyDollSlot) {
					index = i;
					break;
				}
		}
		
		AliceSpireKit.log(this.getClass(), "index = " + index);
		
		boolean artfulChanter = false;
		
		if (index == -1) {
			AliceSpireKit.addActionToBuffer(new RecycleDollAction(this.dolls.get(MAX_DOLL_SLOTS - 1)));
			AliceSpireKit.addActionToBuffer(new AnonymousAction(() -> {
				AbstractDoll left = this.dolls.get(MAX_DOLL_SLOTS - 1);
				if (!(left instanceof EmptyDollSlot))
					AliceSpireKit.addActionToBuffer(new RecycleDollAction(left));
				
				AliceSpireKit.addActionToBuffer(new MoveDollAction(this.dolls.get(MAX_DOLL_SLOTS - 1), 0));
				AliceSpireKit.commitBuffer();
			}));
//			for (int i = MAX_DOLL_SLOTS - 1; i > 0; i--)
//				this.dolls.set(i, this.dolls.get(i - 1));
			index = 0;
		}
		else if (!(this.dolls.get(index) instanceof EmptyDollSlot)) {
			AliceSpireKit.addActionToBuffer(new RecycleDollAction(this.dolls.get(index), doll));
			artfulChanter = true;
		}
		
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		
		AliceSpireKit.addActionToBuffer(new SpawnDollInternalAction(doll, index));
		if (artfulChanter)
			AliceSpireKit.addActionToBuffer(new AnonymousAction(() -> {
				this.triggerArtfulChanter(doll);
			}));
		AliceSpireKit.commitBuffer();
	}
	
	public void spawnDollInternal(AbstractDoll doll, int index) {
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		assert this.dolls.get(index) == doll || this.dolls.get(index) instanceof EmptyDollSlot;
		
		boolean cancelled = false;
		for (AbstractDoll other : this.dolls)
			if (other.preOtherDollSpawn(doll))
				cancelled = true;
		
		if (cancelled)
			return;
		
		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook)
				((OnDollOperateHook) relic).preSpawnDoll(doll);
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).preSpawnDoll(doll);
		
		int multiplier = this.getTotalHouraiPassiveAmount();
		doll.maxHP += multiplier * doll.getBaseHP();
		doll.HP += multiplier * doll.getBaseHP();
		
		doll.showHealthBar();
		
		this.dolls.set(index, doll);
		doll.applyPower();
		
		doll.postSpawn();
		
		this.applyPowers();
		
		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook)
				((OnDollOperateHook) relic).postSpawnDoll(doll);
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).postSpawnDoll(doll);
		
		for (AbstractDoll other : this.dolls)
			if (other != doll)
				other.postOtherDollSpawn(doll);
		
		AliceSpireKit.commitBuffer();
		
		this.update();
	}
	
	public void dollAct(AbstractDoll doll, boolean isSpecial) {
		assert this.dolls.contains(doll);
		
		doll.applyPower();
		doll.onAct();
		
		this.applyPowers();
		
		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook &&
					(!isSpecial || ((OnDollOperateHook) relic).canWorkOnSpecialAct()))
				((OnDollOperateHook) relic).postDollAct(doll);
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook &&
					(!isSpecial || ((OnDollOperateHook) power).canWorkOnSpecialAct()))
				((OnDollOperateHook) power).postDollAct(doll);
		
		for (AbstractDoll other : this.dolls)
			if (other != doll)
				other.postOtherDollAct(doll);
		
		for (AbstractCard card : this.owner.drawPile.group)
			if (card instanceof DollMagic)
				((DollMagic) card).postDollAct();
		for (AbstractCard card : this.owner.discardPile.group)
			if (card instanceof DollMagic)
				((DollMagic) card).postDollAct();
		
		AliceSpireKit.commitBuffer();
		AliceSpireKit.logger.info("Commited buffer here!");
		
		this.update();
	}
	
//	public void recycleDoll(AbstractDoll doll) {
//		this.recycleDollInternal(doll, null);
//	}
	
	public void recycleDoll(AbstractDoll doll, AbstractDoll newDoll) {
		this.recycleDollInternal(doll, newDoll);
	}
	
	private void recycleDollInternal(AbstractDoll doll, AbstractDoll newDoll) {
		assert this.dolls.contains(doll);
		
		doll.applyPower();
		doll.onRecycle();
		
		this.dolls.set(this.dolls.indexOf(doll),
				newDoll != null ? newDoll : new EmptyDollSlot());
		
		this.applyPowers();
		
		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook) {
				((OnDollOperateHook) relic).postRecycleDoll(doll);
				((OnDollOperateHook) relic).postRecycleOrDestroyDoll(doll);
			}
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook) {
				((OnDollOperateHook) power).postRecycleDoll(doll);
				((OnDollOperateHook) power).postRecycleOrDestroyDoll(doll);
			}
		
		for (AbstractDoll other : this.dolls)
			if (other != doll) {
				other.postOtherDollRecycled(doll);
			}
		
		AliceSpireKit.commitBuffer();
		
		this.update();
	}
	
	public void destroyDoll(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		doll.applyPower();
		doll.onDestroyed();
		
		this.dolls.set(this.dolls.indexOf(doll), new EmptyDollSlot());
		
		this.applyPowers();
		
		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook) {
				((OnDollOperateHook) relic).postDestroyDoll(doll);
				((OnDollOperateHook) relic).postRecycleOrDestroyDoll(doll);
			}
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook) {
				((OnDollOperateHook) power).postDestroyDoll(doll);
				((OnDollOperateHook) power).postRecycleOrDestroyDoll(doll);
			}
		
		for (AbstractDoll other : this.dolls)
			if (other != doll) {
				other.postOtherDollDestroyed(doll);
			}
		
		AliceSpireKit.commitBuffer();
		
		this.update();
	}
	
	public void removeDoll(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		doll.applyPower();
		doll.onRemoved();
		
		this.dolls.set(this.dolls.indexOf(doll), new EmptyDollSlot());
		this.applyPowers();
		
		AliceSpireKit.commitBuffer();
		this.update();
	}
	
	public void moveDoll(AbstractDoll doll, int index) {
		assert this.dolls.contains(doll);
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		
		doll.applyPower();
		
		int cur = this.dolls.indexOf(doll);
		int p = index;
		if (cur < index) {
//			p--;
			while (p > cur) {
				if (this.dolls.get(p) instanceof EmptyDollSlot)
					break;
				p--;
			}
				
			for (int i = p; i < index; i++)
				this.dolls.set(i, this.dolls.get(i + 1));
			this.dolls.set(index, doll);
		}
		else if (cur > index) {
//			p++;
			while (p < cur) {
				if (this.dolls.get(p) instanceof EmptyDollSlot)
					break;
				p++;
			}
			
			for (int i = p; i > index; i--)
				this.dolls.set(i, this.dolls.get(i - 1));
			this.dolls.set(index, doll);
		}
		
		if (p != cur)
			this.dolls.set(cur, new EmptyDollSlot());
		
		this.applyPowers();
		
		this.update();
	}
	
	public boolean dollTakesDamage(AbstractDoll doll, int amount) {
		assert this.dolls.contains(doll);
		
		if (doll.takeDamage(amount)) {
			this.destroyDoll(doll);
			return true;
		}
		
		return false;
	}
	
	public ArrayList<AbstractDoll> getDolls() {
		return this.dolls;
	}
	
	public boolean contains(AbstractDoll doll) {
		return this.dolls.contains(doll);
	}
	
	public int getIndex(AbstractDoll doll) {
		return this.dolls.indexOf(doll);
	}
	
	public boolean hasDoll() {
		return this.dolls.stream().anyMatch(doll -> !(doll instanceof EmptyDollSlot));
	}
	
	public boolean hasEmptySlot() {
		return this.dolls.stream().anyMatch(doll -> doll instanceof EmptyDollSlot);
	}
	
	public AbstractDoll getHoveredDoll() {
		for (AbstractDoll doll : this.dolls) {
//			doll.hb.update();
			if (doll.hb.hovered)
				return doll;
		}
		
		return null;
	}
	
	private void updateTotalHouraiPassiveAmount() {
		this.totalHouraiPassiveAmount = this.dolls.stream()
				.filter(doll -> doll instanceof HouraiDoll)
				.mapToInt(doll -> doll.passiveAmount)
				.sum();
	}
	
	public int getTotalHouraiPassiveAmount() {
		return this.totalHouraiPassiveAmount;
	}
	
	public int getDollTypeCount() {
		HashSet<String> types = new HashSet<>();
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot))
				types.add(doll.getID());
		return types.size();
	}
	
	public void triggerArtfulChanter(AbstractDoll doll) {
		if (this.owner.hasPower(ArtfulChanterPower.POWER_ID)) {
			ArtfulChanterPower power = (ArtfulChanterPower) AbstractDungeon.player.getPower(ArtfulChanterPower.POWER_ID);
			power.flash();
			
			for (int i = 0; i < power.amount; i++)
				AliceSpireKit.addToTop(new DollActAction(doll));
		}
	}
	
//	public Formation getFormation() {
//		return this.formation;
//	}
//
//	public enum Formation {
//		NORMAL, ATTACK, DEFENSIVE
//	}
	
	public Vector2 calcDollPosition(int index) {
		float dist = 260.0F * Settings.scale;
		float degree = 90 + 40 * (index - 3); // 0 on the right
		
		float x = this.owner.drawX + dist * MathUtils.cosDeg(degree);
		float y = this.owner.drawY + this.owner.hb.height * 0.6F + dist * MathUtils.sinDeg(degree);
		return new Vector2(x, y);
	}
}
