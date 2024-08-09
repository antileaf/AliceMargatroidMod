package rs.antileaf.alice.cards.AliceMargatroid;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cardmodifier.PhantomCardModifier;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Phantom extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Phantom.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public Phantom() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		if (super.canUse(p, m)) {
			if (AliceSpireKit.hasDuplicateCards(p.hand.group, null)) {
				this.cantUseMessage = AliceSpireKit.getDuplicateCardsMessage(p.hand.group, null);
				return false;
			}
			else
				return true;
		}
		return false;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new SelectCardsInHandAction(
				AliceMiscKit.join(
						cardStrings.EXTENDED_DESCRIPTION[0],
						"" + this.magicNumber,
						cardStrings.EXTENDED_DESCRIPTION[1]
				),
				(card) -> !PhantomCardModifier.check(card),
				(cards) -> {
					if (!cards.isEmpty()) {
						AbstractCard selected = cards.get(0);
						
						this.addToBot(new AnonymousAction(() -> {
//							boolean hasCreatedDialog = false;
							
							for (int i = 0; i < this.magicNumber; i++) {
								if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
									AbstractDungeon.player.createHandIsFullDialog();
									break;
								}
								else {
									AbstractCard copy = selected.makeStatEquivalentCopy();
									CardModifierManager.addModifier(copy, new PhantomCardModifier());
									AliceSpireKit.addCardToHand(copy);
								}
							}
						}));
					}
				}
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Phantom();
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
