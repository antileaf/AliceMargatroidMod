package me.antileaf.alice.doll;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.doll.RecycleDollAction;
import me.antileaf.alice.action.doll.SpawnDollInternalAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.cards.alice.SevenColoredPuppeteer;
import me.antileaf.alice.cards.deprecated.DollMagic;
import me.antileaf.alice.characters.AliceMargatroid;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.doll.dolls.FranceDoll;
import me.antileaf.alice.doll.dolls.NetherlandsDoll;
import me.antileaf.alice.doll.interfaces.OnDollOperateHook;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.powers.unique.TauntedPower;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DollManager {
	private static final Logger logger = LogManager.getLogger(DollManager.class.getName());

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
	
	public static class Stats {
		public HashSet<String> placedThisCombat = new HashSet<>();
		public HashMap<String, Integer> actCountThisCombat = new HashMap<>();
		public HashSet<String> recycledOrDestroyedThisCombat = new HashSet<>();
		
		void clear() {
			this.placedThisCombat = new HashSet<>();
			this.actCountThisCombat = new HashMap<>();
			this.recycledOrDestroyedThisCombat = new HashSet<>();
		}
		
		void onDollPlaced(AbstractDoll doll) {
			boolean needToUpdate = !this.placedThisCombat.contains(doll.getID());
			this.placedThisCombat.add(doll.getID());
			
			if (needToUpdate)
				SevenColoredPuppeteer.updateAll();
		}
		
		void onDollAct(AbstractDoll doll) {
			this.actCountThisCombat.put(doll.getID(),
					this.actCountThisCombat.getOrDefault(doll.getID(), 0) + 1);
		}

		void onDollRecycled(AbstractDoll doll) {
			this.recycledOrDestroyedThisCombat.add(doll.getID());
		}

		void onDollDestroyed(AbstractDoll doll) {
			this.recycledOrDestroyedThisCombat.add(doll.getID());
		}
		
		int getMaxActCount() {
			return this.actCountThisCombat.values().stream()
					.max(Integer::compareTo)
					.orElse(0);
		}
	}
	
	private Stats stats = new Stats();
	
	private final AbstractPlayer owner;
	private final ArrayList<AbstractDoll> dolls;
	
	private int preservedBlock = 0;
	
	private boolean shown = false;
//	private int totalHouraiPassiveAmount = 0;
	
	public HashMap<AbstractMonster, Integer> damageTarget = new HashMap<>();
	public boolean isDamageTargetLocked = false;
	
	public DollManager(AbstractPlayer p) {
		logger.info("DollManager constructor");
		
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
		
		this.stats.clear();
		
//		this.formation = Formation.NORMAL;
		
		logger.info("DollManager.initPreBattle done");
//		this.debug();
	}
	
	public void debug() {
		logger.info("DollManager.Dolls: {}",
				this.dolls.stream().map(doll -> doll.getClass().getSimpleName())
						.reduce((a, b) -> a + ", " + b).orElse("None"));
	}
	
	public void clearPostBattle() {
		this.dolls.clear();
		this.shown = false;
		
		this.isDamageTargetLocked = false;
		this.damageTarget.clear();
		
		this.preservedBlock = 0;
//		this.totalHouraiPassiveAmount = 0;
		
		this.stats.clear();
	}
	
	public boolean isShown() {
		return this.shown;
	}
	
	public void activate() {
		this.shown = true;
	}
	
	public Stats getStats() {
		return this.stats;
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
	
	public void heal(AbstractDoll doll, int amount) {
		doll.heal(amount);
	}

	public void increaseMaxHealth(AbstractDoll doll, int amount, boolean isEmpty) {
		doll.increaseMaxHealth(amount, isEmpty);
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
			logger.info("Preserved block is not 0: {}", this.preservedBlock);

		if (AbstractDungeon.player.hasPower(BlurPower.POWER_ID))
			return;
		
		for (AbstractDoll doll : this.dolls) {
			if (!(doll instanceof FranceDoll))
				doll.clearBlock(this.preservedBlock);
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
		
		for (AbstractDoll doll : this.dolls) {
			doll.setDamageAboutToTake(-1, 0);
			doll.setTaunt(false);
		}

		this.damageTarget.clear();

		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (!m.isDeadOrEscaped()) {
				 for (int i = 1; i <= MAX_DOLL_SLOTS; i++)
					 if (m.hasPower(TauntedPower.POWER_ID + "_" + i)) {
						 this.damageTarget.put(m, i - 1);

						 if (dolls.get(i - 1) != null)
							 dolls.get(i - 1).setTaunt(true);
						 
						 break;
					 }
			}

		for (int i = 0; i < MAX_DOLL_SLOTS; i++) {
			AbstractMonster monster = AliceHelper.getMonsterByIndex(i);
			
			if (monster == null || this.damageTarget.containsKey(monster))
				continue;
			
			int index = i;
			if (monster instanceof SpireShield)
				index = MAX_DOLL_SLOTS - 1;
			else if (monster instanceof SpireSpear)
				index = 0;
			
			this.damageTarget.put(monster, index);
		}
		
		if (!AliceHelper.isInBattle()) {
//			logger.warn("DollManager: Not in battle. Maybe you used some debugging tools?");
			return;
		}
		
		for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
			if (monster == null || monster.isDeadOrEscaped())
				continue;
			
			if (!this.damageTarget.containsKey(monster)) {
				logger.warn("Failed to get target index for {}", monster.name);
				continue;
			}
			
			int index = this.damageTarget.get(monster);

//			AliceHelper.logger.info("monster = " + monster.name + ", index = " + index);

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
					logger.warn("updateDamageAboutToTake() Failed to get intentMultiAmt.");
					count = 1;
				}
			}
			
			if (this.dolls.get(index) != null)
				this.dolls.get(index).addDamageAboutToTake(damage, count);
			else
				logger.warn("doll is null");
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
		
		AliceHelper.addToBot(new AnonymousAction(() -> {
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
		
		AliceHelper.commitBuffer();
		
//		AliceSpireKit.addToBot(new AnonymousAction(this::onEndOfTurnLockDamageTarget));
	}
	
	public void applyPowers() {
//		this.updateTotalHouraiPassiveAmount();
		for (AbstractDoll doll : this.dolls)
			doll.applyPower();
		
		this.updatePreservedBlock();
	}
	
	public void update() {
		if (!AliceHelper.isInBattle()) {
			logger.info("DollManager.update: Not in battle. Maybe you used some debugging tools?");
			return;
		}

		this.updateDamageAboutToTake();
		
		for (AbstractDoll doll : this.dolls)
			doll.update();

		if (AbstractDungeon.player.hoveredCard instanceof AbstractAliceCard) {
			AbstractAliceCard ac = (AbstractAliceCard) AbstractDungeon.player.hoveredCard;

			if (ac.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT)
				this.activate();
		}
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
	
	public void spawnDoll(AbstractDoll doll, int index, AbstractGameAction followUpAction) {
		this.activate();

		if (doll instanceof NetherlandsDoll)
			for (AbstractDoll other : this.dolls)
				if (other instanceof NetherlandsDoll) {
					for (int i = 0; i < 2; i++)
						AliceHelper.addToTop(new DollActAction(other));

					AliceHelper.logger.info("There is already a NetherlandsDoll. Triggering its passive effect.");
					return;
				}
		
		if (index == -1) {
			for (int i = 0; i < MAX_DOLL_SLOTS; i++)
				if (this.dolls.get(i) instanceof EmptyDollSlot) {
					index = i;
					break;
				}
		}

		logger.info("index = {}", index);
		
//		boolean artfulChanter = false;
		
		if (index == -1) {
//			AliceHelper.addActionToBuffer(new RecycleDollAction(this.dolls.get(MAX_DOLL_SLOTS - 1)));
//			AliceHelper.addActionToBuffer(new AnonymousAction(() -> {
//				AbstractDoll left = this.dolls.get(MAX_DOLL_SLOTS - 1);
//				if (!(left instanceof EmptyDollSlot))
//					AliceHelper.addActionToBuffer(new RecycleDollAction(left));
//
//				AliceHelper.addActionToBuffer(new MoveDollAction(this.dolls.get(MAX_DOLL_SLOTS - 1), 0));
//				AliceHelper.commitBuffer();
//			}));
//			for (int i = MAX_DOLL_SLOTS - 1; i > 0; i--)
//				this.dolls.set(i, this.dolls.get(i - 1));

			for (int i = 0; i < MAX_DOLL_SLOTS; i++)
				if (this.dolls.get(i) instanceof EmptyDollSlot) {
					index = i;
					break;
				}
		}
		else if (!(this.dolls.get(index) instanceof EmptyDollSlot)) {
			AliceHelper.addActionToBuffer(new RecycleDollAction(this.dolls.get(index), doll));
//			artfulChanter = true;
		}

		if (index == -1) {
			logger.info("DollManager.spawnDoll: No empty slot found. Cannot spawn doll.");
			return;
		}
		
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		
		AliceHelper.addActionToBuffer(new SpawnDollInternalAction(doll, index));

		if (followUpAction != null)
			AliceHelper.addActionToBuffer(followUpAction);

//		if (artfulChanter)
//			AliceHelper.addActionToBuffer(new AnonymousAction(() -> {
//				this.triggerArtfulChanter(doll);
//			}));

		AliceHelper.commitBuffer();
	}
	
	public void spawnDollInternal(AbstractDoll doll, int index) {
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		assert this.dolls.get(index) == doll || this.dolls.get(index) instanceof EmptyDollSlot;
		
//		boolean cancelled = false;
//		for (AbstractDoll other : this.dolls)
//			if (other.preOtherDollSpawn(doll))
//				cancelled = true;
//
//		if (cancelled)
//			return;
		
		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook)
				((OnDollOperateHook) relic).preSpawnDoll(doll);
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).preSpawnDoll(doll);
		
//		int bonusHP = this.getTotalHouraiPassiveAmount();
//		doll.maxHP += bonusHP;
//		doll.HP += bonusHP;
		
		doll.showHealthBar();
		
		this.dolls.set(index, doll);
		doll.applyPower();
		doll.postSpawn();

//		AliceHelper.addActionToBuffer(new DollActAction(doll));
		
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
		
		AliceHelper.commitBuffer();
		
		this.stats.onDollPlaced(doll);
		
		this.update();
	}
	
	public void dollAct(AbstractDoll doll, AbstractDoll.DollActModifier modifier) {
		assert this.dolls.contains(doll);

		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook)
				((OnDollOperateHook) relic).preDollAct(doll);

		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).preDollAct(doll);
		
		doll.applyPower();

//		boolean special = false;
//		if (modifier.theSetup)
//			special = (doll instanceof ShanghaiDoll || doll instanceof KyotoDoll);
//		else if (modifier.dollAmbush)
//			special = doll instanceof FranceDoll;

		if (modifier == null)
			logger.warn("modifier == null. Although this will not cause any problem, you should check the code.");

		doll.onAct(modifier != null ? modifier :
				new AbstractDoll.DollActModifier());
		
		this.applyPowers();
		
		for (AbstractRelic relic : this.owner.relics)
			if (relic instanceof OnDollOperateHook)
				((OnDollOperateHook) relic).postDollAct(doll);
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
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
		
		AliceHelper.commitBuffer();
//		logger.info("DollManager.dollAct(): Commited buffer here!");
		
		this.stats.onDollAct(doll);
		
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
		
		AliceHelper.commitBuffer();

		this.stats.onDollRecycled(doll);
		
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
		
		AliceHelper.commitBuffer();

		this.stats.onDollDestroyed(doll);
		
		this.update();
	}
	
	public void removeDoll(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		doll.applyPower();
		doll.onRemoved();
		
		this.dolls.set(this.dolls.indexOf(doll), new EmptyDollSlot());
		this.applyPowers();
		
		AliceHelper.commitBuffer();
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

	public AbstractMonster[] getCorrespondingEnemies(AbstractDoll slot) {
		return AbstractDungeon.getMonsters().monsters.stream()
				.filter(m -> !m.isDeadOrEscaped())
				.filter(m -> {
					int i = this.damageTarget.getOrDefault(m, -1);
					return i != -1 && this.dolls.get(i) == slot;
				})
				.toArray(AbstractMonster[]::new);
	}
	
//	private void updateTotalHouraiPassiveAmount() {
//		this.totalHouraiPassiveAmount = this.dolls.stream()
//				.filter(doll -> doll instanceof DEPRECATEDHouraiDoll)
//				.mapToInt(doll -> doll.passiveAmount)
//				.sum();
//	}
	
//	public int getTotalHouraiPassiveAmount() {
//		return this.totalHouraiPassiveAmount;
//	}
	
	public int getDollTypeCount() {
		HashSet<String> types = new HashSet<>();
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot))
				types.add(doll.getID());
		return types.size();
	}

	public int getDollCount() {
		return (int) this.dolls.stream()
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.count();
	}

	@Deprecated
	public void triggerArtfulChanter(AbstractDoll doll) {
//		if (this.owner.hasPower(ArtfulChanterPower.POWER_ID)) {
//			ArtfulChanterPower power = (ArtfulChanterPower) AbstractDungeon.player.getPower(ArtfulChanterPower.POWER_ID);
//			power.flash();
//
//			for (int i = 0; i < power.amount; i++)
//				AliceHelper.addToTop(new DollActAction(doll));
//		}
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
