package me.antileaf.alice.monsters.medicine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import me.antileaf.alice.action.medicine.MedicineApplyPoisonAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.medicine.MedicineCripplingPoison;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MedicineCardItem {
	private static final Logger logger = LogManager.getLogger(MedicineCardItem.class);
	
	public AbstractCard card;
	public CardIntent intent;
	
	public boolean hidden = false;
	
	private float intentParticleTimer = 0.0F;
	private float intentAngle = 0.0F;
	private float alpha = 0.0F;
	private ArrayList<AbstractGameEffect> intentVfx = new ArrayList<>();
	
	public MedicineCardItem(AbstractCard card) {
		this.card = card;
		this.intent = CardIntent.from(card);
	}
	
	public void update(float x, float y, float scale) {
		this.card.target_x = x;
		this.card.target_y = y;
		this.card.targetAngle = 0.0F;
		this.card.targetDrawScale = scale;
		
		this.card.update();
		
		if (!this.hidden)
			this.card.updateHoverLogic();
		else
			this.card.hb.hovered = false;
		
		this.updateIntentVFX();
	}
	
	private float getIntentY() {
		return this.card.current_y + this.card.hb.height / 2.0F + 32.0F * Settings.scale;
	}
	
	public void updateIntentVFX() {
		if (this.intent.buff) {
			this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
			if (this.intentParticleTimer < 0.0F) {
				this.intentParticleTimer = 0.1F;
				this.intentVfx.add(new BuffParticleEffect(this.card.current_x, this.getIntentY()));
			}
		}
		else if (this.intent.debuff || this.intent.strongDebuff || this.intent.poison != -1) {
			this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
			if (this.intentParticleTimer < 0.0F) {
				this.intentParticleTimer = 1.0F;
				this.intentVfx.add(new DebuffParticleEffect(this.card.current_x, this.getIntentY()));
			}
		}
	}
	
	public void render(SpriteBatch sb, boolean hovered) {
		if (!this.hidden) {
			if (hovered) {
				this.card.renderHoverShadow(sb);
				TipHelper.renderTipForCard(this.card, sb, this.card.keywords);
			}
			
			this.card.render(sb);
		}
	}
	
	private void draw(SpriteBatch sb, Texture texture, float x, float y, float angle) {
		sb.draw(texture, x, y,
				texture.getWidth() / 2.0F, texture.getWidth() / 2.0F,
				texture.getWidth(), texture.getHeight(),
				Settings.scale * 1.2F, Settings.scale * 1.2F,
				angle,
				0, 0,
				texture.getWidth(), texture.getHeight(),
				false, false);
	}
	
	public void renderIntent(SpriteBatch sb) {
		if (this.intent.debuff || this.intent.strongDebuff)
			this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
		else
			this.intentAngle = 0.0F;
		
		if (!this.hidden) {
			Color intentColor = Color.WHITE.cpy();
			intentColor.a = this.alpha;
			sb.setColor(intentColor);
			
			float y = this.getIntentY();
			
			if (this.intent.debuff || this.intent.strongDebuff) {
				this.draw(sb, new Texture(""),
						this.card.current_x, y, this.intentAngle); // TODO: intent debuff icon
				
				y -= 32.0F * Settings.scale;
			}
			
			if (this.intent.buff) {
				this.draw(sb, new Texture(""),
						this.card.current_x, y, 0.0F); // TODO: intent buff icon
				
				y -= 32.0F * Settings.scale;
			}
			
			if (this.intent.damage != -1) {
				this.draw(sb, new Texture(""),
						this.card.current_x, y, 0.0F); // TODO: intent attack icon
				
				String str = Integer.toString(this.intent.damage);
				if (this.intent.multiAmt != -1)
					str = this.intent.multiAmt + "x" + str;
				
				FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, str,
						this.card.current_x - 30.0F * Settings.scale,
						y - 12.0F * Settings.scale, intentColor);
				
				y -= 32.0F * Settings.scale;
			}
			
			if (this.intent.poison != -1) {
				this.draw(sb, new Texture(""),
						this.card.current_x, y, 0.0F); // TODO: intent poison icon
				
				String str;
				if (this.intent.poison >= 0)
					str = Integer.toString(this.intent.poison);
				else
					str = "x" + (-this.intent.poison); // Catalyst
				
				FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, str,
						this.card.current_x - 30.0F * Settings.scale,
						y - 12.0F * Settings.scale, intentColor);
				
				y -= 32.0F * Settings.scale;
			}
			
		}
	}
	
	public void applyPowers(MedicineMelancholy medicine) {
		if (this.card.baseDamage != -1) {
			this.card.damage = medicine.calculateCardDamage(this.card.baseDamage);
			this.card.isDamageModified = this.card.damage != this.card.baseDamage;
			
			this.intent.damage = this.card.damage;
			
			if (this.card instanceof Bane) {
				if (medicine.bane())
					this.intent.multiAmt = 2;
				else
					this.intent.multiAmt = -1;
			}
		}
	}
	
	public void play(MedicineMelancholy medicine) {
		if (this.card instanceof PoisonedStab) {
			AliceHelper.addActionToBuffer(new DamageAction(AbstractDungeon.player,
					new DamageInfo(medicine, this.card.damage, DamageInfo.DamageType.NORMAL),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			
			AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(AbstractDungeon.player,
					medicine, this.card.magicNumber));
		}
		else if (this.card instanceof Bane) {
			AliceHelper.addActionToBuffer(new DamageAction(AbstractDungeon.player,
					new DamageInfo(medicine, this.card.damage, DamageInfo.DamageType.NORMAL),
					AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
			
			if (medicine.bane())
				AliceHelper.addActionToBuffer(new DamageAction(AbstractDungeon.player,
						new DamageInfo(medicine, this.card.damage, DamageInfo.DamageType.NORMAL),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		}
		else if (this.card instanceof DeadlyPoison) {
			AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(AbstractDungeon.player,
					medicine, this.card.magicNumber));
		}
		else if (this.card instanceof PiercingWail) {
			AliceHelper.addActionToBuffer(new ApplyPowerAction(AbstractDungeon.player,
					medicine, new StrengthPower(AbstractDungeon.player, -this.card.magicNumber)));
			
			AliceHelper.addActionToBuffer(new AnonymousAction(() -> {
				if (!AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID))
					AliceHelper.addToTop(new ApplyPowerAction(AbstractDungeon.player,
							medicine, new GainStrengthPower(AbstractDungeon.player, this.card.magicNumber)));
			}));
		}
		else if (this.card instanceof Outmaneuver) { // 抢占先机
		
		}
		else if (this.card instanceof CripplingPoison) {
			if (this.card instanceof MedicineCripplingPoison && ((MedicineCripplingPoison) this.card).upgradedCount >= 2) {
				AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(AbstractDungeon.player,
						medicine, this.card.magicNumber));
				
				AliceHelper.addActionToBuffer(new ApplyPowerAction(AbstractDungeon.player,
						medicine, new StrengthPower(AbstractDungeon.player, -this.card.magicNumber)));
				
				AliceHelper.addActionToBuffer(new AnonymousAction(() -> {
					if (!AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID))
						AliceHelper.addToTop(new ApplyPowerAction(AbstractDungeon.player,
								medicine, new GainStrengthPower(AbstractDungeon.player, this.card.magicNumber)));
				}));
			}
			else {
				AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(AbstractDungeon.player,
						medicine, this.card.magicNumber));
			}
		}
		else if (this.card instanceof BouncingFlask) {
		
		}
		else if (this.card instanceof Catalyst) {
		
		}
		else if (this.card instanceof InfiniteBlades) {
		
		}
		else if (this.card instanceof Caltrops) {
		
		}
		else if (this.card instanceof NoxiousFumes) {
		
		}
		else if (this.card instanceof Envenom) {
		
		}
		else if (this.card instanceof AfterImage) {
		
		}
		else if (this.card instanceof PhantasmalKiller) {
		
		}
		else {
			logger.warn("play(): Unknown card for Medicine Melancholy: {}", this.card.cardID);
		}
		
		AliceHelper.commitBuffer();
	}
	
	public static class CardIntent {
		public int damage = -1;
		public int multiAmt = -1;
		public int poison = -1; // -2/-3 means Catalyst(+)
		public boolean buff = false;
		public boolean debuff = false;
		public boolean strongDebuff = false;
		
		public CardIntent() {}
		
		public CardIntent damage(int damage) {
			this.damage = damage;
			return this;
		}
		
		public CardIntent multiAmt(int multiAmt) {
			this.multiAmt = multiAmt;
			return this;
		}
		
		public CardIntent poison(int poison) {
			this.poison = poison;
			return this;
		}
		
		public CardIntent buff() {
			this.buff = true;
			return this;
		}
		
		public CardIntent debuff() {
			this.debuff = true;
			return this;
		}
		
		public CardIntent strongDebuff() {
			this.strongDebuff = true;
			return this;
		}
		
		public boolean unknown() {
			return this.damage == -1 && this.multiAmt == -1 && this.poison == -1
					&& !this.buff && !this.debuff && !this.strongDebuff;
		}
		
		public static CardIntent from(AbstractCard card) {
			if (card instanceof PoisonedStab) {
				return new CardIntent().damage(card.damage)
						.poison(card.magicNumber);
			}
			else if (card instanceof Bane) {
				return new CardIntent().damage(card.damage)
						.multiAmt(2); // TODO
			}
			else if (card instanceof DeadlyPoison) {
				return new CardIntent().poison(card.magicNumber);
			}
			else if (card instanceof PiercingWail) {
				return new CardIntent().strongDebuff();
			}
			else if (card instanceof Outmaneuver) {
				return new CardIntent().buff();
			}
			else if (card instanceof CripplingPoison) {
				if (card instanceof MedicineCripplingPoison && ((MedicineCripplingPoison) card).upgradedCount >= 2)
					return new CardIntent().poison(card.magicNumber).strongDebuff();
				else
					return new CardIntent().poison(card.magicNumber).debuff();
			}
			else if (card instanceof BouncingFlask) {
				return new CardIntent().poison(3)
						.multiAmt(card.magicNumber);
			}
			else if (card instanceof Catalyst) {
				return new CardIntent().poison(!card.upgraded ? -2 : -3);
			}
			else if (card instanceof InfiniteBlades) {
				return new CardIntent().buff();
			}
			else if (card instanceof Caltrops) {
				return new CardIntent().buff();
			}
			else if (card instanceof NoxiousFumes) {
				return new CardIntent().buff();
			}
			else if (card instanceof Envenom) {
				return new CardIntent().buff();
			}
			else if (card instanceof AfterImage) {
				return new CardIntent().buff();
			}
			else if (card instanceof PhantasmalKiller) {
				return new CardIntent().buff();
			}
			else {
				logger.warn("Unknown card for Medicine Melancholy: {}", card.cardID);
				return new CardIntent();
			}
		}
	}
}
