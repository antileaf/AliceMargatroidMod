package rs.antileaf.alice.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
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
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;
import java.util.List;

import static rs.antileaf.alice.AliceMargatroidMod.ALICE_PUPPETEER_FLAVOR;

public abstract class AbstractAliceCard extends CustomCard {
//	protected static final CardStrings cardStrings =
//			CardCrawlGame.languagePack.getCardStrings("AbstractAliceCard");
	protected static final Color CYAN_COLOR = new Color(0f, 204f / 255f, 0f, 1f);
	
	protected String cardSign = null;
	public final ArrayList<AliceTargetIcon> targetIcons = new ArrayList<>();
//	public Hitbox iconsHb;
//	public ArrayList<PowerTip> iconsTips;

	public boolean cantBePlayed = false;
//	public boolean isSupplement = false;
	
	public int tempHP = 0;
	public int baseTempHP = 0;
	
	public int secondaryMagicNumber = -1;
	public int baseSecondaryMagicNumber = -1;
	public boolean upgradedSecondaryMagicNumber = false;
	public boolean isSecondaryMagicNumberModified = false;
	
	public int secondaryDamage = -1;
	public int baseSecondaryDamage = -1;
	public boolean upgradedSecondaryDamage = false;
	public boolean isSecondaryDamageModified = false;

	boolean isLoli = false;
	
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
						(type == CardType.ATTACK ? AliceSpireKit.getCardImgFilePath("Attack") :
								type == CardType.SKILL ? AliceSpireKit.getCardImgFilePath("Skill") :
										AliceSpireKit.getCardImgFilePath("Power")),
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
		
