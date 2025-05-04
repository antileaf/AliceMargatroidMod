package me.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import me.antileaf.alice.cards.alice.DollCrusader;
import me.antileaf.alice.cards.colorless.CreateDoll;
import me.antileaf.alice.doll.AbstractDoll;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

public class DollCrusaderPredictor extends AbstractPredictor {
	public DollCrusaderPredictor() {
		super(DollCrusader.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public void update(Object o) {
		if (o instanceof AbstractCard) {
			Random rng = this.predictedRandom.copy();

			this.cardsToPreview.clear();

			for (int i = 0; i < 7; i++)
				this.cardsToPreview.add(new CreateDoll(AbstractDoll.dollClasses[
								rng.random(AbstractDoll.dollClasses.length - 1)]));
		}
	}
}
