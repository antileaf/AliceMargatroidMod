package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.unique.MagicConduitPower;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.utils.AliceSpireKit;

public class MagicConduit extends AbstractAliceCard {
	public static final String SIMPLE_NAME = MagicConduit.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;
	
	public MagicConduit() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		return AliceHoveredTargets.fromDolls(DollManager.get().getDolls().get(DollManager.MAX_DOLL_SLOTS - 1));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(p, p, new MagicConduitPower(1), 1));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new MagicConduit();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
