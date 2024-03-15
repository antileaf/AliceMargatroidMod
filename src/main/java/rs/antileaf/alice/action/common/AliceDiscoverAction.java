package rs.antileaf.alice.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.MasterReality;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AliceDiscoverAction extends AbstractGameAction {
	private static final float DURATION = Settings.ACTION_DUR_FAST;
	
	private final Supplier<ArrayList<AbstractCard>> supplier;
	private final Consumer<AbstractCard> consumer;
	private final String text;
	private final boolean masterReality;
	private boolean selected = false;
	
	public AliceDiscoverAction(Supplier<ArrayList<AbstractCard>> supplier,Consumer<AbstractCard> consumer,
	                           String text, boolean masterReality) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = DURATION;
		
		this.supplier = supplier;
		this.consumer = consumer;
		this.text = text;
		this.masterReality = masterReality;
	}
	
	public AliceDiscoverAction(ArrayList<AbstractCard> cards, Consumer<AbstractCard> consumer,
	                           String text, boolean masterReality) {
		this(() -> cards, consumer, text, masterReality);
	}
	
	public void update() {
		if (this.duration == DURATION) {
			ArrayList<AbstractCard> cards = supplier.get();
			if (this.masterReality && AbstractDungeon.player.hasPower(MasterRealityPower.POWER_ID))
				for (AbstractCard c : cards)
					c.upgrade();
			
			if (cards.isEmpty()) {
				this.isDone = true;
				return;
			}
			
			if (cards.size() == 1) {
				consumer.accept(cards.get(0));
				this.isDone = true;
				return;
			}
			
			AbstractDungeon.cardRewardScreen.customCombatOpen(cards, text, false);
			this.tickDuration();
		}
		else {
			if (!this.selected) {
				if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
					consumer.accept(AbstractDungeon.cardRewardScreen.discoveryCard);
					AbstractDungeon.cardRewardScreen.discoveryCard = null;
				}
				
				this.selected = true;
			}
			
			this.tickDuration();
		}
	}
}
