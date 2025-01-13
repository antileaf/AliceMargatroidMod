package rs.antileaf.alice.powers.deprecated;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceMiscHelper;

public class TyrantPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String POWER_ID = TyrantPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public TyrantPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	private static void updateCurrentDolls(int amt) {
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot)){
				doll.HP += amt;
				doll.maxHP += amt;
			}
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
		this.addToTop(new AnonymousAction(() -> {
			updateCurrentDolls(stackAmount);
		}));
	}
	
	@Override
	public void onInitialApplication() {
		this.addToTop(new AnonymousAction(() -> {
			updateCurrentDolls(this.amount);
		}));
	}
	
	@Override
	public void updateDescription() {
		this.description = AliceMiscHelper.join(
				powerStrings.DESCRIPTIONS[0],
				"#b" + this.amount,
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public void postSpawnDoll(AbstractDoll doll) {
		doll.HP += this.amount;
		doll.maxHP += this.amount;
	}
}
