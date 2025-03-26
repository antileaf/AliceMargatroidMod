package rs.antileaf.alice.cards;

import basemod.ReflectionHacks;
import basemod.helpers.TooltipInfo;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import rs.antileaf.alice.cards.utils.AbstractSecondaryVariablesCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.strings.AliceCardNoteStrings;
import rs.antileaf.alice.strings.AliceCardSignStrings;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.targeting.handlers.*;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceHelper;
import rs.antileaf.alice.utils.SignatureHelper;

import java.util.ArrayList;
import java.util.List;

import static rs.antileaf.alice.AliceMargatroidMod.ALICE_PUPPETEER_FLAVOR;

public abstract class AbstractAliceCard extends AbstractSecondaryVariablesCard {
	private static final float FADE_DURATION = 0.3F;
	private static final float FORCED_FADE_DURATION = 0.5F;

	protected String cardSign = null;
	public final ArrayList<AliceTargetIcon> targetIcons = new ArrayList<>();

//	public boolean cantBePlayed = false;

	private TextureAtlas.AtlasRegion signaturePortrait = null;
	public boolean hasSignature = false;

	private boolean signatureHovered = false; // 矢野你诗人？
	public float signatureHoveredTimer = 0.0F;
	public float forcedTimer = 0.0F;

	public float previewTransparency = -1.0F;
	public boolean dontAvoidSCVPanel = false;
	
