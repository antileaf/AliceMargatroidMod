package me.antileaf.alice.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

@Deprecated
public abstract class AbstractLoliCard<C extends AbstractCard> extends AbstractAliceCard {
	public AbstractLoliCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			AbstractCard.CardType type,
			AbstractCard.CardColor color,
			AbstractCard.CardRarity rarity,
			AbstractCard.CardTarget target
	) {
		super(
				id,
				name,
				img,
				cost,
				rawDescription,
				type,
				color,
				rarity,
				target
		);
	}

	// 推荐写一个 from 静态方法，方便注册
	public static <C extends AbstractCard> AbstractLoliCard<C> from(AbstractCard card) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public static <L extends AbstractLoliCard<C>, C extends AbstractCard>
	L fromOthers(L loliCard, C card) { // loliCard 必须是一个刚刚 new 出来的实例
		for (int i = 0; i < card.timesUpgraded; i++)
			loliCard.upgrade();

		// 我也不清楚为什么 BaseMod 要插在这里，先抄着
		CardModifierManager.copyModifiers(card, loliCard, false, true, false);

		loliCard.upgraded = card.upgraded;
		loliCard.timesUpgraded = card.timesUpgraded;

		loliCard.cost = card.cost; // 通常 cost 是不变的，如果变的话就自己写
		loliCard.costForTurn = card.costForTurn;
		loliCard.isCostModified = card.isCostModified;
		loliCard.isCostModifiedForTurn = card.isCostModifiedForTurn;

		loliCard.inBottleLightning = card.inBottleLightning;
		loliCard.inBottleFlame = card.inBottleFlame;
		loliCard.inBottleTornado = card.inBottleTornado;

		loliCard.freeToPlayOnce = card.freeToPlayOnce;

		return loliCard;
	}

	public static <L extends AbstractLoliCard<A>, A extends AbstractAliceCard>
	L fromAlice(L loliCard, A aliceCard) {
		L result = fromOthers(loliCard, aliceCard);

		// 好像爱丽丝也没什么需要特殊处理的属性，那没事了

		return result;
	}
}
