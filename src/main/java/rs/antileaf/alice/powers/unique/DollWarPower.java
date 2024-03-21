package rs.antileaf.alice.powers.unique;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.powers.interfaces.OnDollOperatePower;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollWarPower extends AbstractPower implements OnDollOperatePower {
	public static final String POWER_ID = DollWarPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DollWarPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.img = new Texture(AliceSpireKit.getPowerImgFilePath("default"));
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = AliceMiscKit.join(
				powerStrings.DESCRIPTIONS[0],
				"#b" + this.amount,
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public void onSpawnDoll(AbstractDoll doll) {
		this.flash();
		this.addToBot(new DollGainBlockAction(doll, this.amount));
	}
}
