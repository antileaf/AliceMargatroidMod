package me.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import me.antileaf.alice.cards.alice.SPDoll;
import me.antileaf.alice.cards.colorless.CreateDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.doll.dolls.KyotoDoll;
import me.antileaf.alice.doll.dolls.ShanghaiDoll;
import me.antileaf.alice.utils.AliceHelper;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

import java.util.ArrayList;

public class SPDollPredictor extends AbstractPredictor {
	private boolean upgraded = false;
	private int bits = 0;

	public SPDollPredictor() {
		super(SPDoll.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public boolean shouldUpdate(Object o) {
		if (super.shouldUpdate(o))
			return true;

		if (AliceHelper.isInBattle()) {

			int newBits = 0;
			for (int i = 0; i < DollManager.MAX_DOLL_SLOTS; i++)
				if (!(DollManager.get().getDolls().get(i) instanceof EmptyDollSlot))
					newBits |= (1 << i);

			boolean res = false;

			if (o instanceof AbstractCard && ((AbstractCard) o).upgraded != this.upgraded)
				res = true;
			else if (this.bits != newBits)
				res = true;

			this.upgraded = (o instanceof AbstractCard) && ((AbstractCard) o).upgraded;
			this.bits = newBits;

			return res;
		}

		return false;
	}

	@Override
	public void update(Object o) {
		if (o instanceof AbstractCard) {
			this.cardsToPreview.clear();

			AbstractCard card = (AbstractCard) o;
			Random rng = this.predictedRandom.copy();

			ArrayList<Integer> indices = new ArrayList<>();
			for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
				if (DollManager.get().getDolls().get(i) instanceof EmptyDollSlot)
					indices.add(i);
			}

			int count = Math.min(indices.size(), card.magicNumber);

			for (int i = 0; i < count; i++) {
				int num = rng.random(0, 1);

				int k = rng.random(indices.size() - 1);
				int index = indices.get(k);
				indices.remove(k);

				this.cardsToPreview.add(new CreateDoll(num == 0 ?
						ShanghaiDoll.SIMPLE_NAME : KyotoDoll.SIMPLE_NAME, index));
			}
		}
	}
}
