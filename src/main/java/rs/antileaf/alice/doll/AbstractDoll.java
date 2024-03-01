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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.utils.AliceSpireKit;

public abstract class AbstractDoll extends CustomOrb {
	public int actAmount = 0;
	protected int baseActAmount = 0;
	
	String actDescription;
	
	private int maxHP;
	private int HP;
	private int block; // TODO: Implement block.
	
	private int damageAboutToTake;
	private float vfxTimer = 1.0F;
	private float animX, animY;
	private float animSpeed;
	
	protected boolean highlightPassiveValue = false;
	protected float passiveFontScale = 1.0f;
	
	protected boolean highlightActValue = false;
	protected float actFontScale = 1.0f;
	
	protected RenderTextMode renderTextMode;
	
	public AbstractDoll(String ID, String name, int maxHP, int basePassiveAmount, int baseActAmount,
	                    String passiveDescription, String actDescription, String imgPath) {
		super(ID, name, basePassiveAmount, -1, passiveDescription, "", imgPath);
		
		this.baseActAmount = this.actAmount = baseActAmount;
		this.actDescription = actDescription;
		
		this.maxHP = this.HP = maxHP;
		this.block = 0;
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
		this.hb.move(this.tX, this.tY);
		this.hb.update();
		if (this.hb.hovered) {
			// TODO
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
			// TODO: Add vfx.
		}
		
		
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
		int index = DollManager.getInstance(AbstractDungeon.player).getDolls().indexOf(this);
		
		AbstractMonster monster = null;
		int cnt = 0;
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (!m.isDeadOrEscaped())
				if (cnt++ == index) {
					monster = m;
					break;
				}
		
		if (monster == null)
			this.damageAboutToTake = 0;
		else
			this.damageAboutToTake = monster.getIntentDmg();
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
		DollManager dm = DollManager.getInstance(player);
		
		this.passiveAmount = this.basePassiveAmount;
		this.actAmount = this.baseActAmount;
		
		int hourai = dm.getHouraiDollCount();
		if (hourai > 0) {
			this.passiveAmount += hourai;
			this.actAmount += hourai;
		}
		
		// TODO: Powers / Relics
		
		this.updateDescription();
	}
	
	@Override
	public final void applyFocus() { // Dolls should not be affected by focus.
		assert false : "AbstractDoll.applyFocus() should not be called!";
	}
	
	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		// TODO: render hp and block
		// TODO: render damageAboutToTake
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
	
	public enum AmountType {
		DAMAGE, BLOCK, MAGIC
	}
	
	public enum AmountTime {
		PASSIVE, ACT
	}
}
