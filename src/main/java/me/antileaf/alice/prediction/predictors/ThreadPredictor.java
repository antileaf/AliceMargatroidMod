package me.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import me.antileaf.alice.cards.alice.Thread;
import me.antileaf.alice.cards.colorless.CreateDoll;
import me.antileaf.alice.doll.AbstractDoll;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

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
