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
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.BlockedNumberEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import org.jetbrains.annotations.Nullable;
import rs.antileaf.alice.doll.dolls.*;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.powers.interfaces.PlayerDollAmountModPower;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractDoll extends CustomOrb {
	public static String[] CREATURE_TEXT = CardCrawlGame.languagePack.getUIString("AbstractCreature").TEXT;
	private static final float BLOCK_ANIM_TIME = 0.7F;
	private static final float HB_Y_OFFSET_DIST = 6.0F * Settings.scale;
	private static final float BLOCK_OFFSET_DIST = 12.0F * Settings.scale;
	private static final float BLOCK_ICON_X = -14.0F * Settings.scale;
	private static final float BLOCK_ICON_Y = -14.0F * Settings.scale;
	private static final float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
	private static final float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
	private static final float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;
	private static final float HEALTH_BG_OFFSET_X = 31.0F * Settings.scale;
	private static final float FONT_SCALE = 0.7F;
	
	private static final float RETICLE_OFFSET_DIST = 15.0F * Settings.scale;
	
	public int actAmount = 0;
	protected int baseActAmount = 0;
	
	protected String actDescription;
	
	public int maxHP;
	public int HP;
	public int block;
	
	protected DollAmountType passiveAmountType = DollAmountType.MAGIC;
	protected DollAmountType actAmountType = DollAmountType.MAGIC;
	
	private int damageAboutToTake = 0;
	private float vfxTimer = 1.0F;
	private float animX, animY;
	private float animSpeed;
	
	protected float highlightPassiveValueTimer = 0.0F;
	protected float passiveFontScale = FONT_SCALE;
	
	protected float highlightActValueTimer = 0.0F;
	protected float actFontScale = FONT_SCALE;
	
	protected RenderTextMode renderTextMode;
	
	public Hitbox healthHb;
	
	private Color hbBgColor;
	private Color hbShadowColor;
	private Color blockColor;
	private Color blockOutlineColor;
	private Color blockTextColor;
	private Color redHbBarColor;
	private Color blueHbBarColor;
	private Color hbTextColor;
	
	private float hbYOffset;
	private float healthBarWidth;
	private float targetHealthBarWidth;
	private float blockOffset = 0.0F;
	private float blockScale = 1.0F;
	public float hbAlpha = 0.0F;
	
	private float healthHideTimer = 0.0F;
	private float healthBarAnimTimer = 0.0F;
	private float blockAnimTimer = 0.0F;
	private float hbShowTimer = 0.0F;
	
	public float reticleAlpha;
	private Color reticleColor;
	private Color reticleShadowColor;
	public boolean reticleRendered;
	private float reticleOffset;
	private float reticleAnimTimer;
	
	public AbstractDoll(String ID, String name, int maxHP, int basePassiveAmount, int baseActAmount, String imgPath, RenderTextMode renderTextMode) {
		super(ID, name, basePassiveAmount, -1, "", "", imgPath);
		
		this.healthHb = new Hitbox(this.hb.width, 72.0F * Settings.scale);
		this.targetHealthBarWidth = this.hb.width;
		
		this.baseActAmount = this.actAmount = baseActAmount;
		this.renderTextMode = renderTextMode;
		
		this.maxHP = this.HP = maxHP;
		this.block = 0;
		
		this.hbYOffset = HB_Y_OFFSET_DIST * 5.0F;
		this.hbBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
		this.hbShadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
		this.blockColor = new Color(0.6F, 0.93F, 0.98F, 0.0F);
		this.blockOutlineColor = new Color(0.6F, 0.93F, 0.98F, 0.0F);
		this.blockTextColor = new Color(0.9F, 0.9F, 0.9F, 0.0F);
		this.redHbBarColor = new Color(0.8F, 0.05F, 0.05F, 0.0F);
		this.blueHbBarColor = Color.valueOf("31568c00");
		this.hbTextColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
		
		this.reticleAlpha = 0.0F;
		this.reticleColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
		this.reticleShadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
		this.reticleRendered = false;
		this.reticleOffset = 0.0F;
		this.reticleAnimTimer = 0.0F;
		
		this.updateDescription();
		this.initPosition(AbstractDungeon.player.animX, AbstractDungeon.player.animY);
	}
	
	protected void initPosition(float cx, float cy) {
		this.cX = this.tX = this.animX = cx;
		this.cY = this.tY = this.animY = cy;
		
		this.hb.move(this.cX, this.cY);
		this.healthHb.move(this.hb.cX, this.hb.cY - this.hb.height / 2.0F - this.healthHb.height / 2.0F);
		
		this.angle = 0.0F; // TODO: Randomize?
	}
	
	public void transAnim(Vector2 target) {
		this.animX = target.x;
		this.animY = target.y;
		this.animSpeed = 10.0F;
	}
	
	public void transAnim(float x, float y) {
		this.transAnim(new Vector2(x, y));
	}
	
//	public boolean checkIsHoveredWhenHoverCard() {
//		AbstractCard hoveredCard = AbstractDungeon.player.hoveredCard;
//		if (hoveredCard == null)
//			return false;
//
//		if (CardTargetEnum.isDollTarget(hoveredCard.target)) {
//			if ((this instanceof EmptyDollSlot) && hoveredCard.target != CardTargetEnum.DOLL_OR_EMPTY_SLOT)
//				return false;
//
//			return this == DollManager.get().getHoveredDoll();
//		}
//
//		return false;
//	}
	
	public void renderGenericTip() {
		if (this.hb.hovered) {
			TipHelper.renderGenericTip(
					this.tX + 96.0F * Settings.scale,
					this.tY + 64.0F * Settings.scale,
					this.name,
					this.description
			);
		}
	}
	
	@Override
	public void update() {
		if (DollManager.get().contains(this)) {
			Vector2 pos = DollManager.get().calcDollPosition(DollManager.get().getDolls().indexOf(this));
			if (pos != null)
				this.transAnim(pos);
			
			this.updateDamageAboutToTake();
		}
		
		this.hb.move(this.tX, this.tY);
		this.hb.update();
		this.healthHb.move(this.hb.cX, this.hb.cY - this.hb.height / 2.0F - this.healthHb.height / 2.0F);
		this.healthHb.update();
		this.updateReticle();
		if (this.hb.hovered) {
			this.renderGenericTip();
		}
		
		AbstractPlayer player = AbstractDungeon.player;
		this.cX = MathHelper.orbLerpSnap(this.cX, player.animX + this.tX);
		this.cY = MathHelper.orbLerpSnap(this.cY, player.animY + this.tY); // Ignore the warning.
		
		if (this.channelAnimTimer != 0.0F) {
			this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
			if (this.channelAnimTimer < 0.0F)
				this.channelAnimTimer = 0.0F;
		}
//
		this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
		this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
		this.angle += Gdx.graphics.getDeltaTime() * 45.0F;
		this.vfxTimer -= Gdx.graphics.getDeltaTime();
		if (this.vfxTimer < 0.0F) {
			this.vfxTimer = 0.0F; // TODO
		}
//
//		this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
//		this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
		
		this.updateAnimation();
	}
	
	public void updateAnimation() {
		this.bobEffect.update();
		this.cX = MathHelper.orbLerpSnap(this.cX, AbstractDungeon.player.animX + this.tX);
		this.cY = MathHelper.orbLerpSnap(this.cY, AbstractDungeon.player.animY + this.tY);
		
		if (this.highlightPassiveValueTimer > 0.0F) {
			this.highlightPassiveValueTimer -= Gdx.graphics.getDeltaTime();
			if (this.highlightPassiveValueTimer < 0.0F)
				this.highlightPassiveValueTimer = 0.0F;
		}
		if (this.highlightActValueTimer > 0.0F) {
			this.highlightActValueTimer -= Gdx.graphics.getDeltaTime();
			if (this.highlightActValueTimer < 0.0F)
				this.highlightActValueTimer = 0.0F;
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
		else if (monster.intent == AbstractMonster.Intent.ATTACK ||
				monster.intent == AbstractMonster.Intent.ATTACK_BUFF ||
				monster.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
				monster.intent == AbstractMonster.Intent.ATTACK_DEFEND)
			this.damageAboutToTake = monster.getIntentDmg();
		else
			this.damageAboutToTake = 0;
	}
	
	// Returns remaining damage.
	public int onPlayerDamaged(int amount) {
		return Integer.max(0, amount - (this.HP + this.block));
	}
	
	public void addBlock(int blockAmount) {
		boolean showEffect = (this.block == 0);
		
		this.block = Math.min(this.block + blockAmount, 999);
		
		if (showEffect && this.block > 0)
			this.gainBlockAnimation();
		else if (blockAmount > 0) {
			Color col = Settings.GOLD_COLOR.cpy();
			col.a = this.blockTextColor.a;
			this.blockTextColor = col;
			this.blockScale = 5.0F;
		}
		
		this.updateDescription();
	}
	
	public void loseBlock(int amount, boolean noAnimation) {
		boolean showEffect = (this.block > 0);
		
		if (amount > this.block)
			AliceSpireKit.log(AbstractDoll.class, "AbstractDoll.loseBlock() called with amount > block.");
		this.block = Math.max(0, this.block - amount);
		
		//
		
		if (this.block > 0 && amount > 0) {
			Color col = Color.SCARLET.cpy();
			col.a = this.blockTextColor.a;
			this.blockTextColor = col;
			this.blockScale = 5.0F;
		}
		
		this.updateDescription();
	}
	
	public void loseBlock(int amount) {
		this.loseBlock(amount, false);
	}
	
	private void brokeBlock() {
		AliceSpireKit.addEffect(new HbBlockBrokenEffect(
				this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X,
				this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y
		));
		CardCrawlGame.sound.play("BLOCK_BREAK");
	}
	
	public boolean takeDamage(int amount) {
		if (amount > 0) {
			int blockLoss = Math.min(this.block, amount);
			if (blockLoss > 0) {
				if (Settings.SHOW_DMG_BLOCK)
					AliceSpireKit.addEffect(new BlockedNumberEffect(
							this.hb.cX,
							this.hb.cY + this.hb.height / 2.0F,
							"" + blockLoss));
				
				this.loseBlock(blockLoss);
				if (this.block == 0)
					this.brokeBlock();
				else
					CardCrawlGame.sound.play("BLOCK_ATTACK");
				amount -= blockLoss;
			}
		}
		
		boolean ret = false;
		
		if (amount > 0) {
			this.HP -= amount;
			if (this.HP <= 0) {
				this.HP = 0;
				ret = true;
			}
		}
		
		this.updateDescription();
		this.healthBarUpdatedEvent();
		
		return ret;
	}
	
	public void showHealthBar() {
		this.hbShowTimer = 0.7F;
		this.hbAlpha = 0.0F;
	}
	
	public void hideHealthBar() {
		this.hbAlpha = 0.0F;
	}
	
	@Override
	public final void onEvoke() {
		assert false : "AbstractDoll.onEvoke() should not be called!";
	}
	
	public abstract void onAct();
	
	public void postSpawn() {}
	
	public void onRecycle() {}
	
	public void onDestroyed() {}
	
	public void onRemoved() {}
	
	@Override
	public void onStartOfTurn() {}
	
	@Override
	public void onEndOfTurn() {}
	
	public void postOtherDollAct(AbstractDoll doll) {}
	
	public void postOtherDollSpawn(AbstractDoll doll) {}
	
	public void postOtherDollRecycle(AbstractDoll doll) {}
	
	public void postOtherDollDestroyed(AbstractDoll doll) {}
	
	public void postOtherDollRemoved(AbstractDoll doll) {}
	
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
		this.description = this.HP + "/" + this.maxHP;
		if (this.block > 0)
			this.description += " (+" + this.block + ")";
		
		this.description += " NL #y被动 ：" + this.passiveDescription + " NL #y行动 ：" + this.actDescription;
	}
	
	public abstract void updateDescriptionImpl();
	
	@Override
	public final void applyFocus() { // Dolls should not be affected by focus.
		assert false : "AbstractDoll.applyFocus() should not be called!";
	}
	
	public void clearBlock(int preserve) {
		int blockLoss = this.block - preserve;
		if (blockLoss > 0)
			this.loseBlock(blockLoss);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		this.updateSelfAnimation();
		
		super.render(sb);
		
		this.renderHealth(sb);
		
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
				this.getFontColor(this.highlightPassiveValueTimer > 0.0F),
				this.passiveFontScale);
	}
	
	private void renderActValue(SpriteBatch sb, float x, float y) {
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				Integer.toString(this.actAmount),
				x,
				y,
				this.getFontColor(this.highlightActValueTimer > 0.0F),
				this.actFontScale);
	}
	
	@Override
	public void renderText(SpriteBatch sb) {
		assert this.renderTextMode != null: "AbstractDoll.renderTextMode should not be null!";
		
		this.passiveFontScale = MathHelper.scaleLerpSnap(this.passiveFontScale, FONT_SCALE);
		this.actFontScale = MathHelper.scaleLerpSnap(this.actFontScale, FONT_SCALE);
		
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
		
		if (this.damageAboutToTake > 0) {
			FontHelper.renderFontCentered(sb,
					FontHelper.cardEnergyFont_L,
					"" + this.damageAboutToTake,
					this.cX,
					this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 40.0F * Settings.scale,
					Color.RED,
					this.fontScale);
		}
	}
	
	
	public void healthBarUpdatedEvent() {
		this.healthBarAnimTimer = 1.2F;
		this.targetHealthBarWidth = this.hb.width * (float)this.HP / (float)this.maxHP;
		
		if (this.maxHP == this.HP) {
			this.healthBarWidth = this.targetHealthBarWidth;
		} else if (this.HP == 0) {
			this.healthBarWidth = 0.0F;
			this.targetHealthBarWidth = 0.0F;
		}
		
		if (this.targetHealthBarWidth > this.healthBarWidth) {
			this.healthBarWidth = this.targetHealthBarWidth;
		}
		
	}
	
	protected void updateHealthBar() {
		this.updateHbHoverFade();
		this.updateBlockAnimations();
		this.updateHbPopInAnimation();
		this.updateHbDamageAnimation();
		this.updateHbAlpha();
	}
	
	private void updateHbHoverFade() {
		if (this.healthHb.hovered) {
			this.healthHideTimer -= Gdx.graphics.getDeltaTime() * 4.0F;
			if (this.healthHideTimer < 0.2F) {
				this.healthHideTimer = 0.2F;
			}
		} else {
			this.healthHideTimer += Gdx.graphics.getDeltaTime() * 4.0F;
			if (this.healthHideTimer > 1.0F) {
				this.healthHideTimer = 1.0F;
			}
		}
		
	}
	
	private void updateHbAlpha() {
		if (this.targetHealthBarWidth == 0.0F && this.healthBarAnimTimer <= 0.0F) {
			this.hbShadowColor.a = MathHelper.fadeLerpSnap(this.hbShadowColor.a, 0.0F);
			this.hbBgColor.a = MathHelper.fadeLerpSnap(this.hbBgColor.a, 0.0F);
			this.hbTextColor.a = MathHelper.fadeLerpSnap(this.hbTextColor.a, 0.0F);
			this.blockOutlineColor.a = MathHelper.fadeLerpSnap(this.blockOutlineColor.a, 0.0F);
		} else {
			this.hbBgColor.a = this.hbAlpha * 0.5F;
			this.hbShadowColor.a = this.hbAlpha * 0.2F;
			this.hbTextColor.a = this.hbAlpha;
			this.redHbBarColor.a = this.hbAlpha;
			this.blueHbBarColor.a = this.hbAlpha;
			this.blockOutlineColor.a = this.hbAlpha;
		}
		
	}
	
	protected void gainBlockAnimation() {
		this.blockAnimTimer = BLOCK_ANIM_TIME;
		this.blockTextColor.a = 0.0F;
		this.blockColor.a = 0.0F;
	}
	
	private void updateBlockAnimations() {
		if (this.block > 0) {
			if (this.blockAnimTimer > 0.0F) {
				this.blockAnimTimer -= Gdx.graphics.getDeltaTime();
				if (this.blockAnimTimer < 0.0F) {
					this.blockAnimTimer = 0.0F;
				}
				
				this.blockOffset = Interpolation.swingOut.apply(BLOCK_OFFSET_DIST * 3.0F, 0.0F, 1.0F - this.blockAnimTimer / 0.7F);
				this.blockScale = Interpolation.pow3In.apply(3.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
				this.blockColor.a = Interpolation.pow2Out.apply(0.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
				this.blockTextColor.a = Interpolation.pow5In.apply(0.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
			} else if (this.blockScale != 1.0F) {
				this.blockScale = MathHelper.scaleLerpSnap(this.blockScale, 1.0F);
			}
			
			if (this.blockTextColor.r != 1.0F) {
				this.blockTextColor.r = MathHelper.slowColorLerpSnap(this.blockTextColor.r, 1.0F);
			}
			
			if (this.blockTextColor.g != 1.0F) {
				this.blockTextColor.g = MathHelper.slowColorLerpSnap(this.blockTextColor.g, 1.0F);
			}
			
			if (this.blockTextColor.b != 1.0F) {
				this.blockTextColor.b = MathHelper.slowColorLerpSnap(this.blockTextColor.b, 1.0F);
			}
		}
		
	}
	
	private void updateHbPopInAnimation() {
		if (this.hbShowTimer > 0.0F) {
			this.hbShowTimer -= Gdx.graphics.getDeltaTime();
			if (this.hbShowTimer < 0.0F) {
				this.hbShowTimer = 0.0F;
			}
			
			this.hbAlpha = Interpolation.fade.apply(0.0F, 1.0F, 1.0F - this.hbShowTimer / 0.7F);
			this.hbYOffset = Interpolation.exp10Out.apply(HB_Y_OFFSET_DIST * 5.0F, 0.0F, 1.0F - this.hbShowTimer / 0.7F);
		}
		
	}
	
	private void updateHbDamageAnimation() {
		if (this.healthBarAnimTimer > 0.0F) {
			this.healthBarAnimTimer -= Gdx.graphics.getDeltaTime();
		}
		
		if (this.healthBarWidth != this.targetHealthBarWidth && this.healthBarAnimTimer <= 0.0F && this.targetHealthBarWidth < this.healthBarWidth) {
			this.healthBarWidth = MathHelper.uiLerpSnap(this.healthBarWidth, this.targetHealthBarWidth);
		}
		
	}
	
	public void renderHealth(SpriteBatch sb) {
		if (!Settings.hideCombatElements) {
			float x = this.hb.cX - this.hb.width / 2.0F;
			float y = this.hb.cY - this.hb.height / 2.0F + this.hbYOffset;
			this.renderHealthBg(sb, x, y);
			if (this.targetHealthBarWidth != 0.0F) {
				this.renderRedHealthBar(sb, x, y);
			}
			
			if (this.block != 0 && this.hbAlpha != 0.0F) {
				this.renderBlockOutline(sb, x, y);
			}
			
			this.renderHealthText(sb, y);
			if (this.block != 0 && this.hbAlpha != 0.0F) {
				this.renderBlockIconAndValue(sb, x, y);
			}
		}
	}
	
	private void renderBlockOutline(SpriteBatch sb, float x, float y) {
		sb.setColor(this.blockOutlineColor);
		sb.setBlendFunction(770, 1);
		sb.draw(ImageMaster.BLOCK_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
		sb.draw(ImageMaster.BLOCK_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.hb.width, HEALTH_BAR_HEIGHT);
		sb.draw(ImageMaster.BLOCK_BAR_R, x + this.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
		sb.setBlendFunction(770, 771);
	}
	
	private void renderBlockIconAndValue(SpriteBatch sb, float x, float y) {
		sb.setColor(this.blockColor);
		sb.draw(ImageMaster.BLOCK_ICON, x + BLOCK_ICON_X - 32.0F, y + BLOCK_ICON_Y - 32.0F + this.blockOffset, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
		FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, "" + this.block, x + BLOCK_ICON_X, y - 16.0F * Settings.scale, this.blockTextColor, this.blockScale);
	}
	
	private void renderHealthBg(SpriteBatch sb, float x, float y) {
		sb.setColor(this.hbShadowColor);
		sb.draw(ImageMaster.HB_SHADOW_L, x - HEALTH_BAR_HEIGHT, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
		sb.draw(ImageMaster.HB_SHADOW_B, x, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, this.hb.width, HEALTH_BAR_HEIGHT);
		sb.draw(ImageMaster.HB_SHADOW_R, x + this.hb.width, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
		sb.setColor(this.hbBgColor);
		if (this.HP != this.maxHP) {
			sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
			sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.hb.width, HEALTH_BAR_HEIGHT);
			sb.draw(ImageMaster.HEALTH_BAR_R, x + this.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
		}
		
	}
	
	private void renderRedHealthBar(SpriteBatch sb, float x, float y) {
		if (this.block > 0) {
			sb.setColor(this.blueHbBarColor);
		} else {
			sb.setColor(this.redHbBarColor);
		}
		
		if (this.HP > 0) {
			sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
		}
		
		sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.targetHealthBarWidth, HEALTH_BAR_HEIGHT);
		sb.draw(ImageMaster.HEALTH_BAR_R, x + this.targetHealthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
	}
	
	private void renderHealthText(SpriteBatch sb, float y) {
		if (this.targetHealthBarWidth != 0.0F) {
			float tmp = this.hbTextColor.a;
			Color var10000 = this.hbTextColor;
			var10000.a *= this.healthHideTimer;
			FontHelper.renderFontCentered(
					sb,
					FontHelper.healthInfoFont,
					this.HP + "/" + this.maxHP, this.hb.cX,
					y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale, this.hbTextColor
			);
			this.hbTextColor.a = tmp;
		} else {
			FontHelper.renderFontCentered(
					sb,
					FontHelper.healthInfoFont,
					CREATURE_TEXT[0],
					this.hb.cX,
					y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y - 1.0F * Settings.scale, this.hbTextColor);
		}
	}
	
	public void renderReticle(SpriteBatch sb) {
		this.reticleRendered = true;
		this.renderReticleCorner(sb, -this.hb.width / 2.0F + this.reticleOffset, this.hb.height / 2.0F - this.reticleOffset, false, false);
		this.renderReticleCorner(sb, this.hb.width / 2.0F - this.reticleOffset, this.hb.height / 2.0F - this.reticleOffset, true, false);
		this.renderReticleCorner(sb, -this.hb.width / 2.0F + this.reticleOffset, -this.hb.height / 2.0F + this.reticleOffset, false, true);
		this.renderReticleCorner(sb, this.hb.width / 2.0F - this.reticleOffset, -this.hb.height / 2.0F + this.reticleOffset, true, true);
	}
	
	protected void updateReticle() {
		if (this.reticleRendered) {
			this.reticleRendered = false;
			this.reticleAlpha += Gdx.graphics.getDeltaTime() * 3.0F;
			if (this.reticleAlpha > 1.0F) {
				this.reticleAlpha = 1.0F;
			}
			
			this.reticleAnimTimer += Gdx.graphics.getDeltaTime();
			if (this.reticleAnimTimer > 1.0F) {
				this.reticleAnimTimer = 1.0F;
			}
			
			this.reticleOffset = Interpolation.elasticOut.apply(RETICLE_OFFSET_DIST, 0.0F, this.reticleAnimTimer);
		} else {
			this.reticleAlpha = 0.0F;
			this.reticleAnimTimer = 0.0F;
			this.reticleOffset = RETICLE_OFFSET_DIST;
		}
		
	}
	
	private void renderReticleCorner(SpriteBatch sb, float x, float y, boolean flipX, boolean flipY) {
		this.reticleShadowColor.a = this.reticleAlpha / 4.0F;
		sb.setColor(this.reticleShadowColor);
		sb.draw(ImageMaster.RETICLE_CORNER, this.hb.cX + x - 18.0F + 4.0F * Settings.scale, this.hb.cY + y - 18.0F - 4.0F * Settings.scale, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
		this.reticleColor.a = this.reticleAlpha;
		sb.setColor(this.reticleColor);
		sb.draw(ImageMaster.RETICLE_CORNER, this.hb.cX + x - 18.0F, this.hb.cY + y - 18.0F, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
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
	
	public void highlightPassiveValue() {
		this.highlightPassiveValueTimer = 0.5F;
		this.passiveFontScale = 1.5f;
	}
	
	public void highlightActValue() {
		this.highlightActValueTimer = 0.5F;
		this.actFontScale = 1.5f;
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
	
	public static AbstractDoll newInst(Class clazz) {
		assert AbstractDoll.class.isAssignableFrom(clazz) : "clazz is not a subclass of Abstract";
		
		if (clazz == EmptyDollSlot.class) {
			AliceSpireKit.log(AbstractDoll.class, "Why newInst(EmptyDollSlot)?");
			return new EmptyDollSlot();
		}
		else if (clazz == ShanghaiDoll.class) {
			return new ShanghaiDoll();
		}
		else if (clazz == NetherlandsDoll.class) {
			return new NetherlandsDoll();
		}
		else if (clazz == HouraiDoll.class) {
			return new HouraiDoll();
		}
		else if (clazz == KyotoDoll.class) {
			return new KyotoDoll();
		}
		else if (clazz == LondonDoll.class) {
			return new LondonDoll();
		}
		else if (clazz == FranceDoll.class) {
			return new FranceDoll();
		}
		else if (clazz == OrleansDoll.class) {
			return new OrleansDoll();
		}
		else {
			AliceSpireKit.log(AbstractDoll.class, "Unknown class: " + clazz);
			return null;
		}
	}
	
	@Nullable
	public static AbstractDoll getRandomDollExcept(Class... exceptClasses) {
		for (Class c : exceptClasses)
			assert AbstractDoll.class.isAssignableFrom(c) : "exceptClasses contains a class that is not doll";
		
		Class<? extends AbstractDoll>[] fullClasses = new Class[] {
				ShanghaiDoll.class,
				NetherlandsDoll.class,
				HouraiDoll.class,
				KyotoDoll.class,
				LondonDoll.class,
				FranceDoll.class,
				OrleansDoll.class
		};
		
		Class[] remainingClasses = Arrays.stream(fullClasses)
				.filter(c -> Arrays.stream(exceptClasses).noneMatch(ec -> ec.equals(c)))
				.toArray(Class[]::new);
		
		if (remainingClasses.length == 0) {
			AliceSpireKit.log(AbstractDoll.class, "No remaining classes to choose from.");
			return null;
		}
		
		int index = AbstractDungeon.cardRandomRng.random(remainingClasses.length - 1);
		return newInst(remainingClasses[index]);
	}
	
//	public static AbstractDoll getRandomDollExcept(Class<? extends AbstractDoll> exceptClass) {
//		return getRandomDollExcept(exceptClass);
//	}
	
	public static AbstractDoll getRandomDoll() {
		return getRandomDollExcept();
	}
}
