package rs.antileaf.alice.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import rs.antileaf.alice.cards.colorless.CreateSusan;
import rs.antileaf.alice.cards.colorless.PoisonousSweet;
import rs.antileaf.alice.utils.AliceHelper;

@Deprecated
public class DEPRECATEDLilyOfTheValleyFlowerField extends AbstractImageEvent {
	public static final String SIMPLE_NAME = DEPRECATEDLilyOfTheValleyFlowerField.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

	public static final int MAX_HP_GAIN = 10;
	public static final int MAX_HP_GAIN_HIGH_ASC = 5;
	public static final int HP_LOSS = 7;
	public static final int HP_LOSS_HIGH_ASC = 10;

	private static enum CurrentScreen {
		INTRO, CHOICE, LEAVE
	}

	CurrentScreen cur;

	public DEPRECATEDLilyOfTheValleyFlowerField() {
		super(eventStrings.NAME, eventStrings.DESCRIPTIONS[0], AliceHelper.getEventImgFilePath(SIMPLE_NAME));
		
		this.cur = CurrentScreen.INTRO;
		this.imageEventText.setDialogOption(eventStrings.OPTIONS[0]);
	}
	
	public int getHpGain() {
		return CardCrawlGame.isInARun() && AbstractDungeon.ascensionLevel >= 15 ? MAX_HP_GAIN_HIGH_ASC : MAX_HP_GAIN;
	}
	
	public int getHpLoss() {
		return CardCrawlGame.isInARun() && AbstractDungeon.ascensionLevel >= 15 ? HP_LOSS_HIGH_ASC : HP_LOSS;
	}
	
//	@Override
//	public void update() {
//		super.update();
//		if (!RoomEventDialog.waitForInput)
//			this.buttonEffect(this.roomEventText.getSelectedOption());
//	}
	
	@Override
	protected void buttonEffect(int buttonPressed) {
		if (this.cur == CurrentScreen.INTRO) {
			if (buttonPressed != 0)
				AliceHelper.log("INTRO, Invalid button pressed: " + buttonPressed);
			
			this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[1]);
			this.imageEventText.updateDialogOption(0, String.format(eventStrings.OPTIONS[1], this.getHpGain()),
					new PoisonousSweet());
			this.imageEventText.setDialogOption(String.format(eventStrings.OPTIONS[2], this.getHpLoss()),
					new CreateSusan());
			
			this.cur = CurrentScreen.CHOICE;
		}
		else if (this.cur == CurrentScreen.CHOICE) {
			if (buttonPressed == 0) {
				this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[2]);
				AbstractDungeon.player.increaseMaxHp(this.getHpGain(), true);
				AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
				AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
						new PoisonousSweet(), (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)
				));
				logMetric(ID, "Take lily of the valley");
			}
			else if (buttonPressed == 1) {
				this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[3]);
				AbstractDungeon.player.damage(new DamageInfo(null, this.getHpLoss(), DamageInfo.DamageType.HP_LOSS));
				CardCrawlGame.sound.play("POWER_POISON", 0.05F);
				CardCrawlGame.sound.play("BLOOD_SPLAT");
				AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
						new CreateSusan(), (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)
				));
				logMetric(ID, "Take Su-san");
			}
			else
				AliceHelper.log("CHOICE, Invalid button pressed: " + buttonPressed);
			
			this.imageEventText.updateDialogOption(0, eventStrings.OPTIONS[3]);
			this.imageEventText.clearRemainingOptions();
			
			this.cur = CurrentScreen.LEAVE;
		}
		else if (this.cur == CurrentScreen.LEAVE) {
			logMetric(ID, "Leave");
			this.openMap();
		}
	}
}
