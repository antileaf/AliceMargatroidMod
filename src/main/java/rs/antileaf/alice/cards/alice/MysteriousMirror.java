package rs.antileaf.alice.cards.alice;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cardmodifier.PhantomCardModifier;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class MysteriousMirror extends AbstractAliceCard {
	public static final String SIMPLE_NAME = MysteriousMirror.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	
	public MysteriousMirror() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);
		
		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> tmpHand = p.hand.group.stream()
				.filter(card -> card != this && !PhantomCardModifier.check(card))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		
		this.addToBot(new AnonymousAction(() -> {
			for (AbstractCard card : tmpHand) {
				if (PhantomCardModifier.check(card))
					continue;
				
				if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
					AbstractDungeon.player.createHandIsFullDialog();
					break;
				}
				else {
					AbstractCard copy = card.makeStatEquivalentCopy();
					CardModifierManager.addModifier(copy, new PhantomCardModifier());
					AliceSpireKit.addCardToHand(copy);
				}
			}
		}));
		
		this.addToBot(new ApplyPowerAction(p, p, new NoDrawPower(p)));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new MysteriousMirror();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.retain = this.selfRetain = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
