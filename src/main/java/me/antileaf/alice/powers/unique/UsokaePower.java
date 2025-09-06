package me.antileaf.alice.powers.unique;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.card.specific.UsokaeCompatibilityPatch;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsokaePower extends AbstractAlicePower implements OnReceivePowerPower {
	private static final Logger logger = LogManager.getLogger(UsokaePower.class.getName());

	public static final String SIMPLE_NAME = UsokaePower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public UsokaePower() {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = -1;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}

	@Override
	public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
		// Returning false will negate the power

		if (UsokaeCompatibilityPatch.patched.containsKey(power)) {
			int index = UsokaeCompatibilityPatch.patched.get(power);
			if (index != -1) {
				AbstractDoll doll = DollManager.get().getDolls().get(index);
				if (doll != null) {
					logger.info("patched, index = {}", index);
					return doll instanceof EmptyDollSlot;
				}
			}

			logger.warn("This should not be reachable.");
			return true;
		}

		if (target != this.owner || !(source instanceof AbstractMonster) || power.type != PowerType.DEBUFF)
			return true;

		AbstractMonster monster = (AbstractMonster) source;
		if (!DollManager.get().damageTarget.containsKey(monster)) {
			AliceHelper.logger.info("UsokaePower: No doll target for monster {}", monster.name);
			return true;
		}

		AbstractDoll doll = DollManager.get().getDolls().get(DollManager.get().damageTarget.get(monster));
		if (doll == null) {
			AliceHelper.logger.info("UsokaePower: Doll target for monster {} is null", monster.name);
			return true;
		}

		if (doll instanceof EmptyDollSlot)
			return true;

		this.flash();
		return false;
	}
}
