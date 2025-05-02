package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.HealDollAction;
import me.antileaf.alice.action.doll.IncreaseDollMaxHealthAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.doll.dolls.KyotoDoll;
import me.antileaf.alice.doll.dolls.ShanghaiDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.targeting.AliceHoveredTargets;
import me.antileaf.alice.utils.AliceHelper;

public class DollInSea extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollInSea.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 4;
	private static final int UPGRADE_PLUS_MAGIC = 4;
	
	public DollInSea() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.COMMON,
				CardTarget.NONE
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		return AliceHoveredTargets.fromDolls(DollManager.get().getDolls().stream()
				.filter(doll -> (doll instanceof ShanghaiDoll) || (doll instanceof KyotoDoll))
				.toArray(AbstractDoll[]::new));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot))
				this.addToBot(new HealDollAction(doll, doll.maxHP));

		for (AbstractDoll doll : DollManager.get().getDolls())
			if (doll instanceof ShanghaiDoll || doll instanceof KyotoDoll)
				this.addToBot(new IncreaseDollMaxHealthAction(doll, this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollInSea();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
