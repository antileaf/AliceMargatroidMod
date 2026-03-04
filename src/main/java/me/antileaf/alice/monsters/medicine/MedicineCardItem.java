package me.antileaf.alice.monsters.medicine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import me.antileaf.alice.action.medicine.MedicineApplyPoisonAction;
import me.antileaf.alice.action.medicine.MedicineApplyPoisonToAllAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.medicine.MedicineCripplingPoison;
import me.antileaf.alice.cards.medicine.MedicineEnvenom;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.powers.medicine.*;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.alice.utils.AliceImageMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MedicineCardItem implements Disposable {
	private static final Logger logger = LogManager.getLogger(MedicineCardItem.class);
	
	public AbstractCard card;
	public CardIntent intent;
	
	public boolean hidden = false;
	
	private float intentParticleTimer = 0.0F;
	private float intentAngle = 0.0F;
//	private float alpha = 1.0F;
	private ArrayList<AbstractGameEffect> intentVfx = new ArrayList<>();
	
	public MedicineCardItem(AbstractCard card) {
		this.card = card;
		this.intent = CardIntent.from(card);
	}
	
	public void update(float x, float y, float scale, boolean hovered) {
		this.card.target_x = x;
		this.card.target_y = y;
		this.card.targetAngle = 0.0F;
		this.card.targetDrawScale = hovered ? 0.9F : 0.5F;
		
		this.card.update();
		
		if (!this.hidden) {
			this.card.updateHoverLogic();
			this.card.targetDrawScale = hovered ? 0.9F : 0.5F;
		}
		else
			this.card.hb.hovered = false;
		
		this.updateIntentVFX();
	}
	
	private float getIntentY() {
		return this.card.current_y + this.card.hb.height / 2.0F + 32.0F * Settings.scale;
	}
	
	public void updateIntentVFX() {
		float y = this.getIntentY() + 64.0F * Settings.scale *
				((this.intent.damage != -1 ? 1 : 0) + (this.intent.poison != -1 ? 1 : 0));
		
		if (this.intent.buff) {
			this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
			if (this.intentParticleTimer < 0.0F) {
				this.intentParticleTimer = 0.1F;
				this.intentVfx.add(new BuffParticleEffect(this.card.current_x, y));
			}
		}
		else if (this.intent.debuff || this.intent.strongDebuff || this.intent.poison != -1) {
			this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
			if (this.intentParticleTimer < 0.0F) {
				this.intentParticleTimer = 1.0F;
				
				if (this.intent.debuff || this.intent.strongDebuff)
					this.intentVfx.add(new DebuffParticleEffect(this.card.current_x, y));
				
				if (this.intent.poison != -1)
					this.intentVfx.add(new DebuffParticleEffect(this.card.current_x, this.getIntentY()));
			}
		}
		
		ArrayList<AbstractGameEffect> toRemove = new ArrayList<>();
		for (AbstractGameEffect vfx : this.intentVfx) {
			vfx.update();
			if (vfx.isDone)
				toRemove.add(vfx);
		}
		
		for (AbstractGameEffect vfx : toRemove) {
			this.intentVfx.remove(vfx);
			vfx.dispose();
		}
	}
	
	public void render(SpriteBatch sb, boolean hovered) {
		if (!this.hidden) {
			if (hovered)
				this.card.renderHoverShadow(sb);
				
			this.card.render(sb);
			
			if (hovered)
				TipHelper.renderTipForCard(this.card, sb, this.card.keywords);
		}
	}
	
	private void draw(SpriteBatch sb, Texture texture, float x, float y, float angle) {
		sb.draw(texture, x - texture.getWidth() / 2.0F, y - texture.getHeight() / 2.0F,
				texture.getWidth() / 2.0F, texture.getHeight() / 2.0F,
				texture.getWidth(), texture.getHeight(),
				Settings.scale, Settings.scale,
				angle,
				0, 0,
				texture.getWidth(), texture.getHeight(),
				false, false);
	}
	
	private Texture getAttackIntent() {
		int total = (this.intent.multiAmt != -1 ? this.intent.multiAmt : 1) * this.intent.damage;
		Texture attackIntent;
		if (total < 5)
			attackIntent = ImageMaster.INTENT_ATK_1;
		else if (total < 10)
			attackIntent = ImageMaster.INTENT_ATK_2;
		else if (total < 15)
			attackIntent = ImageMaster.INTENT_ATK_3;
		else if (total < 20)
			attackIntent = ImageMaster.INTENT_ATK_4;
		else if (total < 25)
			attackIntent = ImageMaster.INTENT_ATK_5;
		else if (total < 30)
			attackIntent = ImageMaster.INTENT_ATK_6;
		else
			attackIntent = ImageMaster.INTENT_ATK_7;
		return attackIntent;
	}
	
	public void renderIntent(SpriteBatch sb) {
		if (this.intent.debuff || this.intent.strongDebuff)
			this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
		else
			this.intentAngle = 0.0F;
		
		if (!this.hidden) {
			Color intentColor = Color.WHITE.cpy();
//			intentColor.a = this.alpha;
			sb.setColor(intentColor);
			
			for (AbstractGameEffect vfx : this.intentVfx)
				vfx.render(sb);
			
			sb.setColor(intentColor);
			
			float y = this.getIntentY() + 64.0F * Settings.scale *
					((this.intent.buff ? 1 : 0) + (this.intent.debuff || this.intent.strongDebuff ? 1 : 0) +
							(this.intent.damage != -1 ? 1 : 0) + (this.intent.poison != -1 ? 1 : 0));
			
			if (this.intent.debuff || this.intent.strongDebuff) {
				y -= 64.0F * Settings.scale;
				
				this.draw(sb, ImageMaster.INTENT_DEBUFF_L,
						this.card.current_x, y, this.intentAngle);
			}
			
			if (this.intent.buff) {
				y -= 64.0F * Settings.scale;
				
				this.draw(sb, ImageMaster.INTENT_BUFF_L,
						this.card.current_x, y, 0.0F);
			}
			
			if (this.intent.damage != -1) {
				y -= 64.0F * Settings.scale;
				
				this.draw(sb, this.getAttackIntent(),
						this.card.current_x, y, 0.0F);
				
				String str = Integer.toString(this.intent.damage);
				if (this.intent.multiAmt != -1)
					str = str + "x" + this.intent.multiAmt;
				
				FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, str,
						this.card.current_x - 30.0F * Settings.scale,
						y - 12.0F * Settings.scale, intentColor);
			}
			
			if (this.intent.poison != -1) {
				y -= 64.0F * Settings.scale;
				
				this.draw(sb, AliceImageMaster.INTENT_POISON,
						this.card.current_x, y, 0.0F);
				
				String str;
				if (this.intent.poison >= 0) {
					str = Integer.toString(this.intent.poison);
					if (this.intent.poison > 1)
						str = str + "x" + this.intent.poison;
				}
				else
					str = "x" + (-this.intent.poison); // Catalyst
				
				FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, str,
						this.card.current_x - 30.0F * Settings.scale,
						y - 12.0F * Settings.scale, intentColor);
			}
		}
	}
	
	public void applyPowers(MedicineMelancholy medicine) {
		if (this.card.baseDamage != -1) {
			this.card.damage = medicine.calculateCardDamage(this.card.baseDamage);
			this.card.isDamageModified = this.card.damage != this.card.baseDamage;
			
			this.intent.damage = this.card.damage;
			
			if (this.card instanceof Bane) {
				if (medicine.bane(this.intent.index))
					this.intent.multiAmt = 2;
				else
					this.intent.multiAmt = -1;
			}
		}
	}
	
	public void play(MedicineMelancholy medicine) {
		if (this.card instanceof PoisonedStab) { // 带毒刺击
			AliceHelper.addActionToBuffer(new DamageAction(AbstractDungeon.player,
					new MedicineDamageInfo(this.intent.index, medicine, this.card.damage),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			
			AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(this.intent.index,
					medicine, this.card.magicNumber));
		}
		else if (this.card instanceof Bane) { // 灾祸
			AliceHelper.addActionToBuffer(new DamageAction(AbstractDungeon.player,
					new MedicineDamageInfo(this.intent.index, medicine, this.card.damage),
					AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
			
			if (medicine.bane(this.intent.index))
				AliceHelper.addActionToBuffer(new DamageAction(AbstractDungeon.player,
						new MedicineDamageInfo(this.intent.index, medicine, this.card.damage),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		}
		else if (this.card instanceof DeadlyPoison) { // 致命毒药
			AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(this.intent.index,
					medicine, this.card.magicNumber));
		}
		else if (this.card instanceof PiercingWail) { // 尖啸
			AliceHelper.addActionToBuffer(new AnonymousAction(() -> {
				if (!AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID))
					AliceHelper.addToTop(new ApplyPowerAction(AbstractDungeon.player,
							medicine, new GainStrengthPower(AbstractDungeon.player, this.card.magicNumber)));
				
				AliceHelper.addToTop(new ApplyPowerAction(AbstractDungeon.player,
						medicine, new StrengthPower(AbstractDungeon.player, -this.card.magicNumber)));
			}));
		}
		else if (this.card instanceof Outmaneuver) { // 抢占先机
			AliceHelper.addActionToBuffer(new ApplyPowerAction(medicine, medicine,
					new MedicineOutmaneuverPower(medicine, this.card.upgraded ? 3 : 2)));
		}
		else if (this.card instanceof CripplingPoison) { // 致残毒云
			AliceHelper.addActionToBuffer(new MedicineApplyPoisonToAllAction(medicine, this.card.magicNumber));
			AliceHelper.addActionToBuffer(new ApplyPowerAction(AbstractDungeon.player, medicine,
					new WeakPower(AbstractDungeon.player, 2, true)));
			
			if (this.card instanceof MedicineCripplingPoison &&
					((MedicineCripplingPoison) this.card).upgradedCount >= 2)
				AliceHelper.addActionToBuffer(new ApplyPowerAction(AbstractDungeon.player, medicine,
						new VulnerablePower(AbstractDungeon.player, 2, true)));
		}
		else if (this.card instanceof BouncingFlask) { // 弹跳药瓶
			for (int i = 0; i < this.intent.multiAmt; i++) {
				int index = this.intent.indices[i];
				AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(index, medicine, 3));
			}
		}
		else if (this.card instanceof CorpseExplosion) { // 尸体爆炸
			AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(this.intent.index, medicine,
					this.card.magicNumber));
			// TODO: corpse explosion
		}
		else if (this.card instanceof Catalyst) { // 催化剂
			int maxPoison = 0, index = -1;
			for (int i = 0; i < DollManager.MAX_DOLL_SLOTS; i++) {
				AbstractDoll doll = DollManager.get().getDolls().get(i);
				if (doll != null && !(doll instanceof EmptyDollSlot) && doll.poison > maxPoison) {
					maxPoison = doll.poison;
					index = i;
				}
			}
			
			if (index != -1) {
				AbstractDoll doll = DollManager.get().getDolls().get(index);
				if (doll.poison > 0) {
					AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(index, medicine,
							doll.poison * this.card.magicNumber));
				}
			}
			else if (AbstractDungeon.player.hasPower(MedicinePoisonPower.POWER_ID))
				AliceHelper.addActionToBuffer(new MedicineApplyPoisonAction(-1, medicine,
						AbstractDungeon.player.getPower(MedicinePoisonPower.POWER_ID).amount *
								this.card.magicNumber));
		}
		else if (this.card instanceof InfiniteBlades) { // 无限刀刃
			AliceHelper.addActionToBuffer(new ApplyPowerAction(medicine, medicine,
					new MedicineInfiniteBladesPower(medicine, this.card.magicNumber)));
		}
		else if (this.card instanceof Caltrops) { // 铁蒺藜
			AliceHelper.addActionToBuffer(new ApplyPowerAction(medicine, medicine,
					new ThornsPower(medicine, this.card.magicNumber)));
		}
		else if (this.card instanceof NoxiousFumes) { // 毒雾
			AliceHelper.addActionToBuffer(new ApplyPowerAction(medicine, medicine,
					new MedicineNoxiousFumesPower(medicine, this.card.magicNumber)));
		}
		else if (this.card instanceof Envenom) { // 涂毒
			int amount = 1;
			if (this.card instanceof MedicineEnvenom && ((MedicineEnvenom) this.card).upgradedCount >= 2)
				amount = 2;
			
			AliceHelper.addActionToBuffer(new ApplyPowerAction(medicine, medicine,
					new MedicineEnvenomPower(medicine, amount)));
		}
		else if (this.card instanceof AfterImage) { // 余像
			AliceHelper.addActionToBuffer(new ApplyPowerAction(medicine, medicine,
					new MedicineAfterImagePower(medicine, this.card.magicNumber)));
		}
		else if (this.card instanceof PhantasmalKiller) { // 幻影杀手
			AliceHelper.addActionToBuffer(new ApplyPowerAction(medicine, medicine,
					new PhantasmalPower(medicine, 1)));
		}
		else if (this.card instanceof Shiv) { // 小刀
			AliceHelper.addActionToBuffer(new DamageAction(AbstractDungeon.player,
					new MedicineDamageInfo(this.intent.index, medicine, this.card.damage),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}
		else {
			logger.warn("play(): Unknown card for Medicine Melancholy: {}", this.card.cardID);
		}
		
		if (medicine.hasPower(MedicineAfterImagePower.POWER_ID))
			medicine.getPower(MedicineAfterImagePower.POWER_ID).onSpecificTrigger();
	}
	
	public static class CardIntent {
		public int damage = -1;
		public int multiAmt = -1;
		public int poison = -1; // -2/-3 means Catalyst(+)
		public int index = -1; // 0~6
		public int[] indices = null; // for Bouncing Flask only
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
		
		public CardIntent randomIndex() {
			this.index = AbstractDungeon.monsterRng.random(0, 6);
			return this;
		}
		
		public CardIntent indices(int[] indices) {
			this.indices = indices;
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
			return this.damage == -1 && this.poison == -1
					&& !this.buff && !this.debuff && !this.strongDebuff;
		}
		
		public static CardIntent from(AbstractCard card) {
			if (card instanceof PoisonedStab) {
				return new CardIntent()
						.damage(card.damage)
						.poison(card.magicNumber)
						.randomIndex();
			}
			else if (card instanceof Bane) {
				return new CardIntent()
						.damage(card.damage)
						.randomIndex();
			}
			else if (card instanceof DeadlyPoison) {
				return new CardIntent()
						.poison(card.magicNumber)
						.randomIndex();
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
				int[] indices = new int[card.magicNumber];
				for (int i = 0; i < card.magicNumber; i++)
					indices[i] = AbstractDungeon.monsterRng.random(0, 6);
				
				return new CardIntent()
						.poison(3)
						.multiAmt(card.magicNumber)
						.indices(indices);
			}
			else if (card instanceof CorpseExplosion) {
				return new CardIntent()
						.poison(card.magicNumber)
						.debuff()
						.randomIndex();
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
			else if (card instanceof Shiv) {
				return new CardIntent()
						.damage(card.damage)
						.randomIndex();
			}
			else {
				logger.warn("Unknown card for Medicine Melancholy: {}", card.cardID);
				return new CardIntent();
			}
		}
	}
	
	@Override
	public void dispose() {
		for (AbstractGameEffect vfx : this.intentVfx)
			vfx.dispose();
	}
}
