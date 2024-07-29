package rs.antileaf.alice.powers.unique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.effects.common.AliceDrawLineEffect;
import rs.antileaf.alice.powers.AbstractAlicePower;

public class MagicConduitPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String POWER_ID = MagicConduitPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	private final float VFX_MIN = 0.1F, VFX_MAX = 0.4F;
	private float vfxTimer = 0.0F;
	private float lineTimer = 0.0F;
	
	public MagicConduitPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(
				powerStrings.DESCRIPTIONS[0],
				this.amount
		);
	}
	
	@Override
	public void postDollAct(AbstractDoll doll) {
		if (DollManager.get().getDolls().size() == DollManager.MAX_DOLL_SLOTS
				&& DollManager.get().getDolls().get(DollManager.MAX_DOLL_SLOTS - 1) == doll) {
			this.flash();
			this.addToTop(new GainEnergyAction(this.amount));
		}
	}
	
	@Override
	public void update(int slot) {
		super.update(slot);
		
		this.vfxTimer -= Gdx.graphics.getDeltaTime();
		if (this.vfxTimer < 0.0F) {
			if (!DollManager.get().getDolls().isEmpty()) {
				AbstractDoll doll = DollManager.get().getDolls().get(DollManager.MAX_DOLL_SLOTS - 1);
				
				if (!(doll instanceof EmptyDollSlot)) {
					AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(doll.cX, doll.cY));
				}
			}
			
			this.vfxTimer = MathUtils.random(this.VFX_MIN, this.VFX_MAX);
		}
		
		this.lineTimer -= Gdx.graphics.getDeltaTime();
		if (this.lineTimer < 0.0F) {
			if (!DollManager.get().getDolls().isEmpty()) {
				AbstractDoll doll = DollManager.get().getDolls().get(DollManager.MAX_DOLL_SLOTS - 1);
				if (!(doll instanceof EmptyDollSlot))
					AbstractDungeon.effectList.add(new AliceDrawLineEffect(
							Color.CYAN.cpy(),
							(t) -> (float)(0.05 + 0.15 * MathUtils.sin(t * MathUtils.PI)),
							2.0F,
							0.25F,
							doll.getDrawCX(), doll.getDrawCY(),
							AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY - AbstractDungeon.player.hb.height / 4F,
							true
					));
			}
			
			this.lineTimer = 2.0F;
		}
	}
}
