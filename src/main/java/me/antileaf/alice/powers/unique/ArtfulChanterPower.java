package me.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.interfaces.OnDollOperateHook;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class ArtfulChanterPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String SIMPLE_NAME = ArtfulChanterPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public ArtfulChanterPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		DollManager.Stats stats = DollManager.get().getStats();

		StringBuilder sb = new StringBuilder(String.format(powerStrings.DESCRIPTIONS[0], this.amount));
		sb.append(" NL ");

		if (stats.recycledOrDestroyedThisCombat.isEmpty())
			sb.append(powerStrings.DESCRIPTIONS[1]);
		else {
			sb.append(powerStrings.DESCRIPTIONS[2]);

			for (String id : stats.recycledOrDestroyedThisCombat) {
				sb.append(" NL ");
				sb.append(AbstractDoll.getName(id));
			}
		}

		this.description = sb.toString();
	}

	@Override
	public void postRecycleOrDestroyDoll(AbstractDoll doll) {
		AliceHelper.addActionToBuffer(new AnonymousAction(this::updateDescription));
	}

	@Override
	public void postSpawnDoll(AbstractDoll doll) {
		if (DollManager.get().getStats().recycledOrDestroyedThisCombat
				.contains(doll.getID())) {
			this.flash();

			for (int i = 0; i < this.amount; i++)
				AliceHelper.addActionToBuffer(new DollActAction(doll));
		}
	}
}
