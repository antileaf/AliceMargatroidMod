package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import rs.antileaf.alice.action.common.AliceDiscoverAction;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.cards.colorless.CreateDoll;
import rs.antileaf.alice.cards.interfaces.ConditionalExhaustCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.FranceDoll;
import rs.antileaf.alice.doll.dolls.HouraiDoll;
import rs.antileaf.alice.doll.dolls.NetherlandsDoll;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.strings.AliceLanguageStrings;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class ClawMachine extends AbstractAliceCard implements ConditionalExhaustCard {
	public static final String SIMPLE_NAME = ClawMachine.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;

	private final ArrayList<String> choices = new ArrayList<>();

	public ClawMachine() {
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

		this.resumeChoices();

		this.targetIcons.add(AliceTargetIcon.SLOT);
//		this.targetIcons.add(AliceTargetIcon.NONE);

		this.initializeDescription();
	}

	private void resumeChoices() {
		this.choices.clear();
		this.choices.add(NetherlandsDoll.SIMPLE_NAME);
		this.choices.add(FranceDoll.SIMPLE_NAME);
		this.choices.add(HouraiDoll.SIMPLE_NAME);
	}

	public int getBits() {
		int bits = 0;

		if (this.choices.contains(NetherlandsDoll.SIMPLE_NAME))
			bits |= 1;
		if (this.choices.contains(FranceDoll.SIMPLE_NAME))
			bits |= 2;
		if (this.choices.contains(HouraiDoll.SIMPLE_NAME))
			bits |= 4;

		return bits;
	}

	public String predict(Random rng) {
		return this.choices.get(rng.random(this.choices.size() - 1));
	}

	@Override
	public void triggerOnExhaust() {
		this.resumeChoices();
		this.initializeDescription();
	}

	@Override
	public void initializeDescription() {
//		AliceHelper.logger.info("ClawMachine.initializeDescription: choices = {}", this.choices);
		if (this.choices == null)
			return;

		if (this.choices.size() > 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < this.choices.size(); i++) {
				if (i > 0) {
					if (i < this.choices.size() - 1)
						sb.append(" ").append(AliceLanguageStrings.CAESURA).append(" ");
					else
						sb.append(" ").append(AliceLanguageStrings.OR).append(" ");
				}

				sb.append("alicemargatroid:").append(AbstractDoll.getKeyword(this.choices.get(i)));
			}

			this.rawDescription = String.format(!this.upgraded ? cardStrings.DESCRIPTION :
					cardStrings.UPGRADE_DESCRIPTION, sb);
		}
		else if (this.choices.size() == 1)
			this.rawDescription = String.format(cardStrings.EXTENDED_DESCRIPTION[1],
					"alicemargatroid:" + AbstractDoll.getKeyword(this.choices.get(0)));
		else
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[2]; // 按理说不应该没有选项

		super.initializeDescription();
	}

	@Override
	public boolean shouldExhaust() {
		return this.choices.isEmpty();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDoll slot = this.getTargetedSlot();
		int index = DollManager.get().getDolls().indexOf(slot);

		if (this.choices.isEmpty())
			return;

		if (this.upgraded) {
			this.addToBot(new AliceDiscoverAction(
					() -> this.choices.stream().map(CreateDoll::new)
							.collect(ArrayList::new, ArrayList::add, ArrayList::addAll),
					(card) -> {
						if (card instanceof CreateDoll) {
							String clz = ((CreateDoll) card).dollClazz;
//							AliceHelper.logger.info("ClawMachine.use: clz = {}", clz);

							AbstractDoll doll = AbstractDoll.newInst(clz);
							this.addToTop(new SpawnDollAction(doll, index));

							this.choices.remove(clz);
							this.initializeDescription();
						}
//						else
//							AliceHelper.logger.info("ClawMachine.use: card is not CreateDoll!");
					},
					cardStrings.EXTENDED_DESCRIPTION[0],
					false
			));
		}
		else {
			String clz = this.choices.get(AbstractDungeon.cardRandomRng.random(this.choices.size() - 1));

			AbstractDoll doll = AbstractDoll.newInst(clz);
			this.addToTop(new SpawnDollAction(doll, index));

			this.choices.remove(clz);
			this.initializeDescription();
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ClawMachine();
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		ClawMachine card = (ClawMachine) super.makeStatEquivalentCopy();
		card.choices.clear();
		card.choices.addAll(this.choices);
		card.initializeDescription();
		return card;
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
