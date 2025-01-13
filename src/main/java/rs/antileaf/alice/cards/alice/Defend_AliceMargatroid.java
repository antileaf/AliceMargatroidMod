package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceHelper;

public class Defend_AliceMargatroid extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Defend_AliceMargatroid.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int BLOCK = 5;
	private static final int UPGRADE_PLUS_BLOCK = 3;
	
	public Defend_AliceMargatroid() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.BASIC,
				CardTargetEnum.DOLL_OR_NONE
		);
		
		this.block = this.baseBlock = BLOCK;
		this.tags.add(CardTags.STARTER_DEFEND);

		this.targetIcons.add(AliceTargetIcon.ALICE);
		this.targetIcons.add(AliceTargetIcon.DOLL);
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		if (slot == null || slot instanceof EmptyDollSlot)
			return AliceHoveredTargets.PLAYER;
		
		return AliceHoveredTargets.NONE;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = this.getTargetedDoll();
		
		if (doll != null)
			this.addToBot(new DollGainBlockAction(doll, this.block));
		else
			this.addToBot(new GainBlockAction(p, this.block));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Defend_AliceMargatroid();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_PLUS_BLOCK);
			this.initializeDescription();
		}
	}
}
