package me.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.effects.unique.SeaOfSubconsciousnessEffect;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class SeaOfSubconsciousnessPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = SeaOfSubconsciousnessPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public SeaOfSubconsciousnessPower(int amount) {
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
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (AbstractDungeon.player.hand.contains(card)) {
			int index = AbstractDungeon.player.hand.group.indexOf(card);
			if (index < DollManager.get().getDolls().size())
				this.addToBot(new AnonymousAction(() -> {
					int i = DollManager.get().getDolls().size() - index - 1;
					if (i >= 0 && i < DollManager.get().getDolls().size()) {
						AbstractDoll doll = DollManager.get().getDolls().get(i);
						if (!(doll instanceof EmptyDollSlot)) {
							this.flash();
							this.addToTop(new DollActAction(doll));
						}
					}
					else
						AliceHelper.log("SeaOfSubconsciousnessPower: index out of bounds: " + i);
				}));
		}
	
//		AliceSpireKit.addEffect(new PowerIconShowEffect(this));
	}
	
	@Override
	public void update(int slot) {
		super.update(slot);
		
		int index = -1;
		
		AbstractCard selected = AbstractDungeon.player.hoveredCard;
		if (selected == null) {
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				if (c.isHoveredInHand(0.1F)) {
					selected = c;
					break;
				}
			}
		}
		
		if (selected != null)
			index = AbstractDungeon.player.hand.group.indexOf(selected);
		
//		AliceSpireKit.log("SeaOfSubconsciousnessPower: index = " + index);
		
		if (index >= 0 && index < DollManager.get().getDolls().size()) {
			index = DollManager.get().getDolls().size() - index - 1;
			
			if (!SeaOfSubconsciousnessEffect.instances.containsKey(index)) {
				SeaOfSubconsciousnessEffect.instances.put(index, new SeaOfSubconsciousnessEffect(index));
				AbstractDungeon.effectList.add(SeaOfSubconsciousnessEffect.instances.get(index));
			}
		}
		
		for (int i : SeaOfSubconsciousnessEffect.instances.keySet())
			SeaOfSubconsciousnessEffect.instances.get(i).hovered = (i == index);
	}
}