	public AbstractAliceCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			AbstractCard.CardType type,
			AbstractCard.CardColor color,
			AbstractCard.CardRarity rarity,
			AbstractCard.CardTarget target
	) {
		super(
				id,
				name,
				img != null ? img :
						(type == CardType.ATTACK ? AliceHelper.getCardImgFilePath("Attack") :
								type == CardType.SKILL ? AliceHelper.getCardImgFilePath("Skill") :
										AliceHelper.getCardImgFilePath("Power")),
				cost,
				rawDescription,
				type,
				color,
				rarity,
				target
		);
		
		FlavorText.AbstractCardFlavorFields.boxColor.set(this, ALICE_PUPPETEER_FLAVOR);

//		this.iconsHb = new Hitbox(-100.0F, -100.0F, 0.0F, 0.0F);
//		this.iconsTips = new ArrayList<>();

		AliceCardSignStrings sign = AliceCardSignStrings.get(this.cardID);
		if (sign != null)
			this.cardSign = sign.SIGN;

		this.signaturePortrait = SignatureHelper.load(this.getSignatureImgPath());
		if (this.signaturePortrait != null)
			this.hasSignature = true;
	}

	public String getSignatureImgPath() {
		return this.textureImg.replace(".png", "_s.png")
				.replace("/cards/", "/signature/");
	}

	public String getSignaturePortraitImgPath() {
		return this.getSignatureImgPath().replace(".png", "_p.png");
	}
	
	public TooltipInfo getNote() {
		AliceCardNoteStrings note = AliceCardNoteStrings.get(this.cardID);
		if (note == null)
			return null;
		
		return new TooltipInfo(note.TITLE, note.DESCRIPTION);
	}
	
	@Override
	public List<TooltipInfo> getCustomTooltipsTop() {
		ArrayList<TooltipInfo> result = new ArrayList<>();

		TooltipInfo note = this.getNote();
		if (note != null)
			result.add(note);
		
		return result;
	}
	
	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		
		if (this.baseSecondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalDamageModified = this.isDamageModified;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);
			
			this.baseDamage = this.baseSecondaryDamage;
			super.calculateCardDamage(mo);
			this.isSecondaryDamageModified = this.secondaryDamage != this.baseSecondaryDamage;
			this.secondaryDamage = this.damage;
			
			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isDamageModified = originalDamageModified;
			this.multiDamage = originalMultiDamage;
		}
	}

	public AbstractDoll getTargetedSlot() {
		if (this.target == CardTargetEnum.DOLL)
			return DollTargeting.getTarget(this);
		else if (this.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT)
			return DollOrEmptySlotTargeting.getTarget(this);
		else if (this.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE)
			return DollOrEmptySlotOrNoneTargeting.getTarget(this);
		else if (this.target == CardTargetEnum.DOLL_OR_NONE)
			return DollOrNoneTargeting.getTarget(this);
		else if (this.target == CardTargetEnum.DOLL_OR_ENEMY) {
			Object target = DollOrEnemyTargeting.getTarget(this);
			return target instanceof AbstractDoll ? (AbstractDoll) target : null;
		}
		else if (this.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_ENEMY) {
			Object target = DollOrEmptySlotOrEnemyTargeting.getTarget(this);
			return target instanceof AbstractDoll ? (AbstractDoll) target : null;
		}
		else
			return null; // Not a doll card.
	}
	
	public AbstractDoll getTargetedDoll() {
		AbstractDoll slot = this.getTargetedSlot();
		return slot instanceof EmptyDollSlot ? null : slot;
	}

	@Deprecated
	public AbstractMonster getTargetedEnemy() {
		if (this.target == CardTargetEnum.DOLL_OR_ENEMY) {
			Object target = DollOrEnemyTargeting.getTarget(this);
			return target instanceof AbstractMonster ? (AbstractMonster) target : null;
		}
		else if (this.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_ENEMY) {
			Object target = DollOrEmptySlotOrEnemyTargeting.getTarget(this);
			return target instanceof AbstractMonster ? (AbstractMonster) target : null;
		}
		else
			return null; // Not a doll-or-enemy card.
	}
	
	public AliceHoveredTargets getHoveredTargets(AbstractMonster hoveredMonster, AbstractDoll hoveredSlot) {
		return AliceHoveredTargets.NONE;
	}

	@Override
	 public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		if (!super.canUse(p, m))
			return false;
		
		if (this.target == CardTargetEnum.DOLL) {
			if (!DollManager.get().hasDoll() && DollTargeting.getTarget(this) == null) {
				this.cantUseMessage = CardCrawlGame.languagePack
						.getUIString(AliceHelper.makeID("NoDollDialog")).TEXT[0];
				AliceHelper.log("cantUseMessage: " + this.cantUseMessage);

				// TODO: There may be a bug in stslib. Fix it later.
				return false;
			}
		}
		
		return true;
	}

	public void aliceTriggerAtStartOfTurn() {}

	@Override
	public void update() {
		super.update();

		if (this.signatureHovered || (AliceHelper.isInBattle() && this.isHoveredInHand(1.0F))) {
			this.signatureHoveredTimer += Gdx.graphics.getDeltaTime();
			if (this.signatureHoveredTimer >= FADE_DURATION)
				this.signatureHoveredTimer = FADE_DURATION;
		}
		else {
			this.signatureHoveredTimer -= Gdx.graphics.getDeltaTime();
			if (this.signatureHoveredTimer <= 0.0F)
				this.signatureHoveredTimer = 0.0F;
		}

		if (this.forcedTimer > 0.0F) {
			this.forcedTimer -= Gdx.graphics.getDeltaTime();
			if (this.forcedTimer <= 0.0F)
				this.forcedTimer = 0.0F;
		}
	}
	
	public void renderCardSign(SpriteBatch sb, float xPos, float yPos, float yOffsetBase, float scale) {
		if (!AliceConfigHelper.enableSpellCardSignDisplay())
			return;
		
		if (this.cardSign != null) {
			if (this.isFlipped || this.isLocked || this.transparency <= 0.0F)
				return;
			
			float offsetY = yOffsetBase * Settings.scale * scale / 2.0F;
			BitmapFont.BitmapFontData fontData = FontHelper.cardTitleFont.getData();
			float originalScale = fontData.scaleX;
			float scaleMultiplier = 0.8F;
			
			fontData.setScale(scaleMultiplier * scale * 0.85F);
			Color color = Settings.CREAM_COLOR.cpy();
			color.a = this.transparency;
			FontHelper.renderRotatedText(
					sb,
					FontHelper.cardTitleFont,
					this.cardSign,
					xPos,
					yPos,
					0.0F,
					offsetY,
					this.angle,
					true,
					color
			);
			fontData.setScale(originalScale);
		}
	}
	
	public void renderCardSign(SpriteBatch sb) {
		this.renderCardSign(sb, this.current_x, this.current_y, 400.0F, this.drawScale);
	}

	private void drawOnCard(SpriteBatch sb, Texture img, float xPos, float yPos, Vector2 offset,
							float width, float height, float alpha, float drawScale, float scaleModifier) {
		if (this.angle != 0.0F)
			offset.rotate(this.angle);

		offset.scl(Settings.scale * drawScale);

		float drawX = xPos + offset.x, drawY = yPos + offset.y;
		float scale = drawScale * Settings.scale * scaleModifier;

		Color backup = sb.getColor().cpy();
		sb.getColor().a *= alpha;
		sb.draw(
				img,
				drawX - width / 2.0F,
				drawY - height / 2.0F,
				width / 2.0F,
				height / 2.0F,
				width,
				height,
				scale,
				scale,
				this.angle,
				0,
				0,
				img.getWidth(),
				img.getHeight(),
				false,
				false
		);
		sb.setColor(backup);
	}

	public void renderTargetIcons(SpriteBatch sb, float xPos, float yPos, float xOffsetBase, float yOffsetBase,
								  float width, float height, float alpha, float drawScale) {
		if (this.isFlipped || this.isLocked || this.transparency <= 0.0F)
			return;

		float xOffset = xOffsetBase, yOffset = yOffsetBase;

		for (AliceTargetIcon icon : this.targetIcons) {
			this.drawOnCard(sb, icon.bg, xPos, yPos, new Vector2(xOffset, yOffset), width, height,
					alpha * this.transparency, drawScale, AliceTargetIcon.BG_SCALE);
			this.drawOnCard(sb, icon.img, xPos, yPos, new Vector2(xOffset, yOffset), width, height,
					alpha * this.transparency, drawScale, icon.scaleModifier);

//			xOffset += width;
			yOffset -= height;
		}
	}

	private static final float X_OFFSET = 134.0F, Y_OFFSET = 100.0F;

	public void renderTargetIcons(SpriteBatch sb) {
//		this.renderTargetIcons(sb, this.current_x, this.current_y, 48.0F, -22.0F,
//				AliceTargetIcon.WIDTH, AliceTargetIcon.WIDTH, this.drawScale);
		if (SignatureHelper.shouldUseSignature(this.cardID) && this.getSignatureTransparency() <= 0.0F)
			return;

		this.renderTargetIcons(sb, this.current_x, this.current_y,
				X_OFFSET, Y_OFFSET,
				AliceTargetIcon.WIDTH, AliceTargetIcon.WIDTH,
				 SignatureHelper.shouldUseSignature(this.cardID) ? this.getSignatureTransparency() : 1.0F,
				this.drawScale);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		this.renderCardSign(sb);
//		this.renderTargetIcons(sb);
		// The logic for rendering target icons is moved to patches.

//		this.updateIconsHb();
//		this.iconsHb.render(sb);
	}

	@Override
	public void renderInLibrary(SpriteBatch sb) {
		super.renderInLibrary(sb);
		if (!SingleCardViewPopup.isViewingUpgrade || !this.isSeen || this.isLocked) {
			this.renderCardSign(sb);
//			this.renderTargetIcons(sb);

//			this.updateIconsHb();
//			this.iconsHb.render(sb);
		}
	}

	public void forceToShowDescription() {
		this.signatureHoveredTimer = FORCED_FADE_DURATION;
	}

	private float getSignatureTransparency() {
		if (this.previewTransparency >= 0.0F)
			return this.previewTransparency;

		float ret = Math.max(this.signatureHoveredTimer, this.forcedTimer) / FADE_DURATION;
		if (ret > 1.0F)
			ret = 1.0F;
		return ret;
	}

	@Override
	public void hover() {
		super.hover();
		this.signatureHovered = true;
	}

	@Override
	public void unhover() {
		super.unhover();
		this.signatureHovered = false;
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
		super.renderCardTip(sb);

		float transparency = SignatureHelper.shouldUseSignature(this.cardID) ?
				this.getSignatureTransparency() : 1.0F;

		if (this.cardsToPreview instanceof AbstractAliceCard &&
				SignatureHelper.shouldUseSignature(this.cardsToPreview.cardID))
			((AbstractAliceCard) this.cardsToPreview).previewTransparency = transparency;

		if (MultiCardPreview.multiCardPreview.get(this) != null) {
			for (AbstractCard c : MultiCardPreview.multiCardPreview.get(this)) {
				if (c instanceof AbstractAliceCard &&
						SignatureHelper.shouldUseSignature(c.cardID))
					((AbstractAliceCard) c).previewTransparency = transparency;
			}
		}
	}

	@SpireOverride
	protected void renderDescription(SpriteBatch sb) {
		if (!SignatureHelper.shouldUseSignature(this.cardID)) {
			SpireSuper.call(sb);
			return;
		}

		if (this.getSignatureTransparency() > 0.0F) {
			Color textColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "textColor");
			Color goldColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "goldColor");
			float textColorAlpha = textColor.a, goldColorAlpha = goldColor.a;

			textColor.a *= this.getSignatureTransparency();
			goldColor.a *= this.getSignatureTransparency();
			SpireSuper.call(sb);

			textColor.a = textColorAlpha;
			goldColor.a = goldColorAlpha;
		}
	}

	@SpireOverride
	protected void renderDescriptionCN(SpriteBatch sb) {
		if (!SignatureHelper.shouldUseSignature(this.cardID)) {
			SpireSuper.call(sb);
			return;
		}

		if (this.getSignatureTransparency() > 0.0F) {
			Color textColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "textColor");
			Color goldColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "goldColor");
			float textColorAlpha = textColor.a, goldColorAlpha = goldColor.a;

			textColor.a *= this.getSignatureTransparency();
			goldColor.a *= this.getSignatureTransparency();
			SpireSuper.call(sb);

			textColor.a = textColorAlpha;
			goldColor.a = goldColorAlpha;
		}
	}

	private Color getRenderColor() {
		return ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");
	}

	private Color getTypeColor() {
		return ReflectionHacks.getPrivate(this, AbstractCard.class, "typeColor");
	}

	private void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img,
							  float drawX, float drawY) {
		ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper",
				SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class)
				.invoke(this, sb, color, img, drawX, drawY);
	}

	@Override
	public void renderSmallEnergy(SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y) {
		if (SignatureHelper.shouldUseSignature(this.cardID)) {
			Color renderColor = this.getRenderColor();

			float alpha = renderColor.a;
			renderColor.a *= this.getSignatureTransparency();

			super.renderSmallEnergy(sb, region, x, y);

			renderColor.a = alpha;
		}
		else
			super.renderSmallEnergy(sb, region, x, y);
	}

	@SpireOverride
	protected void renderImage(SpriteBatch sb, boolean hovered, boolean selected) {
		SpireSuper.call(sb, hovered, selected);

		if (SignatureHelper.shouldUseSignature(this.cardID) && this.getSignatureTransparency() > 0.0F) {
			Color renderColor = this.getRenderColor();

			float alpha = renderColor.a;
			renderColor.a *= this.getSignatureTransparency();

			this.renderHelper(sb, renderColor,
					this.description.size() >= 4 ?
							SignatureHelper.DESC_SHADOW : SignatureHelper.DESC_SHADOW_SMALL,
					this.current_x, this.current_y);

			renderColor.a = alpha;
		}
	}

	@SpireOverride
	protected void renderCardBg(SpriteBatch sb, float x, float y) {
		if (SignatureHelper.shouldUseSignature(this.cardID)) {
//			TextureAtlas.AtlasRegion bg;
//
//			if (this.type == CardType.ATTACK)
//				bg = SignatureHelper.ATTACK_BG;
//			else if (this.type == CardType.POWER)
//				bg = SignatureHelper.POWER_BG;
//			else
//				bg = SignatureHelper.SKILL_BG;
//
//			this.renderHelper(sb, this.getRenderColor(), bg, x, y);
		}
		else
			SpireSuper.call(sb, x, y);
	}

