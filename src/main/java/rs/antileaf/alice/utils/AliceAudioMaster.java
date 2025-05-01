package rs.antileaf.alice.utils;

import basemod.BaseMod;

public class AliceAudioMaster {
	public static final String CHAR_SELECT_1 = "AliceMargatroid:CHAR_SELECT_1";
	public static final String CHAR_SELECT_2 = "AliceMargatroid:CHAR_SELECT_2";
	public static final String CHAR_SELECT_3 = "AliceMargatroid:CHAR_SELECT_3";
	public static final String ARIS_SELECT_1 = "AliceMargatroid:ARIS_SELECT_1";
	public static final String ARIS_SELECT_2 = "AliceMargatroid:ARIS_SELECT_2";
	public static final String EASTER_EGG = "AliceMargatroid:EASTER_EGG";
	public static final String ARIS_COMMON_SKILL = "AliceMargatroid:ARIS_COMMON_SKILL";
	public static final String ALICE_IN_WONDERLAND = "AliceMargatroid:ALICE_IN_WONDERLAND";
	public static final String RESURGENCE = "AliceMargatroid:RESURGENCE";
	
	public static void init() {
		BaseMod.addAudio(CHAR_SELECT_1, "AliceMargatroidMod/audio/charSelect/SELECT_ALICE1.wav");
		BaseMod.addAudio(CHAR_SELECT_2, "AliceMargatroidMod/audio/charSelect/SELECT_ALICE2.wav");
		BaseMod.addAudio(CHAR_SELECT_3, "AliceMargatroidMod/audio/charSelect/SELECT_ALICE3.wav");
		BaseMod.addAudio(CHAR_SELECT_3, "AliceMargatroidMod/audio/charSelect/SELECT_ALICE3.wav");
		BaseMod.addAudio(ARIS_SELECT_1, "AliceMargatroidMod/audio/charSelect/ARIS_1.mp3");
		BaseMod.addAudio(ARIS_SELECT_2, "AliceMargatroidMod/audio/charSelect/ARIS_2.ogg");
		BaseMod.addAudio(EASTER_EGG, "AliceMargatroidMod/audio/charSelect/panpankapan.ogg");
		BaseMod.addAudio(ARIS_COMMON_SKILL, "AliceMargatroidMod/audio/charSelect/CommonSkill.ogg");
		BaseMod.addAudio(ALICE_IN_WONDERLAND, "AliceMargatroidMod/audio/effects/AliceInWonderland.wav");
		BaseMod.addAudio(RESURGENCE, "AliceMargatroidMod/audio/effects/Resurgence.wav");
	}
}
