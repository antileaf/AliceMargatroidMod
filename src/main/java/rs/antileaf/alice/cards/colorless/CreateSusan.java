package rs.antileaf.alice.cards.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.Su_san;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceHelper;

public class CreateSusan extends AbstractAliceCard {
	public static final String SIMPLE_NAME = CreateSusan.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	
	public CreateSusan() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS, // AbstractCardEnum.ALICE_MARGATROID_DERIVATION_COLOR,
				CardRarity.SPECIAL,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getDolls().indexOf(slot);
		
		this.addToBot(new SpawnDollAction(new Su_san(), index));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CreateSusan();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.isInnate = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
