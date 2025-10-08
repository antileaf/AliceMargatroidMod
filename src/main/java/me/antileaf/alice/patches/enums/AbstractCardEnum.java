package me.antileaf.alice.patches.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AbstractCardEnum {
	@SpireEnum
	public static AbstractCard.CardColor ALICE_MARGATROID_COLOR;
	
//  @SpireEnum(name = "Alice Derivation")
//  public static AbstractCard.CardColor ALICE_MARGATROID_DERIVATION_COLOR;
//
//  @SpireEnum(name = "Alice Loli Cards")
//  public static AbstractCard.CardColor ALICE_LOLI_COLOR;

	@SpireEnum // (name = "Marisa Derivation")
//	@NoLibraryType
	public static AbstractCard.CardColor ALICE_MARISA_COLOR;
	
	@SpireEnum
	public static AbstractCard.CardColor ALICE_MEDICINE_COLOR;
}
