package me.antileaf.alice.patches.enums;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoLibraryType;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class LibraryTypeEnum {
	@SpireEnum
	public static CardLibrary.LibraryType ALICE_MARGATROID_COLOR;
	
//	@SpireEnum(name = "Alice Derivation")
//	public static CardLibrary.LibraryType ALICE_MARGATROID_DERIVATION_COLOR;
	
	@SpireEnum // (name = "Marisa Derivation")
	@NoLibraryType
	public static CardLibrary.LibraryType ALICE_MARISA_COLOR;
	
//	@SpireEnum
//	public static AbstractCard.CardColor ALICE_MEDICINE_COLOR;
}
