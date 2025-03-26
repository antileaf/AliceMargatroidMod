package rs.antileaf.alice.cards.deprecated;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceHelper;

public class DollMagic extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollMagic.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int BLOCK = 4;
	private static final int MAGIC = 5;
	private static final int UPGRADE_MINUS_MAGIC = 1;
	
	private int counter;
	
	public DollMagic() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_NONE
		);
		
		this.block = this.baseBlock = BLOCK;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.counter = this.magicNumber;
		
		if (AliceHelper.isInBattle())
			this.applyPowers();

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
	public void applyPowers() {
		super.applyPowers();
		
		if (AliceHelper.isInBattle() &&
				(AbstractDungeon.player.drawPile.contains(this) || AbstractDungeon.player.discardPile.contains(this)))
			this.rawDescription = cardStrings.DESCRIPTION +
					String.format(cardStrings.EXTENDED_DESCRIPTION[0], this.counter);
		else
			this.rawDescription = cardStrings.DESCRIPTION;
		
		this.initializeDescription();
	}
	
	// Called in DollManager.
	public void postDollAct() {
		this.counter--;
		this.applyPowers();
		if (this.counter <= 0)
			this.addToBot(new AnonymousAction(this::returnToHand));
	}
	
	@Override
	public void triggerWhenDrawn() {
		this.counter = this.magicNumber;
		this.applyPowers();
	}
	
	@Override
	public void triggerOnEndOfPlayerTurn() {
		this.counter = this.magicNumber;
		this.applyPowers();
	}
	
	@Override
	public void triggerOnManualDiscard() {
		this.counter = this.magicNumber;
		this.applyPowers();
	}
	
	@Override
	public void onMoveToDiscard() {
		this.counter = this.magicNumber;
		this.applyPowers();
	}
	
	@Override
	public void triggerOnExhaust() {
		this.counter = this.magicNumber;
		this.applyPowers();
	}
	
	public void returnToHand() {
		if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
			if (AbstractDungeon.player.drawPile.contains(this))
				AbstractDungeon.player.drawPile.moveToHand(this);
			else if (AbstractDungeon.player.discardPile.contains(this))
				AbstractDungeon.player.discardPile.moveToHand(this);
		}
		
		this.counter = this.magicNumber;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll doll = this.getTargetedDoll();
		
		if (doll != null)
			this.addToBot(new DollGainBlockAction(doll, this.block));
		else
			this.addToBot(new GainBlockAction(p, this.block));
		
		this.counter = this.magicNumber;
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollMagic();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(-UPGRADE_MINUS_MAGIC);
			
			if (AliceHelper.isInBattle()) {
				this.counter -= UPGRADE_MINUS_MAGIC;
				if (this.counter <= 0)
					this.addToBot(new AnonymousAction(this::returnToHand));
				
				this.applyPowers();
			}
			this.initializeDescription();
		}
	}
}
