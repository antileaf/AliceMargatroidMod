package rs.antileaf.alice.cards.loli;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractLoliCard;
import rs.antileaf.alice.cards.alice.CharitableFranceDoll;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.FranceDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceSpireKit;

public class CharitableFranceDoll_Loli extends AbstractLoliCard<CharitableFranceDoll> {
	public static final String SIMPLE_NAME = CharitableFranceDoll_Loli.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	private static final int MAGIC2 = 3;
	private static final int UPGRADE_PLUS_MAGIC2 = 1;

	public CharitableFranceDoll_Loli() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(CharitableFranceDoll.SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_LOLI_COLOR,
				CardRarity.UNCOMMON,
				CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;

		this.tags.add(CardTagEnum.ALICE_DOLL_ACT);

		this.targetIcons.add(AliceTargetIcon.SLOT);
		this.targetIcons.add(AliceTargetIcon.NONE);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
			this.addToBot(new ApplyPowerAction(mon, p, new StrengthPower(mon,
					-this.secondaryMagicNumber), -this.secondaryMagicNumber, true,
					AbstractGameAction.AttackEffect.NONE));

		for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters)
			if (!mon.hasPower(ArtifactPower.POWER_ID))
				this.addToBot(new ApplyPowerAction(mon, p, new GainStrengthPower(mon,
						this.secondaryMagicNumber), this.secondaryMagicNumber, true,
						AbstractGameAction.AttackEffect.NONE));

		AbstractDoll slot = this.getTargetedSlot();
		if (slot instanceof FranceDoll) {
			for (int i = 0; i < this.magicNumber; i++)
				this.addToBot(new DollActAction(slot));
		}
		else {
			int index = DollManager.get().getDolls().indexOf(slot);
			AbstractDoll doll = new FranceDoll();
			this.addToBot(new SpawnDollAction(doll, index));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new CharitableFranceDoll_Loli();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);
			this.initializeDescription();
		}
	}
}
