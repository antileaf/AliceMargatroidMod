package rs.antileaf.alice.doll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.dolls.HouraiDoll;
import rs.antileaf.alice.interfaces.OnDollOperateHook;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;
import java.util.HashMap;

public class DollManager {
	public static final int MAX_DOLL_SLOTS = 7;
	
	private static final HashMap<AbstractPlayer, DollManager> instances = new HashMap<>();
	
	public static DollManager getInstance(AbstractPlayer p) {
		if (!instances.containsKey(p))
			instances.put(p, new DollManager(p));
		return instances.get(p);
	}
	
	public static void clearInstances() {
		instances.clear();
	}
	
	public static final String SIMPLE_NAME = DollManager.class.getSimpleName();
	public static final String ID = AliceSpireKit.generateID(DollManager.SIMPLE_NAME);
	private static final UIStrings uiStrings = AliceSpireKit.getUIString(DollManager.ID);
	public static final String[] TEXT = uiStrings.TEXT;
	
	private final AbstractPlayer owner;
	private final ArrayList<AbstractDoll> dolls;
	
	private Formation formation;
	
	public DollManager(AbstractPlayer p) {
		this.owner = p;
		this.dolls = new ArrayList<>();
	}
	
	public void initPreBattle() {
		this.dolls.clear();
		for (int i = 0; i < MAX_DOLL_SLOTS; i++)
			this.dolls.add(new EmptyDollSlot());
		
		this.formation = Formation.NORMAL;
	}
	
	public void clearPostBattle() {
		this.dolls.clear();
	}
	
	public void clearBlock() {
		int preserve = 0;
		// TODO: calculate preserve
		
		for (AbstractDoll doll : this.dolls)
			doll.clearBlock(preserve);
	}
	
	public void onStartOfTurn() {
		for (AbstractDoll doll : this.dolls)
			doll.onStartOfTurn();
	}
	
	public void onEndOfTurn() {
		for (AbstractDoll doll : this.dolls)
			doll.onEndOfTurn();
	}
	
	public void update() {
		// TODO
	}
	
	public void render(SpriteBatch sb) {
		for (AbstractDoll doll : this.dolls)
			doll.render(sb);
	}
	
	public void spawnDoll(AbstractDoll doll, int index) {
		if (index == -1) {
			for (int i = 0; i < MAX_DOLL_SLOTS; i++)
				if (this.dolls.get(i) instanceof EmptyDollSlot) {
					index = i;
					break;
				}
			
			if (index == -1) {
				this.recycleDoll(this.dolls.get(0));
				for (int i = 0; i < MAX_DOLL_SLOTS - 1; i++)
					this.dolls.set(i, this.dolls.get(i + 1));
				index = MAX_DOLL_SLOTS - 1;
			}
		}
		
		assert index >= 0 && index < MAX_DOLL_SLOTS;
		if (!(this.dolls.get(index) instanceof EmptyDollSlot))
			this.recycleDoll(this.dolls.get(index));
		
		this.spawnDollInternal(doll, index);
	}
	
	private void spawnDollInternal(AbstractDoll doll, int index) {
		assert index >= 0 && index < MAX_DOLL_SLOTS && this.dolls.get(index) instanceof EmptyDollSlot;
		
		this.dolls.set(index, doll);
		doll.postSpawn();
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).onSpawnDoll(doll);
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
		
		if (!(doll instanceof HouraiDoll))
			doll.onAct();
		else
			AliceSpireKit.log(this.getClass(), "There is no doll to act.");
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).onDollAct(doll);
	}
	
	public void recycleDoll(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		doll.onRecycle();
		this.dolls.set(this.dolls.indexOf(doll), new EmptyDollSlot());
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).onRecycleDoll(doll);
	}
	
	public void destroyDoll(AbstractDoll doll) {
		assert this.dolls.contains(doll);
		
		doll.onDestroyed();
		this.dolls.set(this.dolls.indexOf(doll), new EmptyDollSlot());
		
		for (AbstractPower power : this.owner.powers)
			if (power instanceof OnDollOperateHook)
				((OnDollOperateHook) power).onDestroyDoll(doll);
	}
	
	public ArrayList<AbstractDoll> getDolls() {
		return this.dolls;
	}
	
	public boolean contains(AbstractDoll doll) {
		return this.dolls.contains(doll);
	}
	
	public int getHouraiDollCount() {
		return (int) this.dolls.stream().filter(doll -> doll instanceof HouraiDoll).count();
	}
	
	public Formation getFormation() {
		return this.formation;
	}
	
	public enum Formation {
		NORMAL, ATTACK, DEFENSIVE
	}
	
	public Vector2 calcDollPosition(int index) {
		// TODO: Calculate differently for different formations
		float dist = 210.0F * Settings.scale;
		float degree = 90 + 15 * (index - MAX_DOLL_SLOTS / 2.0F); // 0 on the right
		
		float x = this.owner.drawX + dist * MathUtils.cosDeg(degree);
		float y = this.owner.drawY + this.owner.hb.height / 2.0F + dist * MathUtils.sinDeg(degree);
		return new Vector2(x, y);
	}
}
