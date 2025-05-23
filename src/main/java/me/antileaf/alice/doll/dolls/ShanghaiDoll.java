package me.antileaf.alice.doll.dolls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.doll.DollDamageAction;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollDamageInfo;
import me.antileaf.alice.doll.enums.DollAmountTime;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.relics.SuspiciousCard;
import me.antileaf.alice.strings.AliceDollStrings;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.alice.utils.AliceImageMaster;

import java.util.Arrays;

public class ShanghaiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = ShanghaiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 4;
	public static final int PASSIVE_AMOUNT = 2;
	public static final int ACT_AMOUNT = 4;
	
	public static final float CHARGE_WIDTH = 18.0F * Settings.scale;

	@Deprecated
	public int charge = 0;
	
	public ShanghaiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.DAMAGE;

		this.tipImg = ImageMaster.INTENT_ATK_TIP_3;
	}
	
	@Override
	public AliceDollStrings getDollStrings() {
		return dollStrings;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public int getBaseHP() {
		return MAX_HP;
	}
	
	private void drawCharge(SpriteBatch sb, float x, float y) {
		Texture img = AliceImageMaster.SHANGHAI_DOLL_CHARGE;
		
		float scale = CHARGE_WIDTH / img.getWidth();
		
		sb.draw(
				img,
				x - img.getWidth() / 2.0F,
				y - img.getHeight() / 2.0F,
				img.getWidth() / 2.0F,
				img.getHeight() / 2.0F,
				(float) img.getWidth(),
				(float) img.getHeight(),
				scale,
				scale,
				0.0F,
				0,
				0,
				img.getWidth(),
				img.getHeight(),
				false,
				false
		);
	}
	
	@Override
	public void renderImage(SpriteBatch sb) {
		super.renderImage(sb);
		
		if (this.charge > 0) {
			float w = CHARGE_WIDTH;
			float y = this.cY + this.getRenderYOffset() - 26.0F * Settings.scale;
			
			if (this.charge <= 5) {
				float x = this.getDrawCX() - w * (this.charge - 1) / 2.0F;
				
				for (int i = 0; i < this.charge; i++)
					this.drawCharge(sb, x + i * w, y);
			}
			else {
				String text = Integer.toString(this.charge);
				
				FontHelper.renderFontCentered(sb,
						FontHelper.cardEnergyFont_L,
						text,
						this.getDrawCX() - w / 2.0F,
						y,
						Settings.CREAM_COLOR,
						this.passiveFontScale
				);
				
				this.drawCharge(sb, this.getDrawCX() + w * text.length() / 2.0F, y);
			}
		}
	}

	@Override
	public void postSpawn() {
		AliceHelper.addActionToBuffer(new DollActAction(this));
	}
	
	@Override
	public void onAct(DollActModifier modifier) {
		this.highlightActValue();
		
//		int tmpBase = this.baseActAmount;
//		this.baseActAmount += this.charge * this.passiveAmount;
		this.applyPower();
//		this.charge = 0;

		int totalDamage = 0;
		
		if (!AbstractDungeon.player.hasRelic(SuspiciousCard.ID)) {
			AbstractMonster m = modifier.preferredTarget != null &&
					!modifier.preferredTarget.isDeadOrEscaped() ?
					modifier.preferredTarget : AliceHelper.getMonsterWithLeastHP();
			
			if (m != null) {
				DollDamageInfo info = new DollDamageInfo(this.actAmount, this,
						DollAmountType.DAMAGE, DollAmountTime.ACT, modifier);

				totalDamage = info.output;

				AliceHelper.addActionToBuffer(new DollDamageAction(m, info,
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
			}
		}
		else { // 有人偶动力学
			if (AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> !m.isDeadOrEscaped())
					.count() > 1)
				AbstractDungeon.player.getRelic(SuspiciousCard.ID).flash();

			int[] matrix = DollDamageInfo.createDamageMatrix(
					this.actAmount, this, this.actAmountType,
					DollAmountTime.ACT, modifier);

			totalDamage = Arrays.stream(matrix).sum();
			
			AliceHelper.addActionToBuffer(new DamageAllEnemiesAction(
					AbstractDungeon.player,
					matrix,
					DamageInfo.DamageType.THORNS,
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
					true
			));
		}
		
//		this.baseActAmount = tmpBase;
//		this.applyPower();

		if (modifier.theSetup)
			AliceHelper.addActionToBuffer(new DollGainBlockAction(this, totalDamage));
	}
	
	@Override
	public void onStartOfTurn() {
//		this.charge += 1;
//		this.applyPower();
	}
	
	// The logic of Strength is implemented in AbstractDoll.applyPowers().
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new ShanghaiDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.PASSIVE_DESCRIPTION, this.coloredPassiveAmount());
		if (this.charge > 0)
			this.passiveDescription += String.format(dollStrings.EXTENDED_DESCRIPTION[0], this.charge);
		
		this.actDescription = String.format(dollStrings.ACT_DESCRIPTION, this.coloredActAmount());
	}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 6.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 26.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new ShanghaiDoll()).desc();
	}
}
