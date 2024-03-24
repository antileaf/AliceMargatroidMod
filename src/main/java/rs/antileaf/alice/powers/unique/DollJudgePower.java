package rs.antileaf.alice.powers.unique;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.powers.interfaces.OnDollOperatePower;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollJudgePower extends AbstractPower implements OnDollOperatePower {
	public static final String POWER_ID = DollJudgePower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DollJudgePower(int amount) {
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
				"#b" + this.amount + "%",
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public void postDollGainedBlock(AbstractDoll doll, int block) {
		int blockGain = (int) (block * this.amount / 100.0F);
		if (blockGain > 0)
			this.addToBot(new GainBlockAction(this.owner, this.owner, blockGain));
	}
}
