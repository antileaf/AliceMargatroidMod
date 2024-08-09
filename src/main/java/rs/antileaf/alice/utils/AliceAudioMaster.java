package rs.antileaf.alice.utils;

import basemod.BaseMod;

public class AliceAudioMaster {
	public static String CHAR_SELECT_1 = "AliceMargatroidMod:CHAR_SELECT_1";
	public static String CHAR_SELECT_2 = "AliceMargatroidMod:CHAR_SELECT_2";
	public static String CHAR_SELECT_3 = "AliceMargatroidMod:CHAR_SELECT_3";
	public static String ALICE_IN_WONDERLAND = "AliceMargatroidMod:ALICE_IN_WONDERLAND";
	public static String RESURGENCE = "AliceMargatroidMod:RESURGENCE";
	
	public static void init() {
		BaseMod.addAudio(CHAR_SELECT_1, "AliceMargatroidMod/audio/charSelect/SELECT_ALICE1.wav");
		BaseMod.addAudio(CHAR_SELECT_2, "AliceMargatroidMod/audio/charSelect/SELECT_ALICE2.wav");
		BaseMod.addAudio(CHAR_SELECT_3, "AliceMargatroidMod/audio/charSelect/SELECT_ALICE3.wav");
		BaseMod.addAudio(ALICE_IN_WONDERLAND, "AliceMargatroidMod/audio/effects/AliceInWonderland.wav");
		BaseMod.addAudio(RESURGENCE, "AliceMargatroidMod/audio/effects/Resurgence.wav");
	}
}
