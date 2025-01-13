package rs.antileaf.alice.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import rs.antileaf.alice.cards.colorless.CreateSusan;
import rs.antileaf.alice.cards.colorless.PoisonousSweet;
import rs.antileaf.alice.utils.AliceHelper;

public class LilyOfTheValleyFlowerField extends PhasedEvent {
	public static final String SIMPLE_NAME = LilyOfTheValleyFlowerField.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

	private static final String INTRO = "INTRO", LEAVE = "LEAVE", CHOICE = "CHOICE",
			LEAVE_WITH_SUSAN = "LEAVE_WITH_SUSAN", LEAVE_WITH_FLOWERS = "LEAVE_WITH_FLOWERS";

	public static final int MAX_HP_GAIN = 10;
	public static final int MAX_HP_GAIN_HIGH_ASC = 5;
	public static final int HP_LOSS = 7;
	public static final int HP_LOSS_HIGH_ASC = 10;

	public int getHpGain() {
		return CardCrawlGame.isInARun() && AbstractDungeon.ascensionLevel >= 15 ? MAX_HP_GAIN_HIGH_ASC : MAX_HP_GAIN;
	}

	public int getHpLoss() {
		return CardCrawlGame.isInARun() && AbstractDungeon.ascensionLevel >= 15 ? HP_LOSS_HIGH_ASC : HP_LOSS;
	}

	public LilyOfTheValleyFlowerField() {
		super(ID, eventStrings.NAME, AliceHelper.getEventImgFilePath(SIMPLE_NAME));

		this.registerPhase(INTRO, new TextPhase(eventStrings.DESCRIPTIONS[0])
				.addOption(eventStrings.OPTIONS[0], (i) -> this.transitionKey(CHOICE))
				.addOption(eventStrings.OPTIONS[1], (i) -> this.transitionKey(LEAVE)));

		this.registerPhase(LEAVE, new TextPhase(eventStrings.DESCRIPTIONS[1])
				.addOption(eventStrings.OPTIONS[2], (i) -> this.openMap()));

		this.registerPhase(CHOICE, new TextPhase(eventStrings.DESCRIPTIONS[2])
				.addOption(new TextPhase.OptionInfo(String.format(eventStrings.OPTIONS[3], this.getHpGain()),
						new PoisonousSweet()),
						(i) -> {
							AbstractDungeon.player.increaseMaxHp(this.getHpGain(), true);
							AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
							AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new PoisonousSweet(),
									Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
							this.transitionKey(LEAVE_WITH_FLOWERS);
						})
				.addOption(new TextPhase.OptionInfo(String.format(eventStrings.OPTIONS[4], this.getHpLoss()),
								new CreateSusan())
						.enabledCondition(() -> AbstractDungeon.player.currentHealth > this.getHpLoss(),
								String.format(eventStrings.OPTIONS[5], this.getHpLoss())),
						(i) -> {
							AbstractDungeon.player.damage(new DamageInfo(null, this.getHpLoss(), DamageInfo.DamageType.HP_LOSS));
							CardCrawlGame.sound.play("POWER_POISON", 0.05F);
							CardCrawlGame.sound.play("BLOOD_SPLAT");
							AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new CreateSusan(),
									Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
							this.transitionKey(LEAVE_WITH_SUSAN);
						}));

		// OPTIONS[2] 都是“离开”
		this.registerPhase(LEAVE_WITH_SUSAN, new TextPhase(eventStrings.DESCRIPTIONS[3])
				.addOption(eventStrings.OPTIONS[2], (i) -> this.openMap()));

		this.registerPhase(LEAVE_WITH_FLOWERS, new TextPhase(eventStrings.DESCRIPTIONS[4])
				.addOption(eventStrings.OPTIONS[2], (i) -> this.openMap()));

		this.transitionKey(INTRO);
	}
}
