package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.action.doll.SpawnDollAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class DollArrangement extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollArrangement.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int BLOCK = 4;
	private static final int MAGIC = 3;

	public DollArrangement() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT
		);

		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.targetIcons.add(AliceTargetIcon.SLOT);
	}

	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		AliceHoveredTargets res = AliceHoveredTargets.fromDolls(DollManager.get().getDolls().stream()
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.toArray(AbstractDoll[]::new));

		if (this.upgraded)
			res.player = true;

		return res;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getIndex(slot);

		ArrayList<String> choices = new ArrayList<>();
		for (String id : AbstractDoll.dollClasses)
			if (DollManager.get().getDolls().stream()
					.noneMatch(d -> d.getID().equals(id)))
				choices.add(id);

		if (!choices.isEmpty()) {
			String clazz = choices.get(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
			choices.remove(clazz);

			this.addToBot(new SpawnDollAction(AbstractDoll.newInst(clazz), index));

			for (int i = DollManager.get().getDollCount() + (slot instanceof EmptyDollSlot ? 1 : 0); i < 3; i++) {
				if (choices.isEmpty())
					break;

				String nextClazz = choices.get(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
				choices.remove(nextClazz);
				this.addToBot(new SpawnDollAction(AbstractDoll.newInst(nextClazz), -1));
			}
		}

		if (this.upgraded)
			this.addToBot(new GainBlockAction(p, p, this.block));

		this.addToBot(new AnonymousAction(() -> {
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot))
					AliceHelper.addActionToBuffer(new DollGainBlockAction(doll, this.block));

			AliceHelper.commitBuffer();
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollArrangement();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
