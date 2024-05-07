package rs.antileaf.alice.cards.AliceMargatroid;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cardmodifier.RevelationCardModifier;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Revelation extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Revelation.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 4;
	private static final int UPGRADE_PLUS_DAMAGE = 2;
	
	public Revelation() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.exhaust = true;
	}
	
	@Override
	public void triggerOnGlowCheck() {
		if (AliceSpireKit.isInBattle() &&
				AbstractDungeon.player.hand.group.stream()
						.anyMatch(c -> c != this && c.type == CardType.ATTACK))
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		
		AbstractCard[] choices = p.hand.group.stream()
				.filter(c -> c != this && c.type == CardType.ATTACK)
				.toArray(AbstractCard[]::new);
		
		if (choices.length > 0) {
			int amount = this.baseDamage;
			
			AbstractCard choice = choices[AbstractDungeon.cardRng.random(choices.length - 1)];
			this.addToBot(new AnonymousAction(() -> {
				if (AbstractDungeon.player.hand.contains(choice)) {
					if (!CardModifierManager.hasModifier(choice, RevelationCardModifier.ID))
						CardModifierManager.addModifier(choice, new RevelationCardModifier(amount));
					else
						CardModifierManager.getModifiers(choice, RevelationCardModifier.ID).stream()
								.findFirst()
								.ifPresent(mod -> ((RevelationCardModifier) mod).stackAmount(amount));
					choice.flash();
					choice.applyPowers();
				}
				else
					AliceSpireKit.log(SIMPLE_NAME, "Chosen card (" + choice.name + ") not in hand.");
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
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
			this.initializeDescription();
		}
	}
}
