package rs.antileaf.alice.cards.alice;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cardmodifier.RevelationCardModifier;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class Revelation extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Revelation.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int MAGIC = 2;
	
	public Revelation() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> attacks = new ArrayList<>();
		for (AbstractCard other : AbstractDungeon.player.hand.group)
			if (other.type == CardType.ATTACK && other != this)
				attacks.add(other);
		
		if (!attacks.isEmpty()) {
			ArrayList<AbstractCard> chosen;
			if (!this.upgraded) {
				chosen = new ArrayList<>();
				chosen.add(attacks.get(AbstractDungeon.cardRandomRng.random(attacks.size() - 1)));
			}
			else
				chosen = attacks;
			
			boolean upg = this.upgraded;
			
			this.addToBot(new AnonymousAction(() -> {
				ArrayList<AbstractCard> failed = new ArrayList<>();
				for (AbstractCard c : chosen) {
					AliceHelper.upgradeCardDamage(c, this.magicNumber);
					
					if (AbstractDungeon.player.hand.contains(c)) {
						c.flash();
						
						if (CardModifierManager.hasModifier(c, RevelationCardModifier.ID)) {
							RevelationCardModifier mod = (RevelationCardModifier)CardModifierManager
									.getModifiers(c, RevelationCardModifier.ID).get(0);
							
							if (mod.isUpgraded() || !upg)
								continue;
						}
						
						CardModifierManager.addModifier(c, new RevelationCardModifier(upg));
					}
					else
						failed.add(c);
				}
				
				if (!failed.isEmpty())
					AliceHelper.log(SIMPLE_NAME + ": Chosen card(s) not in hand: " + failed);
			}));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Revelation();
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
