package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.FranceDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceHelper;

public class CharitableFranceDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = CharitableFranceDoll.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public CharitableFranceDoll() {
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
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.SLOT);
//		this.targetIcons.add(AliceTargetIcon.NONE);
	}

	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (slot != null) {
			int index = DollManager.get().getIndex(slot);
			AbstractMonster m = AliceHelper.getMonsterByIndex(index);
			if (m != null)
				return new AliceHoveredTargets(){{
					monsters = new AbstractMonster[]{m};
				}};
		}

		return AliceHoveredTargets.NONE;
	}


	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getDolls().indexOf(slot);
		AbstractDoll doll = new FranceDoll();
		this.addToBot(new SpawnDollAction(doll, index));

		AbstractMonster mo = AliceHelper.getMonsterByIndex(index);
		if (mo != null)
			this.addToBot(new ApplyPowerAction(mo, p,
					new WeakPower(mo, this.magicNumber, false)));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CharitableFranceDoll();
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
