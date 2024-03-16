package rs.antileaf.alice.doll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.blue.Recycle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.action.doll.MoveDollAction;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.action.doll.SpawnDollInternalAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.characters.AliceMargatroid;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.HouraiDoll;
import rs.antileaf.alice.doll.dolls.KyotoDoll;
import rs.antileaf.alice.powers.interfaces.OnDollOperatePower;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;
import java.util.HashMap;

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
	
//	private Formation formation;
	
	private int preservedBlock = 0;
	
	private boolean shown = false;
	
	public DollManager(AbstractPlayer p) {
		AliceSpireKit.log(this.getClass(), "DollManager constructor");
		
		this.owner = p;
		this.dolls = new ArrayList<>();
	}
	
	public void initPreBattle() {
		this.shown = (this.owner instanceof AliceMargatroid);
		
		this.dolls.clear();
		for (int i = 0; i < MAX_DOLL_SLOTS; i++)
			this.dolls.add(new EmptyDollSlot());
		
//		this.formation = Formation.NORMAL;
		
		AliceSpireKit.log(this.getClass(), "DollManager.initPreBattle done");
		this.debug();
	}
	
	public void debug() {
		AliceSpireKit.log(this.getClass(), "Dolls: " +
				this.dolls.stream().map(doll -> doll.getClass().getSimpleName())
						.reduce((a, b) -> a + ", " + b).orElse("None"));
	}
	
	public void clearPostBattle() {
		this.dolls.clear();
		this.shown = false;
	}
	
	private void activateInternal() {
		this.shown = true;
	}
	
	public void addBlock(AbstractDoll doll, int block) {
		doll.addBlock(block);
	}
	
	public void updatePreservedBlock() {
		this.preservedBlock = 0;
		for (AbstractDoll doll : this.dolls)
			if (doll instanceof KyotoDoll) {
				doll.applyPower();
				this.preservedBlock += doll.passiveAmount;
			}
	}
	
	public int getPreservedBlock() {
		return this.preservedBlock;
	}
	
	public void clearBlock() {
		for (AbstractDoll doll : this.dolls)
			doll.clearBlock(this.preservedBlock);
	}
	
	public void onStartOfTurn() {
		for (AbstractDoll doll : this.dolls)
			doll.onStartOfTurn();
	}
	
	public void onEndOfTurn() {
		for (AbstractDoll doll : this.dolls)
			doll.onEndOfTurn();
	}
	
	public void applyPowers() {
		for (AbstractDoll doll : this.dolls)
			doll.applyPower();
	}
	
	public void update() {
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
		
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		
		AliceSpireKit.addActionToBuffer(new SpawnDollInternalAction(doll, index));
		AliceSpireKit.commitBuffer();
	}
	
	public void spawnDollInternal(AbstractDoll doll, int index) {
		assert index >= 0 && index < MAX_DOLL_SLOTS && this.dolls.get(index) instanceof EmptyDollSlot;
		
		doll.showHealthBar();
		
		this.dolls.set(index, doll);
		doll.applyPower();
		doll.postSpawn();
		
		this.applyPowers();
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperatePower)
				((OnDollOperatePower) power).onSpawnDoll(doll);
		
		for (AbstractDoll other : this.dolls)
			if (other != doll)
				other.postOtherDollSpawn(doll);
		
		this.update();
	}
	
	public void dollAct(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		if (doll instanceof HouraiDoll) {
			for (int i = 0; i < this.dolls.size(); i++)
				if (!(this.dolls.get(i) instanceof HouraiDoll)) {
					doll = this.dolls.get(i);
					break;
				}
		}
		
		doll.applyPower();
		
		if (!(doll instanceof HouraiDoll))
			doll.onAct();
		else
			AliceSpireKit.log(this.getClass(), "There is no doll to act.");
		
		this.applyPowers();
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperatePower)
				((OnDollOperatePower) power).onDollAct(doll);
		
		for (AbstractDoll other : this.dolls)
			if (other != doll)
				other.postOtherDollAct(doll);
		
		this.update();
	}
	
	public void recycleDoll(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		doll.applyPower();
		
		doll.onRecycle();
		doll.onRemoved();
		this.dolls.set(this.dolls.indexOf(doll), new EmptyDollSlot());
		
		this.applyPowers();
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperatePower)
				((OnDollOperatePower) power).onRecycleDoll(doll);
		
		for (AbstractDoll other : this.dolls)
			if (other != doll) {
				other.postOtherDollRecycle(doll);
				other.postOtherDollRemoved(doll);
			}
		
		this.update();
	}
	
	public void destroyDoll(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		doll.applyPower();
		
		doll.onDestroyed();
		doll.onRemoved();
		this.dolls.set(this.dolls.indexOf(doll), new EmptyDollSlot());
		
		this.applyPowers();
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperatePower)
				((OnDollOperatePower) power).onDestroyDoll(doll);
		
		for (AbstractDoll other : this.dolls)
			if (other != doll) {
				other.postOtherDollDestroyed(doll);
				other.postOtherDollRemoved(doll);
			}
		
		this.update();
	}
	
	public void moveDoll(AbstractDoll doll, int index) {
		assert this.dolls.contains(doll);
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		
		doll.applyPower();
		
		int cur = this.dolls.indexOf(doll);
		int p = index;
		if (cur < index) {
			p--;
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
			p++;
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
	
	public int getHouraiDollCount() {
		return (int) this.dolls.stream().filter(doll -> doll instanceof HouraiDoll).count();
	}
	
//	public Formation getFormation() {
//		return this.formation;
//	}
//
//	public enum Formation {
//		NORMAL, ATTACK, DEFENSIVE
//	}
	
	public Vector2 calcDollPosition(int index) {
		float dist = 225.0F * Settings.scale;
		float degree = 90 + 40 * (index - 3); // 0 on the right
		
		float x = this.owner.drawX + dist * MathUtils.cosDeg(degree);
		float y = this.owner.drawY + this.owner.hb.height / 2.0F + dist * MathUtils.sinDeg(degree);
		return new Vector2(x, y);
	}
}
