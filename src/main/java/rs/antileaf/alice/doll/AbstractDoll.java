package rs.antileaf.alice.doll;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.powers.interfaces.PlayerDollAmountModPower;
import rs.antileaf.alice.utils.AliceSpireKit;

public abstract class AbstractDoll extends CustomOrb {
	public int actAmount = 0;
	protected int baseActAmount = 0;
	
	protected String actDescription;
	
	private int maxHP;
	private int HP;
	private int block; // TODO: Implement block.
	
	protected DollAmountType passiveAmountType = DollAmountType.MAGIC;
	protected DollAmountType actAmountType = DollAmountType.MAGIC;
	
	private int damageAboutToTake = 0;
	private float vfxTimer = 1.0F;
	private float animX, animY;
	private float animSpeed;
	
	protected boolean highlightPassiveValue = false;
	protected float passiveFontScale = 1.0f;
	
	protected boolean highlightActValue = false;
	protected float actFontScale = 1.0f;
	
	protected RenderTextMode renderTextMode;
	
	public AbstractDoll(String ID, String name, int maxHP, int basePassiveAmount, int baseActAmount, String imgPath, RenderTextMode renderTextMode) {
		super(ID, name, basePassiveAmount, -1, "", "", imgPath);
		
		this.baseActAmount = this.actAmount = baseActAmount;
		this.renderTextMode = renderTextMode;
		
		this.maxHP = this.HP = maxHP;
		this.block = 0;
		
		this.updateDescription();
		this.initPosition(AbstractDungeon.player.animX, AbstractDungeon.player.animY);
	}
	
	protected void initPosition(float cx, float cy) {
		this.cX = this.tX = this.animX = cx;
		this.cY = this.tY = this.animY = cy;
		
		this.hb.move(this.cX, this.cY);
		this.angle = 0.0F; // TODO: Randomize?
	}
	
	public void transAnim(Vector2 target) {
		this.animX = target.x;
		this.animY = target.y;
		this.animSpeed = 6.0F;
	}
	
	public void transAnim(float x, float y) {
		this.transAnim(new Vector2(x, y));
	}
	
	@Override
	public void update() {
		if (DollManager.get().contains(this)) {
			Vector2 pos = DollManager.get().calcDollPosition(DollManager.get().getDolls().indexOf(this));
			if (pos != null)
				this.transAnim(pos);
		}
		
		this.hb.move(this.tX, this.tY);
		this.hb.update();
		if (this.hb.hovered) {
			TipHelper.renderGenericTip(
					this.tX + 96.0F * Settings.scale,
					this.tY + 64.0F * Settings.scale,
					this.name,
					this.description
			);
		}
		this.bobEffect.update();
		
		AbstractPlayer player = AbstractDungeon.player;
		this.cX = MathHelper.orbLerpSnap(this.cX, player.animX + this.tX);
		this.cY = MathHelper.orbLerpSnap(this.cY, player.animY + this.tY); // Ignore the warning.
		
		if (this.channelAnimTimer != 0.0F) {
			this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
			if (this.channelAnimTimer < 0.0F)
				this.channelAnimTimer = 0.0F;
		}
		
		this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
		this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
		this.angle += Gdx.graphics.getDeltaTime() * 45.0F;
		this.vfxTimer -= Gdx.graphics.getDeltaTime();
		if (this.vfxTimer < 0.0F) {
			this.vfxTimer = 0.0F; // TODO
		}
		
		this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
		this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
	}
	
	private void updateSelfAnimation() { // TODO: What is this?
		if (this.animX != STOP_ANIM) {
			this.tX = MathUtils.lerp(this.tX, this.animX, Gdx.graphics.getDeltaTime() * this.animSpeed);
			if (Math.abs(this.tX - this.animX) <= Settings.UI_SNAP_THRESHOLD) {
				this.tX = this.animX;
				this.animX = STOP_ANIM;
			}
		}
		if (this.animY != STOP_ANIM) {
			this.tY = MathUtils.lerp(this.tY, this.animY, Gdx.graphics.getDeltaTime() * this.animSpeed);
			if (Math.abs(this.tY - this.animY) <= Settings.UI_SNAP_THRESHOLD) {
				this.tY = this.animY;
				this.animY = STOP_ANIM;
			}
		}
	}
	
	public void updateDamageAboutToTake() {
		int index = DollManager.get().getDolls().indexOf(this);
		AbstractMonster monster = AliceSpireKit.getMonsterByIndex(index);
		
		if (monster == null)
			this.damageAboutToTake = 0;
		else
			this.damageAboutToTake = monster.getIntentDmg();
	}
	
	// Returns remaining damage.
	public int onPlayerDamaged(int amount) {
		return Integer.max(0, amount - (this.HP + this.block));
	}
	
	public boolean takeDamage(int amount) {
		int blockLoss = Math.min(this.block, amount);
		this.block -= blockLoss;
		amount -= blockLoss;
		
		this.HP -= amount;
		if (this.HP <= 0) {
			this.HP = 0;
			return true;
		}
		
		return false;
	}
	
	@Override
	public final void onEvoke() {
		assert false : "AbstractDoll.onEvoke() should not be called!";
	}
	
	public abstract void onAct();
	
	public void postSpawn() {}
	
	public void onRecycle() {}
	
	public void onDestroyed() {}
	
	@Override
	public void onStartOfTurn() {}
	
	@Override
	public void onEndOfTurn() {}
	