//	@Override
//	public Texture getBackgroundSmallTexture() {
//		if (SignatureHelper.shouldUseSignature(this.cardID)) {
//			if (this.type == CardType.ATTACK)
//				return SignatureHelper.ATTACK_BG;
//			else if (this.type == CardType.POWER)
//				return SignatureHelper.POWER_BG;
//			else
//				return SignatureHelper.SKILL_BG;
//		}
//		else
//			return super.getBackgroundSmallTexture();
//	}

	@SpireOverride
	protected void renderPortrait(SpriteBatch sb) {
		if (SignatureHelper.shouldUseSignature(this.cardID)) {
			sb.setColor(this.getRenderColor());
			sb.draw(this.signaturePortrait,
					this.current_x - 256.0F,
					this.current_y - 256.0F,
					256.0F, 256.0F, 512.0F, 512.0F,
					this.drawScale * Settings.scale,
					this.drawScale * Settings.scale,
					this.angle);
		}
		else
			SpireSuper.call(sb);
	}

	@SpireOverride
	protected void renderJokePortrait(SpriteBatch sb) {
		if (SignatureHelper.shouldUseSignature(this.cardID))
			this.renderPortrait(sb);
		else
			SpireSuper.call(sb);
	}

	@SpireOverride
	protected void renderPortraitFrame(SpriteBatch sb, float x, float y) {
		if (SignatureHelper.shouldUseSignature(this.cardID)) {
			TextureAtlas.AtlasRegion frame;

			if (this.type == CardType.ATTACK) {
				if (this.rarity == CardRarity.RARE)
					frame = SignatureHelper.CARD_TYPE_ATTACK_RARE;
				else if (this.rarity == CardRarity.UNCOMMON)
					frame = SignatureHelper.CARD_TYPE_ATTACK_UNCOMMON;
				else
					frame = SignatureHelper.CARD_TYPE_ATTACK_COMMON;
			}
			else if (this.type == CardType.POWER) {
				if (this.rarity == CardRarity.RARE)
					frame = SignatureHelper.CARD_TYPE_POWER_RARE;
				else if (this.rarity == CardRarity.UNCOMMON)
					frame = SignatureHelper.CARD_TYPE_POWER_UNCOMMON;
				else
					frame = SignatureHelper.CARD_TYPE_POWER_COMMON;
			}
			else {
				if (this.rarity == CardRarity.RARE)
					frame = SignatureHelper.CARD_TYPE_SKILL_RARE;
				else if (this.rarity == CardRarity.UNCOMMON)
					frame = SignatureHelper.CARD_TYPE_SKILL_UNCOMMON;
				else
					frame = SignatureHelper.CARD_TYPE_SKILL_COMMON;
			}

			this.renderHelper(sb, this.getRenderColor(), frame, x, y);
		}
		else
			SpireSuper.call(sb, x, y);
	}

	@SpireOverride
	protected void renderBannerImage(SpriteBatch sb, float x, float y) {
		if (!SignatureHelper.shouldUseSignature(this.cardID))
			SpireSuper.call(sb, x, y);
	}

	@SpireOverride
	protected void renderType(SpriteBatch sb) {
		if (!SignatureHelper.shouldUseSignature(this.cardID)) {
			SpireSuper.call(sb);
			return;
		}

		String text;
		if (this.type == CardType.ATTACK)
			text = AbstractCard.TEXT[0];
		else if (this.type == CardType.SKILL)
			text = AbstractCard.TEXT[1];
		else if (this.type == CardType.POWER)
			text = AbstractCard.TEXT[2];
		else if (this.type == CardType.CURSE)
			text = AbstractCard.TEXT[3];
		else if (this.type == CardType.STATUS)
			text = AbstractCard.TEXT[7];
		else
			text = AbstractCard.TEXT[5];

		BitmapFont font = FontHelper.cardTypeFont;
		font.getData().setScale(this.drawScale);
		this.getTypeColor().a = this.getRenderColor().a;
		FontHelper.renderRotatedText(sb, font, text, this.current_x,
				this.current_y - 195.0F * this.drawScale * Settings.scale,
				0.0F,
				-1.0F * this.drawScale * Settings.scale,
				this.angle, false, this.getTypeColor());
	}
}
