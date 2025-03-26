package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.ShanghaiDoll;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class LuminousShanghaiDollPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String SIMPLE_NAME = LuminousShanghaiDollPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public LuminousShanghaiDollPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.priority = 99;
		
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
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
//	@Override
//	public boolean canWorkOnSpecialAct() {
//		return false;
//	}
	
	@Override
	public void postDollAct(AbstractDoll doll) {
		if (!(doll instanceof ShanghaiDoll)) {
			ArrayList<AbstractDoll> shanghaiDolls = new ArrayList<>();
			
			for (AbstractDoll d : DollManager.get().getDolls())
				if (d instanceof ShanghaiDoll)
					shanghaiDolls.add(d);
			
			if (!shanghaiDolls.isEmpty()) {
				AbstractDoll target = shanghaiDolls.get(AbstractDungeon.cardRandomRng.random(shanghaiDolls.size() - 1));
				for (int i = 0; i < this.amount; i++)
					this.addToTop(new DollActAction(target, true));
				this.flash();
			}
		}
	}
}
