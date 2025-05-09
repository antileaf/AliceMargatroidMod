package me.antileaf.alice.cards.colorless;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.patches.card.LoopSCVPreviewCardsPatch;
import me.antileaf.alice.utils.AliceHelper;

import java.util.Arrays;

public class CreateDoll extends AbstractAliceCard {
	public static final String SIMPLE_NAME = CreateDoll.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

//	public static ArrayList<CreateDoll> previews;
//	public static int currentPreview = 0;
//	public static float previewTimer = -999.0F;

	public String dollClazz = null;

	public CreateDoll() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				-2,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				CardColor.COLORLESS, // AbstractCardEnum.ALICE_MARGATROID_DERIVATION_COLOR,
				CardRarity.SPECIAL,
				CardTarget.NONE
		);
	}

	public CreateDoll(String dollClazz) {
		this();

		this.dollClazz = dollClazz;
		this.initializeDescription();

		if (Gdx.files.internal(AliceHelper.getCardImgFilePath(SIMPLE_NAME + "_" + dollClazz)).exists())
			this.loadCardImage(AliceHelper.getCardImgFilePath(SIMPLE_NAME + "_" + dollClazz));
	}

	public CreateDoll(String dollClazz, int index) {
		this(dollClazz);
		this.magicNumber = this.baseMagicNumber = index + 1;
		this.cost = this.costForTurn = index + 1;
		this.initializeDescription();
	}

	@Override
	public void initializeDescription() {
		this.name = this.dollClazz == null ? cardStrings.NAME : AbstractDoll.getKeyword(this.dollClazz);
		this.rawDescription = String.format(this.magicNumber == -1 ?
						cardStrings.DESCRIPTION : cardStrings.UPGRADE_DESCRIPTION,
				this.dollClazz == null ?
						cardStrings.EXTENDED_DESCRIPTION[0] :
						"alicemargatroid:" + AbstractDoll.getKeyword(this.dollClazz));

		super.initializeDescription();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}
	
	@Override
	public boolean canUpgrade() {
		return false;
	}
	
	@Override
	public void upgrade() {
		// No upgrade.
	}

	public static void registerLoopPreview() {
		LoopSCVPreviewCardsPatch.register(CreateDoll.class,
				Arrays.stream(AbstractDoll.dollClasses)
						.map(CreateDoll::new)
						.toArray(AbstractCard[]::new));
	}

//	private static AbstractCard getCurrentPreview() {
//		if (previewTimer > -999.0F) {
//			previewTimer -= Gdx.graphics.getDeltaTime();
//			if (previewTimer <= 0.0F) {
//				currentPreview++;
//				if (currentPreview >= previews.size())
//					currentPreview = 0;
//				previewTimer = 1.0F;
//			}
//		}
//
//		return previews.get(currentPreview);
//	}

//	@Override
//	public void renderSCVPreview(SpriteBatch sb) {
//		this.cardsToPreview = getCurrentPreview();
//		super.renderCardPreviewInSingleView(sb);
//		this.cardsToPreview = null;
//	}
}
