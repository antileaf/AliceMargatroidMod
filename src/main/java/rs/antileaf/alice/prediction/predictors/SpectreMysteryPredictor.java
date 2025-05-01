package rs.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;
import rs.antileaf.alice.cards.alice.SpectreMystery;
import rs.antileaf.alice.utils.AliceHelper;

public class SpectreMysteryPredictor extends AbstractPredictor {
	private static final long BASE = 10007L;

	private boolean upgraded;
	private long hash;

	public SpectreMysteryPredictor() {
		super(SpectreMystery.ID, RandomNumberType.cardRandomRng);
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
				newHash = newHash * BASE + c.uuid.hashCode() + c.timesUpgraded;

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

			for (int i = 0; i < card.magicNumber && i < AbstractDungeon.player.drawPile.size(); i++)
				this.cardsToPreview.add(AbstractDungeon.player.drawPile.getNCardFromTop(i)
						.makeStatEquivalentCopy());
		}
	}
}
