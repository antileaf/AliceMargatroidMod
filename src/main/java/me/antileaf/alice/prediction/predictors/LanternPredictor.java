package me.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.alice.cards.alice.Lantern;
import me.antileaf.alice.utils.AliceHelper;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

public class LanternPredictor extends AbstractPredictor {
	private static final long BASE = 10007L;

	private long hash;

	public LanternPredictor() {
		super(Lantern.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public boolean shouldUpdate(Object o) {
		if (super.shouldUpdate(o))
			return true;

		if (AliceHelper.isInBattle()) {
			boolean res = false;

			long newHash = 0;
			for (AbstractCard c : AbstractDungeon.player.drawPile.group)
				newHash = newHash * BASE + c.uuid.hashCode() + c.timesUpgraded;

			if (newHash != this.hash)
				res = true;

			this.hash = newHash;

			return res;
		}

		return false;
	}

	@Override
	public void update(Object o) {
		if (o instanceof AbstractCard) {
			AbstractCard card = (AbstractCard) o;

			this.cardsToPreview.clear();

			for (int i = 0; i < 3 && i < AbstractDungeon.player.drawPile.size(); i++)
				this.cardsToPreview.add(AbstractDungeon.player.drawPile.group.get(i)
						.makeStatEquivalentCopy());
		}
	}
}
