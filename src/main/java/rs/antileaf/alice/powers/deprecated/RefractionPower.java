package rs.antileaf.alice.powers.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceMiscHelper;

@Deprecated
public class RefractionPower extends AbstractAlicePower {
	public static final String POWER_ID = RefractionPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public RefractionPower(int amount) {
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
		this.description = AliceMiscHelper.join(
				powerStrings.DESCRIPTIONS[0],
				"#b" + this.amount,
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
		if (card.hasTag(CardTagEnum.ALICE_RAY))
			return damage + this.amount;
		else
			return damage;
	}
}
