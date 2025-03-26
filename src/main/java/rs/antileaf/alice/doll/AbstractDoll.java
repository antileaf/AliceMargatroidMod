package rs.antileaf.alice.doll;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RunicDome;
import com.megacrit.cardcrawl.vfx.combat.BlockedNumberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import org.jetbrains.annotations.Nullable;
import rs.antileaf.alice.doll.dolls.*;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.doll.interfaces.PlayerOrEnemyDollAmountModHook;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.strings.AliceLanguageStrings;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.Arrays;
import java.util.HashMap;

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
	
//	protected final AliceDollStrings dollStrings;
	
	public int actAmount;
	protected int baseActAmount;
	
	protected String actDescription;
	
	public int maxHP;
	public int HP;
	public int block;
	
	protected DollAmountType passiveAmountType = DollAmountType.MAGIC;
	protected DollAmountType actAmountType = DollAmountType.MAGIC;

	protected int specialBuffer = 0;
	
	protected int damageAboutToTake = 0;
	protected int damageCount = 0;
	protected int overflowedDamage = 0;
	private float vfxTimer = 1.0F;
	protected float animX, animY;
	protected float animSpeed;
	
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
	
	public boolean dontShowHPDescription = false;
	
	public AbstractDoll(String ID, String NAME, int maxHP, int basePassiveAmount, int baseActAmount, String imgPath, RenderTextMode renderTextMode) {
		super(ID, NAME, basePassiveAmount, -1, "", "",
				 imgPath != null && AliceConfigHelper.isSunglassesEnabled() &&
						Gdx.files.internal(imgPath.replace(".png", "_ss.png")).exists() ?
						imgPath.replace(".png", "_ss.png") : imgPath);
		
//		if (this.dollStrings == null)
//			throw new IllegalArgumentException("DollStrings for " + ID + " is null.");
		
		this.angle = 0.0F;
		
		this.hb = new Hitbox(
				AbstractDungeon.player.hb.cX,
				AbstractDungeon.player.hb.cY,
				150.0F * Settings.scale,
				150.0F * Settings.scale
		);
		
		this.healthHb = new Hitbox(this.hb.width, 72.0F * Settings.scale);
		this.targetHealthBarWidth = this.hb.width;
		
		this.baseActAmount = this.actAmount = baseActAmount;
		this.renderTextMode = renderTextMode;
		
		this.maxHP = this.HP = maxHP;
		this.block = 0;
		
		this.hbYOffset = HB_Y_OFFSET_DIST * 1.5F;
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
//		this.initPosition(AbstractDungeon.player.animX, AbstractDungeon.player.animY);
	}
	
	protected abstract AliceDollStrings getDollStrings();
	
	protected void initPosition(float cx, float cy) {
		this.cX = this.tX = this.animX = cx;
		this.cY = this.tY = this.animY = cy;
		
		this.hb.move(this.cX, this.cY);
		this.healthHb.move(this.hb.cX, this.hb.cY - this.hb.height / 2.0F - this.healthHb.height / 2.0F);
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
	
	public float getTargetScale() {
		if (this.img == null || this.img.getWidth() == 96)
			return 1.0F;
		
		return 0.5F * 1.2F;
	}
	
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
			Vector2 pos = DollManager.get().calcDollPosition(DollManager.get().getIndex(this));
			if (pos != null)
				this.transAnim(pos);
			
//			this.updateDamageAboutToTake();
		}
		
		this.hb.move(this.tX, this.tY);
		this.hb.update();
		this.healthHb.move(this.hb.cX, this.hb.cY - this.hb.height / 2.0F - this.healthHb.height / 2.0F);
		this.healthHb.update();
		this.updateReticle();
		
		this.passiveFontScale = MathHelper.scaleLerpSnap(this.passiveFontScale, FONT_SCALE);
		this.actFontScale = MathHelper.scaleLerpSnap(this.actFontScale, FONT_SCALE);
		
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
		this.scale = Interpolation.swingIn.apply(Settings.scale * this.getTargetScale(), 0.01F, this.channelAnimTimer / 0.5F);
