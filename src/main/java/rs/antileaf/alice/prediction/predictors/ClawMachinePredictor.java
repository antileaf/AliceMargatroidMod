package rs.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;
import rs.antileaf.alice.cards.alice.ClawMachine;
import rs.antileaf.alice.cards.colorless.CreateDoll;
import rs.antileaf.alice.utils.AliceHelper;

public class ClawMachinePredictor extends AbstractPredictor {
	private boolean upgraded = false;
	private int bits = 0;

	public ClawMachinePredictor() {
		super(ClawMachine.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public boolean shouldUpdate(Object o) {
		if (super.shouldUpdate(o))
			return true;

		if (AliceHelper.isInBattle()) {
			boolean res = false;

			if ((o instanceof AbstractCard) && ((AbstractCard) o).upgraded != this.upgraded)
				res = true;

			int newBits = 0;
			if (o instanceof ClawMachine) {
				ClawMachine c = (ClawMachine) o;
				newBits = c.getBits();
			}

			if (newBits != this.bits)
				res = true;

			this.upgraded = (o instanceof AbstractCard) && ((AbstractCard) o).upgraded;
			this.bits = newBits;

			return res;
		}

		return false;
	}

	@Override
	public void update(Object o) {
		if (o instanceof ClawMachine) {
			ClawMachine card = (ClawMachine) o;
			Random rng = this.predictedRandom.copy();

			this.cardsToPreview.clear();

			if (!card.upgraded) {
				this.cardsToPreview.add(new CreateDoll(card.predict(rng)));
			}
		}
	}
}
