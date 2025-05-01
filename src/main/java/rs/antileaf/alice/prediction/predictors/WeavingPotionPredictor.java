package rs.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.random.Random;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;
import rs.antileaf.alice.cards.colorless.CreateDoll;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.potions.WeavingPotion;

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
