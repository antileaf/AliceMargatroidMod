package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.handlers.DollTargeting;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollMiraCeti extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollMiraCeti.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = -1;
	private static final int MAGIC = 2;
	
	public DollMiraCeti() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTargetEnum.DOLL
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		
		this.tags.add(CardTagEnum.ALICE_COMMAND);
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AnonymousAction(() -> {
			int amount = this.energyOnUse + (this.upgraded ? 1 : 0);
			
			if (p.hasRelic(ChemicalX.ID))
				amount += ChemicalX.BOOST;
			
			if (amount > 0) {
				if (!this.freeToPlayOnce)
					p.energy.use(this.energyOnUse);
				
				AbstractDoll doll = DollTargeting.getTarget(this);
				
				if (doll != null)
					for (int i = 0; i < amount * this.magicNumber; i++)
						this.addToBot(new DollActAction(doll));
			}
		}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollMiraCeti();
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
