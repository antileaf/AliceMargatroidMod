package rs.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;
import rs.antileaf.alice.cards.alice.Thread;
import rs.antileaf.alice.cards.colorless.CreateDoll;
import rs.antileaf.alice.doll.AbstractDoll;

public class ThreadPredictor extends AbstractPredictor {
	public ThreadPredictor() {
		super(Thread.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public void update(Object o) {
		if (o instanceof AbstractCard) {

			AbstractCard card = (AbstractCard) o;
			Random rng = this.predictedRandom.copy();

			this.cardsToPreview.clear();

			for (int i = 0; i < 6; i++)
				this.cardsToPreview.add(new CreateDoll(AbstractDoll.dollClasses[
								rng.random(AbstractDoll.dollClasses.length - 1)]));
		}
	}
}
