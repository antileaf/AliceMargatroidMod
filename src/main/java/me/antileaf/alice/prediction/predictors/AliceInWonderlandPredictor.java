package me.antileaf.alice.prediction.predictors;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import me.antileaf.alice.cards.alice.AliceInWonderland;
import randomNumberPredictionMaster.misc.RandomNumberType;
import randomNumberPredictionMaster.predictors.AbstractPredictor;

import java.util.ArrayList;

public class AliceInWonderlandPredictor extends AbstractPredictor {
	public AliceInWonderlandPredictor() {
		super(AliceInWonderland.ID, RandomNumberType.cardRandomRng);
	}

	@Override
	public void update(Object o) {
		if (o instanceof AbstractCard) {
			ArrayList<AbstractCard> list = new ArrayList<>();

			for(AbstractCard c : AbstractDungeon.srcCommonCardPool.group) {
				if (!c.hasTag(AbstractCard.CardTags.HEALING))
					list.add(c);
			}

			for(AbstractCard c : AbstractDungeon.srcUncommonCardPool.group) {
				if (!c.hasTag(AbstractCard.CardTags.HEALING))
					list.add(c);
			}

			for(AbstractCard c : AbstractDungeon.srcRareCardPool.group) {
				if (!c.hasTag(AbstractCard.CardTags.HEALING))
					list.add(c);
			}

			AbstractCard card = (AbstractCard) o;

			this.cardsToPreview.clear();

			if (list.isEmpty())
				return;

			Random rng = this.predictedRandom.copy();

			for (int i = 0; i < Math.max(7, AbstractDungeon.player.hand.size()); i++) {
				AbstractCard c = list.get(rng.random(list.size() - 1)).makeCopy();

				if (card.upgraded)
					c.upgrade();

				if (c.cost != -1 && c.cost != -2)
					c.updateCost(-1);

				this.cardsToPreview.add(c);
			}
		}
	}
}