	public void applyPower() {
		AbstractPlayer player = AbstractDungeon.player;
		
		this.passiveAmount = this.basePassiveAmount;
		this.actAmount = this.baseActAmount;
		
		int hourai = DollManager.get().getHouraiDollCount();
		if (hourai > 0) {
			if (this.passiveAmountType != DollAmountType.MAGIC)
				this.passiveAmount += hourai;
			if (this.actAmountType != DollAmountType.MAGIC)
				this.actAmount += hourai;
		}
		
		if (this instanceof ShanghaiDoll) {
			if (player.hasPower("Strength")) {
				int bonus = Integer.max(player.getPower("Strength").amount, 0);
				
				if (this.passiveAmountType != DollAmountType.MAGIC)
					this.passiveAmount += bonus;
				if (this.actAmountType != DollAmountType.MAGIC)
					this.actAmount += bonus;
			}
		}
		
		float tempPassiveAmount = this.passiveAmount, tempActAmount = this.actAmount;
		
		for (AbstractPower p : player.powers)
			if (p instanceof PlayerDollAmountModPower) {
				tempPassiveAmount = ((PlayerDollAmountModPower) p).modifyDollAmount(
						tempPassiveAmount, this.getClass(), this.passiveAmountType, DollAmountTime.PASSIVE);
				
				tempActAmount = ((PlayerDollAmountModPower) p).modifyDollAmount(
						tempActAmount, this.getClass(), this.actAmountType, DollAmountTime.ACT);
			}
		
		// TODO: Powers / Relics
		
		this.passiveAmount = (int) tempPassiveAmount;
		this.actAmount = (int) tempActAmount;
		
		this.updateDescription();
	}
	
	@Override
	public void updateDescription() {
		this.updateDescriptionImpl();
		this.description = "#y被动 ：" + this.passiveDescription + " NL #y行动 ：" + this.actDescription;
	}
	
	public abstract void updateDescriptionImpl();
	
	@Override
	public final void applyFocus() { // Dolls should not be affected by focus.
		assert false : "AbstractDoll.applyFocus() should not be called!";
	}
	
	public void receiveBlock(int block) {
		this.block += block;
	}
	
	public void clearBlock(int preserve) {
		this.block = Math.min(this.block, preserve);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		this.updateSelfAnimation();
		
		super.render(sb);
		// TODO: render hp and block
		// TODO: render damageAboutToTake
		
		this.renderText(sb);
	}
	
	private Color getFontColor(boolean highlight) {
		return highlight ? new Color(0.2F, 1.0F, 1.0F, this.c.a) : this.c;
	}
	
	private void renderPassiveValue(SpriteBatch sb, float x, float y) {
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				Integer.toString(this.passiveAmount),
				x,
				y,
				this.getFontColor(this.highlightPassiveValue),
				this.passiveFontScale);
	}
	
	private void renderActValue(SpriteBatch sb, float x, float y) {
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				Integer.toString(this.actAmount),
				x,
				y,
				this.getFontColor(this.highlightActValue),
				this.actFontScale);
	}
	
	@Override
	public void renderText(SpriteBatch sb) {
		assert this.renderTextMode != null: "AbstractDoll.renderTextMode should not be null!";
		
		if (this.renderTextMode == RenderTextMode.PASSIVE) {
			this.renderPassiveValue(sb,
					this.cX + NUM_X_OFFSET,
					this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET);
		}
		else if (this.renderTextMode == RenderTextMode.ACT) {
			this.renderActValue(sb,
					this.cX + NUM_X_OFFSET,
					this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET);
		}
		else if (this.renderTextMode == RenderTextMode.BOTH) {
			this.renderPassiveValue(sb,
					this.cX + NUM_X_OFFSET,
					this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale);
			
			this.renderActValue(sb,
					this.cX + NUM_X_OFFSET,
					this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale);
		}
	}
	
	@Override
	public final void triggerEvokeAnimation() {
		assert false : "AbstractDoll.triggerEvokeAnimation() should not be called!";
	}
	
	@Override
	public final void showEvokeValue() {
		assert false : "AbstractDoll.showEvokeValue() should not be called!";
	}
	
	@Override
	public final void hideEvokeValues() {
		assert false : "AbstractDoll.hideEvokeValues() should not be called!";
	}
	
	public abstract void triggerActAnimation();
	
	public void startHighlightPassiveValue() {
		this.highlightPassiveValue = true;
		this.passiveFontScale = 1.5f;
	}
	
	public void stopHighlightPassiveValue() {
		this.highlightPassiveValue = false;
	}
	
	public void startHighlightActValue() {
		this.highlightActValue = true;
		this.actFontScale = 1.5f;
	}
	
	public void stopHighlightActValue() {
		this.highlightActValue = false;
	}
	
	public void renderReticle(SpriteBatch sb) {
		// TODO
	}
	
	protected String coloredPassiveAmount() {
		return AliceSpireKit.coloredNumber(this.passiveAmount, this.basePassiveAmount);
	}
	
	protected String coloredActAmount() {
		return AliceSpireKit.coloredNumber(this.actAmount, this.baseActAmount);
	}
	
	protected enum RenderTextMode {
		NONE, PASSIVE, ACT, BOTH
	}
	
	protected final float STOP_ANIM = -9999.0F;
	
	public void addToTop(AbstractGameAction action) {
		AliceSpireKit.addToTop(action);
	}
	
	public void addActionsToTop(AbstractGameAction... actions) {
		AliceSpireKit.addActionsToTop(actions);
	}
	
	public void addToBot(AbstractGameAction action) {
		AliceSpireKit.addToBot(action);
	}
	
	public static AbstractDoll getRandomDoll() {
		// TODO
		return new ShanghaiDoll();
	}
}
