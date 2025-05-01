package rs.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;
import rs.antileaf.alice.cards.alice.Revelation;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class RevelationPredictor extends AbstractPredictor {
	private static final long BASE = 10007L;

	private boolean upgraded = false;
	private long hash = 0;

	public RevelationPredictor() {
		super(Revelation.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public boolean shouldUpdate(Object o) {
		if (super.shouldUpdate(o))
			return true;

		if (AliceHelper.isInBattle()) {
			boolean res = false;

			if ((o instanceof AbstractCard) && ((AbstractCard) o).upgraded != this.upgraded)
				res = true;

			long newHash = 0;
			for (AbstractCard c : AbstractDungeon.player.hand.group)
				if (c.type == AbstractCard.CardType.ATTACK && c != o)
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
			Random rng = this.predictedRandom.copy();

			this.cardsToPreview.clear();

			if (!card.upgraded) {
				ArrayList<AbstractCard> attacks = new ArrayList<>();
				for (AbstractCard c : AbstractDungeon.player.hand.group)
					if (c.type == AbstractCard.CardType.ATTACK && c != card)
						attacks.add(c);

				this.cardsToPreview.add(attacks.get(rng.random(attacks.size() - 1))
						.makeStatEquivalentCopy());
			}
		}
	}
}