		return result;
	}

	@Override
	public void triggerWhenDrawn() {
//		if (this.isSupplement)
//			this.addToTop(new DrawCardAction(1));

		super.triggerWhenDrawn();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		
		if (this.baseSecondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalDamageModified = this.isDamageModified;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);
			
			this.baseDamage = this.baseSecondaryDamage;
			super.applyPowers();
			this.isSecondaryDamageModified = this.secondaryDamage != this.baseSecondaryDamage;
			this.secondaryDamage = this.damage;
			
			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isDamageModified = originalDamageModified;
			this.multiDamage = originalMultiDamage;
		}
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
	
	@Override
	public void resetAttributes() {
		super.resetAttributes();
		
		this.tempHP = this.baseTempHP;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
		this.isSecondaryMagicNumberModified = false;
		this.secondaryDamage = this.baseSecondaryDamage;
		this.isSecondaryDamageModified = false;
	}
	
	@Override
	public void displayUpgrades() {
		super.displayUpgrades();
		
		if (this.upgradedSecondaryMagicNumber) {
			this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
			this.isSecondaryMagicNumberModified = true;
		}
		
		if (this.upgradedSecondaryDamage) {
			this.secondaryDamage = this.baseSecondaryDamage;
			this.isSecondaryDamageModified = true;
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
		if (this.cantBePlayed || !super.canUse(p, m))
			return false;
		
		if (this.target == CardTargetEnum.DOLL) {
			if (!DollManager.get().hasDoll() && DollTargeting.getTarget(this) == null) {
//				this.cantUseMessage = CardCrawlGame.languagePack
//						.getUIString("AliceNoDollDialog").TEXT[0];
//				AliceSpireKit.log("cantUseMessage: " + this.cantUseMessage);
				// TODO: There may be a bug in stslib. Fix it later.
				return false;
			}
		}
		
		return true;
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractAliceCard card = (AbstractAliceCard) super.makeStatEquivalentCopy();

		card.cantBePlayed = this.cantBePlayed;
//		card.isSupplement = this.isSupplement;

		card.tempHP = this.tempHP;
		card.baseTempHP = this.baseTempHP;
		
		card.secondaryMagicNumber = this.secondaryMagicNumber;
		card.baseSecondaryMagicNumber = this.baseSecondaryMagicNumber;
		
		card.secondaryDamage = this.secondaryDamage;
		card.baseSecondaryDamage = this.baseSecondaryDamage;

		return card;
	}
	
	public void aliceTriggerAtStartOfTurn() {}

	public void triggerOnLeaveHand(boolean isExhaust, boolean isEndOfTurn) {}

	public void triggerOnLeaveHand(boolean isExhaust) {
		this.triggerOnLeaveHand(isExhaust, false);
	}

	@Override
	public void triggerOnExhaust() {
		this.triggerOnLeaveHand(true);
		super.triggerOnExhaust();
	}

	@Override
	public void triggerOnManualDiscard() {
		this.triggerOnLeaveHand(false, false);
		super.triggerOnManualDiscard();
	}

	@Override
	public void triggerOnEndOfPlayerTurn() {
		if (!this.retain)
			this.triggerOnLeaveHand(false, true);
		
		super.triggerOnEndOfPlayerTurn();
	}
	
	public void upgradeSecondaryMagicNumber(int amount) {
		this.baseSecondaryMagicNumber += amount;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
		this.upgradedSecondaryMagicNumber = true;
	}
	
	public void upgradeSecondaryDamage(int amount) {
		this.baseSecondaryDamage += amount;
		this.secondaryDamage = this.baseSecondaryDamage;
		this.upgradedSecondaryDamage = true;
	}

	public void addActionsToTop(AbstractGameAction... actions) {
		AliceSpireKit.addActionsToTop(actions);
	}

	public String bracketedName() {
		return "(" + this.name + ")";
	}

//	protected void refreshIconsTip() {
//		this.iconsTips.clear();
//		if (!this.targetIcons.isEmpty())
//			this.iconsTips.add(AliceTargetIconTipHelper.generateTip(this.targetIcons));
//	}

//	protected void updateIconsHb() {
//		if (this.targetIcons.isEmpty()) {
//			this.iconsHb.move(-100.0F, -100.0F);
//			this.iconsHb.resize(0.0F, 0.0F);
//		}
//		else {
//			float hbWidth = AliceTargetIcon.WIDTH * this.drawScale * Settings.scale;
//			float hbHeight = AliceTargetIcon.WIDTH * this.drawScale * Settings.scale * this.targetIcons.size();
//			float x = this.current_x + X_OFFSET * this.drawScale * Settings.scale;
//			float y = this.current_y + (Y_OFFSET - AliceTargetIcon.WIDTH / 2.0F * (this.targetIcons.size() - 1))
//					* this.drawScale * Settings.scale;
//
//			this.iconsHb.move(x, y);
//			this.iconsHb.resize(hbWidth, hbHeight);
//		}
//
//		this.iconsHb.update();
//	}

//	@Override
//	public void update() {
//		super.update();
//		this.updateIconsHb();
//	}
	
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

	private void drawOnCard(SpriteBatch sb, Texture img, float xPos, float yPos, Vector2 offset, float width, float height, float alpha, float drawScale, float scaleModifier) {
		if (this.angle != 0.0F)
			offset.rotate(this.angle);

		offset.scl(Settings.scale * drawScale);

		float drawX = xPos + offset.x, drawY = yPos + offset.y;
		float scale = drawScale * Settings.scale * scaleModifier;

		Color backup = sb.getColor();
		sb.setColor(1.0F, 1.0F, 1.0F, alpha);
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
								  float width, float height, float drawScale) {
		if (this.isFlipped || this.isLocked || this.transparency <= 0.0F)
			return;

		float xOffset = xOffsetBase, yOffset = yOffsetBase;

		for (AliceTargetIcon icon : this.targetIcons) {
			this.drawOnCard(sb, icon.bg, xPos, yPos, new Vector2(xOffset, yOffset), width, height, this.transparency, drawScale, AliceTargetIcon.BG_SCALE);
			this.drawOnCard(sb, icon.img, xPos, yPos, new Vector2(xOffset, yOffset), width, height, this.transparency, drawScale, icon.scaleModifier);

//			xOffset += width;
			yOffset -= height;
		}
	}

	private static final float X_OFFSET = 134.0F, Y_OFFSET = 100.0F;

	public void renderTargetIcons(SpriteBatch sb) {
//		this.renderTargetIcons(sb, this.current_x, this.current_y, 48.0F, -22.0F,
//				AliceTargetIcon.WIDTH, AliceTargetIcon.WIDTH, this.drawScale);
		this.renderTargetIcons(sb, this.current_x, this.current_y, X_OFFSET, Y_OFFSET,
				AliceTargetIcon.WIDTH, AliceTargetIcon.WIDTH, this.drawScale);
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
	
	public void renderInLibrary(SpriteBatch sb) {
		super.renderInLibrary(sb);
		if (!SingleCardViewPopup.isViewingUpgrade || !this.isSeen || this.isLocked) {
			this.renderCardSign(sb);
//			this.renderTargetIcons(sb);

//			this.updateIconsHb();
//			this.iconsHb.render(sb);
		}
	}

	public boolean convertible() {
		return !this.isLoli;
	}

	public boolean isLoli() {
		return this.isLoli;
	}

	public void convertToLoli() {
		if (this.convertible())
			this.isLoli = true;
	}
}
