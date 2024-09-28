package rs.antileaf.alice.doll.dolls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollDamageAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.relics.SuspiciousCard;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceImageMaster;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ShanghaiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = ShanghaiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 4;
	public static final int PASSIVE_AMOUNT = 2;
	public static final int ACT_AMOUNT = 4;
	
	public static final float CHARGE_WIDTH = 18.0F * Settings.scale;
	
	public int charge = 0;
	
	public ShanghaiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.DAMAGE;
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
	public void onAct() {
		this.highlightActValue();
		
		int tmpBase = this.baseActAmount;
		this.baseActAmount += this.charge * this.passiveAmount;
		this.applyPower();
		this.charge = 0;
		
		if (!AbstractDungeon.player.hasRelic(SuspiciousCard.ID)) {
			AbstractMonster m = AliceSpireKit.getMonsterWithLeastHP();
			
			if (m != null) {
				AliceSpireKit.addActionToBuffer(new DollDamageAction(m,
						new DollDamageInfo(this.actAmount, this,
								DollAmountType.DAMAGE, DollAmountTime.ACT),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
			}
		}
		else {
			if (AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> !m.isDeadOrEscaped())
					.count() > 1)
				AbstractDungeon.player.getRelic(SuspiciousCard.ID).flash();
			
			AliceSpireKit.addActionToBuffer(new DamageAllEnemiesAction(
					AbstractDungeon.player,
					DollDamageInfo.createDamageMatrix(
							this.actAmount,
							this,
							this.actAmountType,
							DollAmountTime.ACT),
					DamageInfo.DamageType.THORNS,
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
					true
			));
		}
		
		this.baseActAmount = tmpBase;
		this.applyPower();
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
