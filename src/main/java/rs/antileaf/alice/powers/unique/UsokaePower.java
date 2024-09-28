package rs.antileaf.alice.powers.unique;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class UsokaePower extends AbstractAlicePower implements OnReceivePowerPower {
	public static final String POWER_ID = UsokaePower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public UsokaePower() {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = -1;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}

	@Override
	public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
		// Returning false will negate the power

		if (target != this.owner || !(source instanceof AbstractMonster) || power.type != PowerType.DEBUFF)
			return true;

		AbstractMonster monster = (AbstractMonster) source;
		if (!DollManager.get().damageTarget.containsKey(monster)) {
			AliceSpireKit.logger.info("UsokaePower: No doll target for monster {}", monster.name);
			return true;
		}

		AbstractDoll doll = DollManager.get().getDolls().get(DollManager.get().damageTarget.get(monster));
		if (doll == null) {
			AliceSpireKit.logger.info("UsokaePower: Doll target for monster {} is null", monster.name);
			return true;
		}

		if (doll instanceof EmptyDollSlot)
			return true;

		this.flash();
		return false;
	}
}
