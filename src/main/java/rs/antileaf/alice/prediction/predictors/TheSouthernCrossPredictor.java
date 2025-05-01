package rs.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;
import rs.antileaf.alice.cards.alice.TheSouthernCross;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;
import java.util.Collections;

public class TheSouthernCrossPredictor extends AbstractPredictor {
	private static final long BASE = 10007L;

	private boolean upgraded;
	private long hash;

	public TheSouthernCrossPredictor() {
		super(TheSouthernCross.ID, RandomNumberType.shuffleRng);
	}

	@Override
	public boolean shouldUpdate(Object o) {
		if (super.shouldUpdate(o))
			return true;

		if (AliceHelper.isInBattle()) {
			boolean res = false;

			if (o instanceof AbstractCard && ((AbstractCard) o).upgraded != this.upgraded)
				res = true;

			long newHash = 0;
			for (AbstractCard c : AbstractDungeon.player.drawPile.group)
				newHash = newHash * BASE + c.uuid.hashCode();

			if (newHash != this.hash)
				res = true;

			this.upgraded = (o instanceof AbstractCard) && ((AbstractCard) o).upgraded;
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

			ArrayList<AbstractCard> cards = new ArrayList<>();
			for (AbstractCard c : AbstractDungeon.player.drawPile.group)
				if (c.type == AbstractCard.CardType.ATTACK && c.costForTurn > 0)
					cards.add(c);

			if (!cards.isEmpty()) {
				Random rng = this.predictedRandom.copy();
				Collections.shuffle(cards, new java.util.Random(rng.randomLong()));

				for (int i = 0; i < card.magicNumber; i++) {
					if (cards.isEmpty())
						break;

					this.cardsToPreview.add(cards.remove(cards.size() - 1)
							.makeStatEquivalentCopy());
				}
			}
		}
	}
}