//		this.angle += Gdx.graphics.getDeltaTime() * 45.0F;
//		this.vfxTimer -= Gdx.graphics.getDeltaTime();
//		if (this.vfxTimer < 0.0F) {
//			this.vfxTimer = 0.0F; // TODO
//		}
		
		this.updateAnimation();
		
		// Why this must be here? I don't quite understand.
		if (this.hb.hovered) {
			this.renderGenericTip();
		}
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
		this.scale = Interpolation.swingIn.apply(Settings.scale * this.getTargetScale(), 0.01F, this.channelAnimTimer / 0.5F);
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
	
	public void updateOverflowedDamage() {
		if (this.damageAboutToTake > 0)
			this.overflowedDamage = this.onPlayerDamaged(this.damageAboutToTake * this.damageCount);
		else
			this.overflowedDamage = 0;
	}
	
	public void updateDamageAboutToTake(int damage, int count) {
		this.damageAboutToTake = damage;
		this.damageCount = count;
		
		this.updateOverflowedDamage();
	}
	
	public int calcTotalDamageAboutToTake() {
		return this.damageAboutToTake != -1 ? this.damageAboutToTake * this.damageCount : -1;
	}
	
	public int getOverflowedDamage() {
		return this.overflowedDamage;
	}
	
	// Returns remaining damage.
	public int onPlayerDamaged(int amount) {
		return Integer.max(0, amount - (this.HP + this.block));
	}
	
	public void addBlock(int blockAmount) {
		boolean showEffect = (this.block == 0);
		
		this.block = Math.min(this.block + blockAmount, 999);
		AbstractDungeon.effectList.add(new FlashAtkImgEffect(
				this.hb.cX,
				this.hb.cY,
				AbstractGameAction.AttackEffect.SHIELD
		));
		
		if (showEffect && this.block > 0) {
			this.gainBlockAnimation();
		}
		else if (blockAmount > 0) {
			Color col = Settings.GOLD_COLOR.cpy();
			col.a = this.blockTextColor.a;
			this.blockTextColor = col;
			this.blockScale = 5.0F;
		}
		
		this.updateDescription();
	}
	
	public void loseBlock(int amount, boolean shouldPlaySound) {
		boolean showEffect = (this.block > 0);
		
		if (amount > this.block)
			AliceHelper.log(AbstractDoll.class, "AbstractDoll.loseBlock() called with amount > block.");
		this.block = Math.max(0, this.block - amount);
		
		//
		
		if (this.block > 0 && amount > 0) {
			Color col = Color.SCARLET.cpy();
			col.a = this.blockTextColor.a;
			this.blockTextColor = col;
			this.blockScale = 5.0F;
		}
		else if (this.block == 0 && showEffect) {
			AbstractDungeon.effectList.add(new HbBlockBrokenEffect(
					this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X,
					this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
			if (shouldPlaySound)
				CardCrawlGame.sound.play("BLOCK_BREAK");
		}
		
		this.updateDescription();
	}
	
	public void loseBlock(int amount) {
		this.loseBlock(amount, false);
	}
	
	private void brokeBlock() {
		AliceHelper.addEffect(new HbBlockBrokenEffect(
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
					AliceHelper.addEffect(new BlockedNumberEffect(
							this.hb.cX,
							this.hb.cY + this.hb.height / 2.0F,
							"" + blockLoss));
				
				this.loseBlock(blockLoss, true);
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
	
	public void heal(int amount) {
		this.HP = Math.min(this.HP + amount, this.maxHP);
		this.updateDescription();
		AbstractDungeon.effectsQueue.add(new HealEffect(this.getDrawCX(), this.getDrawCY() +
				(this.img == null ? 96.0F : this.img.getHeight()), amount));
		this.healthBarUpdatedEvent();
	}

	public void increaseMaxHealth(int amount) {
		this.maxHP += amount;
		this.HP += amount;
		this.updateDescription();
		AbstractDungeon.effectsQueue.add(new HealEffect(this.getDrawCX(), this.getDrawCY() +
				(this.img == null ? 96.0F : this.img.getHeight()), amount));
		this.healthBarUpdatedEvent();
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
	
	public abstract String getID();
	
	public abstract int getBaseHP();
	
//	public int getDarkGrimoireBaseHP() {
//		if (!AliceSpireKit.isInBattle())
//			return this.getBaseHP();
//
//		if (AbstractDungeon.player.hasRelic(AlicesDarkGrimoire.ID))
//			return this.getBaseHP() +
//					(int)DollManager.get().getDolls().stream()
//							.filter(doll -> doll != this && !(doll instanceof EmptyDollSlot))
//							.count() * AlicesDarkGrimoire.MULTIPLIER;
//
//		return this.getBaseHP();
//	}
	
	// All actions added by onAct() should call addActionToBuffer.
	// The buffer will be committed in DollManager.dollAct().
	public abstract void onAct();

	public void onSpecialAct() {
		this.onAct();
	}
	
	public void postSpawn() {}
	
	public void onRecycle() {}
	
	public void onDestroyed() {}
	
	// Not equal to recycled or destroyed.
	public void onRemoved() {}
	
	@Override
	public void onStartOfTurn() {}
	
	public void postEnergyRecharge() {}
	
	@Override
	public void onEndOfTurn() {}
	
	// Returning true will cancel this spawn.
//	public boolean preOtherDollSpawn(AbstractDoll doll) {
//		return false;
//	}
	
	public void postOtherDollSpawn(AbstractDoll doll) {}
	
	public void postOtherDollAct(AbstractDoll doll) {}
	
	public void postOtherDollRecycled(AbstractDoll doll) {}
	
	public void postOtherDollDestroyed(AbstractDoll doll) {}
	
//	public void postOtherDollRemoved(AbstractDoll doll) {}
	
	public void applyPower() {
		AbstractPlayer player = AbstractDungeon.player;
		
		this.passiveAmount = this.basePassiveAmount;
		this.actAmount = this.baseActAmount;
		
//		int hourai = DollManager.get().getTotalHouraiPassiveAmount();
//		if (hourai > 0) {
//			if (this.passiveAmountType != DollAmountType.MAGIC)
//				this.passiveAmount += hourai;
//			if (this.actAmountType != DollAmountType.MAGIC)
//				this.actAmount += hourai;
//		}
		
		if (this instanceof ShanghaiDoll) {
			if (player.hasPower(StrengthPower.POWER_ID)) {
				int bonus = Integer.max(player.getPower(StrengthPower.POWER_ID).amount, 0);
				this.actAmount += bonus;
			}
		}
		
		float tempPassiveAmount = this.passiveAmount, tempActAmount = this.actAmount;
		
		for (AbstractRelic r : player.relics)
			if (r instanceof PlayerOrEnemyDollAmountModHook) {
				tempPassiveAmount = ((PlayerOrEnemyDollAmountModHook) r).modifyDollAmount(
						tempPassiveAmount, this, this.passiveAmountType, DollAmountTime.PASSIVE);
				
				tempActAmount = ((PlayerOrEnemyDollAmountModHook) r).modifyDollAmount(
						tempActAmount, this, this.actAmountType, DollAmountTime.ACT);
			}
		
		for (AbstractPower p : player.powers)
			if (p instanceof PlayerOrEnemyDollAmountModHook) {
				tempPassiveAmount = ((PlayerOrEnemyDollAmountModHook) p).modifyDollAmount(
						tempPassiveAmount, this, this.passiveAmountType, DollAmountTime.PASSIVE);
				
				tempActAmount = ((PlayerOrEnemyDollAmountModHook) p).modifyDollAmount(
						tempActAmount, this, this.actAmountType, DollAmountTime.ACT);
			}
		
		this.passiveAmount = (int) tempPassiveAmount;
		this.actAmount = (int) tempActAmount;
		
		this.updateDescription();
	}
	
	@Override
	public void updateDescription() {
		this.updateDescriptionImpl();
		
		AliceDollStrings dollStrings = this.getDollStrings();
		
		this.description = dollStrings.TYPE + " - " + dollStrings.TAG_COLOR + dollStrings.TAG;
		if (!this.dontShowHPDescription) {
			this.description += " NL " + this.HP + "/" + this.maxHP;
			if (this.block > 0)
				this.description += " (+" + this.block + ")";
		}
		this.description += " NL ";
		
		UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(AliceHelper.makeID("DollDescription"));
		String passiveString = uiStrings.TEXT[0];
		String actString = uiStrings.TEXT[1];
		
		this.description += "#y" + passiveString + " #y- " + "#y" + dollStrings.PASSIVE_NAME +
				" " + AliceLanguageStrings.COLON + this.passiveDescription +
				" NL #y" + actString + " " + AliceLanguageStrings.COLON + this.actDescription;
	}
	
	public void updateDescriptionImpl() {
		AliceDollStrings dollStrings = this.getDollStrings();
		
		this.passiveDescription = String.format(dollStrings.PASSIVE_DESCRIPTION, this.coloredPassiveAmount());
		this.actDescription = String.format(dollStrings.ACT_DESCRIPTION, this.coloredActAmount());
	}
	
	@Override
	public final void applyFocus() { // Dolls should not be affected by focus.
		assert false : "AbstractDoll.applyFocus() should not be called!";
	}
	
	public void clearBlock(int preserve) {
		int blockLoss = this.block - preserve;
		if (blockLoss > 0)
			this.loseBlock(blockLoss);
	}
	
	public float getDrawCX() {
		return this.hb.cX;
	}
	
	public float getDrawCY() { // 卡图的底边中点
		return this.hb.cY -
				(this.img != null ? this.img.getHeight() / 2.0F : 96.0F / 2.0F) +
				this.bobEffect.y / 4.0F;
	}
	
	public float getImgSize() {
		return this.img != null ? this.img.getWidth() : 96.0F;
	}
	
	public void renderImage(SpriteBatch sb) {
		sb.draw(
				this.img,
				this.cX - (float)this.img.getWidth() / 2.0F,
				this.cY - (float)this.img.getHeight() / 2.0F + this.bobEffect.y / 8.0F,
				(float)this.img.getWidth() / 2.0F,
				(float)this.img.getHeight() / 2.0F,
				(float)this.img.getWidth(),
				(float)this.img.getHeight(),
				this.scale,
				this.scale,
				this.angle,
				0,
				0,
				this.img.getWidth(),
				this.img.getHeight(),
				false,
				false);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		this.updateSelfAnimation();
		
		sb.setColor(this.c);
		this.renderImage(sb);
		
		this.renderHealth(sb);
		this.renderText(sb);
		this.hb.render(sb);
	}
	
	private Color getFontColor(boolean highlight) {
		return highlight ? new Color(0.2F, 1.0F, 1.0F, this.c.a) : this.c;
	}
	
	protected String getRenderPassiveValue() {
		return Integer.toString(this.passiveAmount);
	}
	
	private void renderPassiveValue(SpriteBatch sb, float x, float y) {
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				this.getRenderPassiveValue(),
				x,
				y,
				this.getFontColor(this.highlightPassiveValueTimer > 0.0F),
				this.passiveFontScale);
	}
	
	protected String getRenderActValue() {
		return Integer.toString(this.actAmount);
	}
	
	private void renderActValue(SpriteBatch sb, float x, float y) {
		FontHelper.renderFontCentered(sb,
				FontHelper.cardEnergyFont_L,
				this.getRenderActValue(),
				x,
				y,
				this.getFontColor(this.highlightActValueTimer > 0.0F),
				this.actFontScale);
	}
	
	protected float getRenderXOffset() {
		return NUM_X_OFFSET;
	}
	
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale;
	}
	
	@Override
	public void renderText(SpriteBatch sb) {
		assert this.renderTextMode != null: "AbstractDoll.renderTextMode should not be null!";
		
//		this.passiveFontScale = MathHelper.scaleLerpSnap(this.passiveFontScale, FONT_SCALE);
//		this.actFontScale = MathHelper.scaleLerpSnap(this.actFontScale, FONT_SCALE);
		
		if (this.renderTextMode == RenderTextMode.PASSIVE) {
			this.renderPassiveValue(sb,
					this.cX + this.getRenderXOffset(),
					this.cY + this.getRenderYOffset());
		}
		else if (this.renderTextMode == RenderTextMode.ACT) {
			this.renderActValue(sb,
					this.cX + this.getRenderXOffset(),
					this.cY + this.getRenderYOffset());
		}
		else if (this.renderTextMode == RenderTextMode.BOTH) {
			this.renderPassiveValue(sb,
					this.cX + this.getRenderXOffset(),
					this.cY + this.getRenderYOffset() + 24.0F * Settings.scale);
			
			this.renderActValue(sb,
					this.cX + this.getRenderXOffset(),
					this.cY + this.getRenderYOffset());
		}
		
		if (AbstractDungeon.player != null && !AbstractDungeon.player.hasRelic(RunicDome.ID)) {
			if (this.damageAboutToTake >= 0) {
				FontHelper.renderFontCentered(
						sb,
						FontHelper.cardEnergyFont_L,
						this.damageAboutToTake + (this.damageCount > 1 ? "x" + this.damageCount : ""),
						this.cX,
						this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 50.0F * Settings.scale,
						Color.RED,
						this.fontScale
				);
			}
			if (this.overflowedDamage > 0) {
				FontHelper.renderFontCentered(
						sb,
						FontHelper.cardEnergyFont_L,
						"(" + this.overflowedDamage + ")",
						(this.cX + AbstractDungeon.player.drawX) / 2.0F,
						(this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 50.0F * Settings.scale +
								AbstractDungeon.player.drawY + AbstractDungeon.player.hb.height / 2.0F) / 2.0F,
						Color.RED,
						this.fontScale
				);
			}
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
		return AliceHelper.coloredNumber(this.passiveAmount, this.basePassiveAmount);
	}
	
	protected String coloredActAmount() {
		return AliceHelper.coloredNumber(this.actAmount, this.baseActAmount);
	}
	
	public enum RenderTextMode {
		NONE, PASSIVE, ACT, BOTH
	}
	
	protected final float STOP_ANIM = -9999.0F;
	
	public void addToTop(AbstractGameAction action) {
		AliceHelper.addToTop(action);
	}
	
	public void addActionsToTop(AbstractGameAction... actions) {
		AliceHelper.addActionsToTop(actions);
	}
	
	public void addToBot(AbstractGameAction action) {
		AliceHelper.addToBot(action);
	}
	
	public static String getHpDescription(int maxHP) {
		UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(AliceHelper.makeID("DollHPDescription"));
		return String.format(uiStrings.TEXT[0], maxHP);
	}
	
	public static String getName(String clazz) {
		if (clazz == null) {
			AliceHelper.log(AbstractDoll.class, "getName() called with null.");
			return "Error";
		}
		
		if (clazz.equals(ShanghaiDoll.ID))
			return AliceDollStrings.get(ShanghaiDoll.ID).NAME;
		else if (clazz.equals(KyotoDoll.ID))
			return AliceDollStrings.get(KyotoDoll.ID).NAME;
		else if (clazz.equals(HouraiDoll.ID))
			return AliceDollStrings.get(HouraiDoll.ID).NAME;
		else if (clazz.equals(NetherlandsDoll.ID))
			return AliceDollStrings.get(NetherlandsDoll.ID).NAME;
		else if (clazz.equals(FranceDoll.ID))
			return AliceDollStrings.get(FranceDoll.ID).NAME;
		else if (clazz.equals(Su_san.ID))
			return AliceDollStrings.get(Su_san.ID).NAME;
		else
			return "Unknown";
	}

	public static String getKeyword(String clazz) {
		if (AliceHelper.getLangShort().equals("zhs"))
			return getName(clazz);

		return getName(clazz).replace(" ", "_");
	}
	
	public static AbstractDoll newInst(String clazz) {
//		assert AbstractDoll.class.isAssignableFrom(clazz) : "clazz is not a subclass of Abstract";
//		assert clazz != null : "clazz is null";
		if (clazz == null) {
			AliceHelper.log("AbstractDoll.newInst", "clazz is null");
			return null;
		}
		
		if (clazz.equals(EmptyDollSlot.ID)) {
			AliceHelper.log(AbstractDoll.class, "Why newInst(EmptyDollSlot)?");
			return new EmptyDollSlot();
		}
		else if (clazz.equals(ShanghaiDoll.ID)) {
			return new ShanghaiDoll();
		}
		else if (clazz.equals(KyotoDoll.ID)) {
			return new KyotoDoll();
		}
		else if (clazz.equals(HouraiDoll.ID)) {
			return new HouraiDoll();
		}
		else if (clazz.equals(NetherlandsDoll.ID)) {
			return new NetherlandsDoll();
		}
		else if (clazz.equals(FranceDoll.ID)) {
			return new FranceDoll();
		}
		else if (clazz.equals(Su_san.ID)) {
			return new Su_san();
		}
		else {
			AliceHelper.log(AbstractDoll.class, "Unknown class: " + clazz);
			return null;
		}
	}
	
	public final static String[] dollClasses = new String[] {
			"ShanghaiDoll",
			"KyotoDoll",
			"HouraiDoll",
			"NetherlandsDoll",
			"FranceDoll"
	};
	
	public static String getRandomDollIdExcept(String... exceptClasses) {
//		for (Class c : exceptClasses)
//			assert AbstractDoll.class.isAssignableFrom(c) : "exceptClasses contains a class that is not doll";
		
		String[] remainingClasses = Arrays.stream(dollClasses)
				.filter(c -> Arrays.stream(exceptClasses).noneMatch(ec -> ec.equals(c)))
				.toArray(String[]::new);
		
		AliceHelper.log(AbstractDoll.class, "Remaining classes: " + Arrays.toString(remainingClasses));
		AliceHelper.log(AbstractDoll.class, "Except classes: " + Arrays.toString(exceptClasses));
		
		if (remainingClasses.length == 0) {
			AliceHelper.log(AbstractDoll.class, "No remaining classes to choose from.");
			return null;
		}
		
		int index = AbstractDungeon.cardRandomRng.random(remainingClasses.length - 1);
		return remainingClasses[index];
	}
	
	@Nullable
	public static AbstractDoll getRandomDollExcept(String... exceptClasses) {
		return newInst(getRandomDollIdExcept(exceptClasses));
	}
	
//	public static AbstractDoll getRandomDollExcept(Class<? extends AbstractDoll> exceptClass) {
//		return getRandomDollExcept(exceptClass);
//	}
	
	public static String getRandomDollId() {
		return getRandomDollIdExcept();
	}
	
	public static AbstractDoll getRandomDoll() {
		return getRandomDollExcept();
	}
	
	public static boolean isBaseDoll(AbstractDoll doll) {
		String[] fullClasses = new String[] {
				ShanghaiDoll.ID,
				NetherlandsDoll.ID,
				FranceDoll.ID,
				KyotoDoll.ID,
				HouraiDoll.ID
		};
		
		return Arrays.stream(fullClasses).anyMatch(c -> c.equals(doll.ID));
	}
	
	private static final HashMap<String, String> descriptions = new HashMap<>();
	
	// Please use this only when the doll is not in the game.
	public String desc() {
		this.dontShowHPDescription = true;
		this.updateDescription();
		return this.description;
	}
	
	public static String getDescription(String dollID) {
		if (descriptions.containsKey(dollID))
			return descriptions.get(dollID);
		
		String res = "";
		if (dollID.equals(ShanghaiDoll.ID))
			res = ShanghaiDoll.getDescription();
		else if (dollID.equals(NetherlandsDoll.ID))
			res = NetherlandsDoll.getDescription();
		else if (dollID.equals(FranceDoll.ID))
			res = FranceDoll.getDescription();
		else if (dollID.equals(KyotoDoll.ID))
			res = KyotoDoll.getDescription();
		else if (dollID.equals(HouraiDoll.ID))
			res = HouraiDoll.getDescription();
		else
			AliceHelper.log(AbstractDoll.class, "getDescription() Unknown doll ID: " + dollID);
		
		descriptions.put(dollID, res);
		return res;
	}
}
