package me.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import me.antileaf.alice.cards.alice.DollArrangement;
import me.antileaf.alice.cards.colorless.CreateDoll;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.utils.AliceHelper;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

import java.util.ArrayList;

public class DollArrangementPredictor extends AbstractPredictor {
	private int bits = 0;

	public DollArrangementPredictor() {
		super(DollArrangement.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public boolean shouldUpdate(Object o) {
		if (super.shouldUpdate(o))
			return true;

		if (AliceHelper.isInBattle()) {
			int newBits = 0;
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot)) {
					for (int i = 0; i < AbstractDoll.dollClasses.length; i++) {
						if (doll.getID().equals(AbstractDoll.dollClasses[i])) {
							newBits |= (1 << i);
							break;
						}
					}
				}

			boolean res = this.bits != newBits;

			this.bits = newBits;

			return res;
		}

		return false;
	}

	@Override
	public void update(Object o) {
		if (o instanceof AbstractCard) {
			this.cardsToPreview.clear();

			ArrayList<String> choices = new ArrayList<>();
			for (String id : AbstractDoll.dollClasses) {
				if (DollManager.get().getDolls().stream()
						.noneMatch(d -> d.getID().equals(id))) {
					choices.add(id);
				}
			}

			if (choices.isEmpty())
				return;

			Random rng = this.predictedRandom.copy();

			String clazz = choices.get(rng.random(choices.size() - 1));
			choices.remove(clazz);
			this.cardsToPreview.add(new CreateDoll(clazz));

			for (int i = DollManager.get().getDollCount(); i < 3; i++) {
				if (choices.isEmpty())
					break;

				String nextClazz = choices.get(rng.random(choices.size() - 1));
				choices.remove(nextClazz);
				this.cardsToPreview.add(new CreateDoll(nextClazz));
			}
		}
	}
}
