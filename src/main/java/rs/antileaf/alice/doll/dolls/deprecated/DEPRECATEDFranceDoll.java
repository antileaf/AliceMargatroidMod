package rs.antileaf.alice.doll.dolls.deprecated;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

@Deprecated
public class DEPRECATEDFranceDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = DEPRECATEDFranceDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 5;
	public static final int PASSIVE_AMOUNT = 1;
	public static final int ACT_AMOUNT = 3;
	
	public DEPRECATEDFranceDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("blue"),
				RenderTextMode.BOTH
		);
		
		this.passiveAmountType = DollAmountType.OTHERS;
		this.actAmountType = DollAmountType.BLOCK;
	}
	
	@Override
	public int onPlayerDamaged(int amount) {
		return 0;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public int getBaseHP() {
		return MAX_HP;
	}
	
	@Override
	public void onAct() {
		AliceSpireKit.addActionToBuffer(new DollGainBlockAction(this, this.actAmount));
		
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (!m.isDeadOrEscaped()) {
				AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
						m,
						AbstractDungeon.player,
						new StrengthPower(m, -this.passiveAmount),
						-this.passiveAmount
				));
				
				if (!m.hasPower(ArtifactPower.POWER_ID))
					AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
							m,
							AbstractDungeon.player,
							new GainStrengthPower(m, this.passiveAmount),
							this.passiveAmount
					));
			}
		}
		
		AliceSpireKit.commitBuffer();
		
		this.highlightPassiveValue();
		this.highlightActValue();
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[0],
				this.coloredPassiveAmount(),
				dollStrings.DESCRIPTION[1]
		);
		
		this.actDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[2],
				this.coloredActAmount(),
				dollStrings.DESCRIPTION[3]
		);
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new DEPRECATEDFranceDoll();
	}
	
	@Override
	public void playChannelSFX() {}
}
