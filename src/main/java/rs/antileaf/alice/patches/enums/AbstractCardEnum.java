package rs.antileaf.alice.patches.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AbstractCardEnum {

  @SpireEnum
  public static AbstractCard.CardColor ALICE_MARGATROID_COLOR;
  
  @SpireEnum(name = "Alice Derivation")
  public static AbstractCard.CardColor ALICE_MARGATROID_DERIVATION_COLOR;
  
  @SpireEnum(name = "Marisa Derivation")
  public static AbstractCard.CardColor ALICE_MARISA_COLOR;
}