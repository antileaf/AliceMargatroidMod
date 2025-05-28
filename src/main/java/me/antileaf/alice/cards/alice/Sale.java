package me.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.doll.RecycleDollAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.CardTagEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.powers.unique.ForbiddenMagicPower;
import me.antileaf.alice.targeting.AliceTargetIcon;
import me.antileaf.alice.targeting.handlers.DollTargeting;
import me.antileaf.alice.utils.AliceHelper;

public class Sale extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Sale.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int GOLD = 12;
	private static final int UPGRADE_PLUS_GOLD = 4;
	
	public Sale() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL
		);
		
		this.magicNumber = this.baseMagicNumber = GOLD;
		this.exhaust = true;

		this.tags.add(CardTags.HEALING);

		this.dollTarget = true;
		this.hardCodedForbiddenMagic = true;
		this.isCommandCard = true;
		this.tags.add(CardTagEnum.ALICE_COMMAND);

		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = DollTargeting.getTarget(this);
		
		if (doll != null) {
			if (p.hasPower(ForbiddenMagicPower.POWER_ID)) {
				int amount = p.getPower(ForbiddenMagicPower.POWER_ID).amount;
				for (int i = 0; i < amount; i++)
					this.addToBot(new DollActAction(doll, AbstractDoll.DollActModifier.ambush()));
			}

			this.addToBot(new RecycleDollAction(doll));
			this.addToBot(new GainGoldAction(this.magicNumber));
			AliceHelper.addEffect(new GainGoldTextEffect(this.magicNumber));
			CardCrawlGame.sound.play("GOLD_JINGLE");
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Sale();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_GOLD);
			this.initializeDescription();
		}
	}
}
