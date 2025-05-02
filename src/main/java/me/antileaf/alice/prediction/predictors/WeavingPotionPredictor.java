package me.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.random.Random;
import me.antileaf.alice.cards.colorless.CreateDoll;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.potions.WeavingPotion;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

public class WeavingPotionPredictor extends AbstractPredictor {
	public WeavingPotionPredictor() {
		super(WeavingPotion.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public void update(Object o) {
		Random rng = this.predictedRandom.copy();

		this.cardsToPreview.clear();

		for (int i = 0; i < 6; i++)
			this.cardsToPreview.add(new CreateDoll(AbstractDoll.dollClasses[
							rng.random(AbstractDoll.dollClasses.length - 1)]));
	}
}
