package me.antileaf.alice.cards;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import me.antileaf.alice.cards.utils.AbstractSecondaryVariablesCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.strings.AliceCardNoteStrings;
import me.antileaf.alice.strings.AliceCardSignStrings;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.targeting.handlers.*;
import me.antileaf.alice.utils.AliceConfigHelper;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static me.antileaf.alice.AliceMargatroidMod.ALICE_PUPPETEER_FLAVOR;

public abstract class AbstractAliceCard extends AbstractSecondaryVariablesCard {
	private static final Logger logger = LogManager.getLogger(AbstractAliceCard.class.getName());
	
	protected String cardSign = null;
	public final ArrayList<AliceTargetIcon> targetIcons = new ArrayList<>();

	public boolean dollTarget = false;
	public boolean hardCodedForbiddenMagic = false;
	public boolean isCommandCard = false;
	
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

//		if (this.dollTarget && AliceHelper.isInBattle() && AbstractDungeon.player.hasPower(ForbiddenMagicPower.POWER_ID)) {
//			PowerStrings ps = CardCrawlGame.languagePack.getPowerStrings(ForbiddenMagicPower.POWER_ID);
//			result.add(new TooltipInfo(
//					ps.DESCRIPTIONS[1],
//					String.format(ps.DESCRIPTIONS[2],
//							AbstractDungeon.player.getPower(ForbiddenMagicPower.POWER_ID).amount)
//			));
//		}
		
		return result;
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
			if (!DollManager.get().hasDoll() /* && DollTargeting.getTarget(this) == null */) {
				this.cantUseMessage = CardCrawlGame.languagePack
						.getUIString(AliceHelper.makeID("NoDollDialog")).TEXT[0];
				logger.debug("cantUseMessage: {}", this.cantUseMessage);

				// TODO: There may be a bug in stslib. Fix it later.
				return false;
			}
		}
		
		return true;
	}

	public void aliceTriggerAtStartOfTurn() {}
	
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

	public void drawOnCard(SpriteBatch sb, Texture img, float xPos, float yPos, Vector2 offset,
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
		if (SignatureHelper.shouldUseSignature(this) && this.getSignatureTransparency() <= 0.0F)
			return;

		this.renderTargetIcons(sb, this.current_x, this.current_y,
				X_OFFSET, Y_OFFSET,
				AliceTargetIcon.WIDTH, AliceTargetIcon.WIDTH,
				 SignatureHelper.shouldUseSignature(this) ? this.getSignatureTransparency() : 1.0F,
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

//		if (AliceHelper.isInBattle() && this.dollTarget &&
//				AbstractDungeon.player.hasPower(ForbiddenMagicPower.POWER_ID)) {
//			this.drawOnCard(sb,
//					ForbiddenMagicPower.ICON,
//					this.current_x, this.current_y,
//					new Vector2(X_OFFSET, Y_OFFSET * 2.0F),
//					128.0F, 128.0F,
//					1.0F, this.drawScale, 0.6F);
//		}
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
}
