package com.dualand.app.activities

import com.dualand.app.R

data class Dua(
    val arabic: String,
    val translation: String,
    val hinditranslation: String,
    val reference: String,
    val backgroundResId: Int,
    val statusBarColorResId: Int,
    val fullAudioResId: Int,
    val image: Int,
    val textheading: String,
    val duaNumber: String,
    val steps: String? = null ,
    val wordAudioPairs: List<Pair<String, Int>>
)

val duaList = listOf(
    Dua(
        arabic = "سُبْحَانَ اللّٰہِ وَبِحَمْدِہِ سُبْحَانَ اللّٰہِ الْعَظِیْمِ",
        translation = "Glory be to Allah and all praise be to Him; Glory be to Allah, the Most Great.",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        reference = "[Ṣaḥīḥ Muslim]",
        textheading = "Praise and Glory",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        image = R.drawable.kaaba,
        duaNumber = "1. ",
        fullAudioResId = R.raw.dua01_part01_audio01,
        wordAudioPairs = listOf(
            "سُبْحَانَ اللّٰہِ" to R.raw.dua01_part01_audio01,
            "وَبِحَمْدِہِ" to R.raw.dua01_part01_audio02,
            "سُبْحَانَ اللّٰہِ الْعَظِیْمِ" to R.raw.dua01_part01_audio03,

            )
    ),
    Dua(
        arabic = "اَللّٰہُ اَکْبَرُکَبِیْرًاوَّالْحَمْدُ لِلّٰہِ کَثِیْرًاوَّسُبْحَانَ اللّٰہِ بُکْرَةً وَّاَصِیْلًا",
        translation = "Allah is truly Great, praise be to Allah in abundance and glory be to Allah in the morning and the evening.",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        reference = "[Sahih Muslim]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua01_part02_audio01,
        image = R.drawable.kaaba,
        duaNumber = "1. ",
        textheading = "Praise and Glory",
        wordAudioPairs = listOf(
            "اَللّٰہُ اَکْبَرُکَبِیْرًا" to R.raw.dua01_part02_audio01,
            "وَّالْحَمْدُ لِلّٰہِ کَثِیْرًا" to R.raw.dua01_part02_audio02,
            "وَّسُبْحَانَ اللّٰہِ" to R.raw.dua01_part02_audio03,
            "بُکْرَةً وَّاَصِیْلًا۔" to R.raw.dua01_part02_audio04,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ وَّعَلٰی آلِ مُحَمَّدٍِ",
        translation = "O Allah! bestow Your mercy upon Mohammad ﷺ and upon the family of Mohammad ﷺ.",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        reference = "[Sunan al-Nasā‘ī]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua03_part01_audio01,
        duaNumber = "2. ",
        textheading = "Peace and Blessing upon\n" + "the Prophet Muhammad ﷺ",
        image = R.drawable.dua_2,
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ" to R.raw.dua03_part01_audio01,
            "صَلِّ عَلٰی" to R.raw.dua03_part01_audio02,
            "مُحَمَّدٍ وَّعَلٰیَ" to R.raw.dua03_part01_audio03,
            "آلِ مُحَمَّدَ" to R.raw.dua03_part01_audio04,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ عَبْدِکَ وَ رَسُوْلِکَ کَمَا صَلَّیْتَ عَلٰی اِبْرَاھِیْمَ وَ بَارِکْ عَلٰی مُحَمَّدٍ وَّعَلٰی آلِ مُحَمَّدٍ کَمَا بَارَکْتَ عَلٰی اِبْرَاھِیْمَ وَآلِ اِبْرَاھِیْمَِ",
        translation = "O Allah! Bestow mercy upon your servant and messenger, Mohammad ﷺ, as You bestowed Your mercy upon Ibrahim AS and bless Mohammad ﷺ and the family of Mohammad ﷺ,as You blessed Ibrahim (AS) and the family Ibrahim (AS). ",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua03_part01_audio01,
        textheading = "Peace and Blessing upon\n" + "the Prophet Muhammad ﷺ",
        image = R.drawable.dua_2,
        duaNumber = "2. ",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ" to R.raw.dua03_part01_audio01,
            "صَلِّ عَلٰی مُحَمَّدٍ" to R.raw.dua03_part01_audio01,
            "عَبْدِکَ وَ رَسُوْلِکَ" to R.raw.dua03_part01_audio01,
            "کَمَا صَلَّیْتَ عَلٰی اِبْرَاھِیْمَ" to R.raw.dua03_part01_audio01,
            "وَ بَارِکْ عَلٰی مُحَمَّدٍ" to R.raw.dua03_part01_audio01,
            "وَّعَلٰی آلِ مُحَمَّدٍ" to R.raw.dua03_part01_audio01,
            "کَمَا بَارَکْتَ عَلٰی اِبْرَاھِیْمَ" to R.raw.dua03_part01_audio01,
            "وَآلِ اِبْرَاھِیْمَِ" to R.raw.dua03_part01_audio01
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ بِکَ اَصْبَحْنَا وَ بِکَ اَمْسَیْنَا وَ بِکَ نَحْیَا وَ بِکَ نَمُوْتُ وَ إِلَـيْكَ اَلْمَصِیْرِ",
        translation = "O Allah, by Your leave we have reached the morning and by Your leave we have reached the evening and by Your leave we live and die, and unto You is our return.",
        reference = " [Sunan al-Tirmidhī]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua03_part01_audio01,
        textheading = "Du’a of Morning\n" +
                "(Before Sunrise)",
        image = R.drawable.dua_3,
        duaNumber = "3. ",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ بِکَ اَصْبَحْنَا" to R.raw.dua03_part01_audio01,
            "وَبِکَ اَمْسَیْنَا" to R.raw.dua03_part01_audio02,
            "وَبِکَ نَحْیَاَُ" to R.raw.dua03_part01_audio03,
            "وَبِکَ نَمُوْتَُ" to R.raw.dua03_part01_audio04,
            "وَإِلَـيْكَ اَلْمَصِیْر" to R.raw.dua03_part01_audio05,

            )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ بِکَ اَمْسَیْنَا وَبِکَ اَصْبَحْنَا وَبِکَ نَحْیَا وَبِکَ نَمُوْتُ وَاِلَیْکَ النُّشُوْرُ۔ِ",
        translation = "O Allah! By Your leave we reach the evening\n" +
                "and by Your leave we reach the morning\n "+"" +
                " and by Your leave we live and by Your leave we\n"+
                "will die and to You is our resurrection.",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        reference = "[Sunan al-Tirmidhī]",
        backgroundResId = R.drawable.header_bg,
        duaNumber = "4. ",
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua04_part01_audio01,
        textheading = "Du’a of Evening\n" +
                "(Before Sunset)",
        image = R.drawable.dua_4,
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ بِکَ اَمْسَیْنَا" to R.raw.dua04_part01_audio01,
            "وَبِکَ اَصْبَحْنَا" to R.raw.dua04_part01_audio02,
            "وَبِکَ نَحْیَاُ" to R.raw.dua04_part01_audio03,
            "وَبِکَ نَمُوْتَُُ" to R.raw.dua04_part01_audio04,
            " وَاِلَیْکَ النُّشُوْرُ۔" to R.raw.dua04_part01_audio05,
        )
    ),
    Dua(
        arabic = "اَعُوْذُ بِکَلِمَاتِ اللّٰہِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
        translation = "I seek refuge in the complete, perfect words of\n" +
                "Allah from the evil of what He has created.",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        reference = "[Ṣaḥīḥ Muslim]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua05_part01_audio01,
        textheading = "Du’a for Protection",
        duaNumber = "5. ",
        image = R.drawable.dua_5,
        wordAudioPairs = listOf(
            "اَعُوْذُ" to R.raw.dua05_part01_audio01,
            " بِکَلِمَاتِ اللّٰہِ التَّامَّاتِ " to R.raw.dua05_part01_audio02,
            " مِنْ شَرِّ" to R.raw.dua05_part01_audio03,
            "مَا خَلَق" to R.raw.dua05_part01_audio04,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
        translation = "In the name of Allah, by Whose name nothing on the earth or in the heavens can cause harm,and He is the All Knowing, the All-Hearing. (3 times)",
        reference = "[Sunan Abu Dawud]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua05_part02_audio01,
        image = R.drawable.dua_5,
        duaNumber = "5. ",
        textheading = "Du’a for Protection",
        wordAudioPairs = listOf(
            "بِسْمِ اللَّهِ الَّذِيِ" to R.raw.dua05_part02_audio01,
            " لَا يَضُرُّ" to R.raw.dua05_part02_audio02,
            " مَعَ اسْمِهِ" to R.raw.dua05_part02_audio03,
            "شَيْءٌ فِي الْأَرْضِ ِ" to R.raw.dua05_part02_audio04,
            "وَلَا فِي السَّمَاءِ " to R.raw.dua05_part02_audio05,
            "وَهُوَ السَّمِيعُ الْعَلِيم" to R.raw.dua05_part02_audio06,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ بِاسْمِكَ اَمُوْتُ وَاَحْیَا",
        translation = "O Allah! In your name I die and I live.",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua06_part01_audio01,
        image = R.drawable.dua_6,
        duaNumber = "6. ",
        textheading = "Du’a before Sleeping",
        steps = "1. Dust the bed before sleeping\n" +
                "2. Sleep on the right side\n" +
                "3. Put your hand under your right\ncheek and say",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ" to R.raw.dua06_part01_audio01,
            "بِاسْمِكَ" to R.raw.dua06_part01_audio02,
            "اَمُوْتُ" to R.raw.dua06_part01_audio03,
            "وَاَحْیَا" to R.raw.dua06_part01_audio04
        )
    ),
    Dua(
        arabic = "اَلْـحَمْدُ لِلّٰهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورَُِ",
        translation = "All praise is for Allah ho gave us life after death\n" +
                "(sleep) and to Him is the resurrection.",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua07_part01_audio01,
        image = R.drawable.dua_7,
        duaNumber = "7. ",
        textheading = "Du’a aftar Waking Up",
        steps = "Rub your face and your eyes with your hands to\n" +
                "remove any remaining effects of sleep and say:",
        wordAudioPairs = listOf(
            " اَلْـحَمْدُ لِلّٰه الَّذِي" to R.raw.dua07_part01_audio01,
            " أَحْيَانَاِ" to R.raw.dua07_part01_audio02,
            "بَعْدَ مَا أَمَاتَنَاِ" to R.raw.dua07_part01_audio03,
            "وَإِلَيْهِ النُّشُور" to R.raw.dua07_part01_audio04,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ اِنِّیْ اَعُوْذُبِکَ مِنَ الْخُبُثِ وَ الْخَبَائِثِِ",
        translation = "O Allah, indeed I seek refuge in You from the\n" +
                "impure male jinns and impure female jinns.",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua08_part01_audio01,
        textheading = " Du’a before Entering\nthe Toilet",
        image = R.drawable.dua_8,
        duaNumber = "8. ",
        steps = "Enter with your left foot and say:",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ اِنِّیْ اَعُوْذُبِکَ" to R.raw.dua08_part01_audio01,
            "مِنَ الْخُبُثِ" to R.raw.dua08_part01_audio02,
            "وَ الْخَبَائِثَ" to R.raw.dua08_part01_audio03,
        )
    ),
    Dua(
        arabic = "غُفْرَانَكَ",
        translation = "I ask you (Allah) for forgiveness.",
        reference = "[Sunan Abu Dawud]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua09_part01_audio01,
        image = R.drawable.dua_9,
        duaNumber = "9. ",
        textheading = "Du’a after Leaving \n" +
                "the Toilet",
        steps = "Leave with your right foot and say:",
        wordAudioPairs = listOf(
            "غُفْرَانَكَ " to R.raw.dua09_part01_audio01,
        )
    ),
    Dua(
        arabic = "الحَمْدُ لِلّٰهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍُِِ",
        translation = "All praise is for Allah who has clothed me with\n" +
                "this garment and provided it for me, with no\n" +
                "power or might from myself. ",
        reference = "[Sunan Abu Dawūd]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua10_part01_audio01,
        duaNumber = "10. ",
        textheading = "Du’a before Putting\n" +
                "on Dress",
        steps = "1. Shake and dust your dress before wearing it.\n" +
                "2. Start wearing the cloth from the right side (right\n" +
                "sleeve, right side of the trouser, right socks, etc.).",
        image = R.drawable.dua_10,
        wordAudioPairs = listOf(
            "الحَمْدُ لِلّٰهِ الَّذِي" to R.raw.dua10_part01_audio01,
            "كَسَانِي " to R.raw.dua10_part01_audio02,
            " هَذَاالثَّوْبَ " to R.raw.dua10_part01_audio03,
            " وَرَزَقَنِيهِ" to R.raw.dua10_part01_audio04,
            "مِنْ غَيْرِ" to R.raw.dua10_part01_audio05,
            " حَوْلٍ مِنِّي " to R.raw.dua10_part01_audio06,
            "  وَلَا قُوَّة " to R.raw.dua10_part01_audio07,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہُِِِِ",
        translation = "In the Name of Allah.",
        reference = "[Ṣaḥīḥ Jāmi’ al-Ṣaghīr]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua11_part01_audio01,
        image = R.drawable.dua_11,
        duaNumber = "11. ",
        steps = "Start to undress from the left side\n" + "(left sleeve, left side of the trouser, left sock etc.",
        textheading = "Du’a before Taking \n" +
                "off Dress",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua11_part01_audio01,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہِ تَوَکَّلْتُ عَلَی اللّٰہِ لَاحَوْلَ وَلَا قُوَّةَ اِلَّا بِاللّٰہِ ُِِِِ",
        translation = "In the name of Allah, I place my trust in Allah, and\n" +
                "there is no might nor power except with Allah.",
        reference = "[Sunan Abu Dawūd]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua12_part01_audio01,
        image = R.drawable.dua_12,
        duaNumber = "12. ",
        textheading = "Du’a before Leaving\n" +
                "the Home",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua12_part01_audio01,
            "تَوَکَّلْتُِ" to R.raw.dua12_part01_audio02,
            "عَلَی اللّٰہِ" to R.raw.dua12_part01_audio03,
            "لَاحَوْلََ" to R.raw.dua12_part01_audio04,
            "وَلَا قُوَّةَ" to R.raw.dua12_part01_audio05,
            "اِلَّا بِاللّٰہِ" to R.raw.dua12_part01_audio06,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہِ",
        translation = "In the Name of Allah ",
        reference = "[Ṣaḥīḥ Muslim] ",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua13_part01_audio01,
        image = R.drawable.dua_13,
        duaNumber = "13. ",
        textheading = "Du’a before Entering\n" +
                "the Home",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua13_part01_audio01,
        )
    ),
    Dua(
        arabic = "اَلسَّلَامُ عَلَیْکُمْ",
        translation = "May Peace (of Allah) be upon you.",
        reference = "[al-Nūr: 27]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua13_part02_audio01,
        image = R.drawable.dua_13,
        duaNumber = "13. ",
        textheading = "Du’a before Entering\n" +
                "the Home",
        wordAudioPairs = listOf(
            "اَلسَّلَامُ عَلَیْکُمِ" to R.raw.dua13_part02_audio01,
        )
    ),
    Dua(
        arabic = "اَللّٰهُمَّ إِنِّیْ أَعُوْذُبِکَ مِنْ مُنْکَرَاتِ الْأخلاَ قِ، وَالْأعْمَالِ، وَالْأھْوَاءِِ",
        translation = "O Allah! Verily, I seek refuge in You from bad \n" +
                "manners, deeds and desires.",
        reference = "[Sunan al-Tirmidhī]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua14_part01_audio01,
        image = R.drawable.dua_14,
        duaNumber = "14. ",
        textheading = "Du’a from protection\n" +
                "from Bad Akhlaq",
        wordAudioPairs = listOf(
            "اَللّٰهُمَّ إِنِّیْ أَعُوْذُبِکَ" to R.raw.dua14_part01_audio01,
            "  مِنْ مُنْکَرَاتِ الْأخلاَ قِ، " to R.raw.dua14_part01_audio02,
            "وَالْأعْمَالِ، " to R.raw.dua14_part01_audio03,
            "وَالْأھْوَاءِِ " to R.raw.dua14_part01_audio04,
        )
    ),
    Dua(
        arabic = "أَسْتَوْدِعُ اللَّهَ دِينَكَ وَأَمَانَتَكَ، وَخَوَاتِيمَ عَمَلِكَِِ",
        translation = "(I make) Allah responsible for your deen, your trustsworthiness\n" +
                "and for the results of your actions",
        reference = "[Sunan al-Tirmidhi]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua14_part01_audio01,
        image = R.drawable.dua_travel,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "15. ",
        textheading = "Du’a for Traveller",
        wordAudioPairs = listOf(
            "أَسْتَوْدِعُ اللَّهَ" to R.raw.dua14_part01_audio01,
            "دِينَكَ وَأَمَانَتَكَ،" to R.raw.dua14_part01_audio02,
            "وَخَوَاتِيمَ عَمَلِك" to R.raw.dua14_part01_audio03,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہِ",
        translation = "In the Name of Allah ",
        reference = " [Sunan Abu Dawūd]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua15_part01_audio01,
        image = R.drawable.dua_15,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "16. ",
        textheading = "Du’a for Journey",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua15_part01_audio01,
        )
    ),
    Dua(
        arabic = "اَلْحَمْدُ لِلّٰہِ",
        translation = "All Praise is for Allah.",
        reference = " [Sunan Abu Dawūd]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua15_part02_audio01,
        image = R.drawable.dua_15,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "16. ",
        textheading = "Du’a for Journey",
        wordAudioPairs = listOf(
            "اَلْحَمْدُ لِلّٰہ" to R.raw.dua15_part02_audio01,
        )
    ),
    Dua(
        arabic = "سُبْحٰنَ الَّذِیْ سَخَّرَلَنَاھٰذَاوَمَاکُنَّا لَہ مُقْرِنِیْنَoوَاِنَّآاِلٰی رَبِّنَا لَمُنْقَلِبُوْنَ oِِ",
        translation = "Glory to Him Who created this transportation for us, though we were unable to create it on our own.And to our Lord we shall return.",
        reference = " [al-Zukhruf: 13-14]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua15_part03_audio01,
        image = R.drawable.dua_15,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "16. ",
        textheading = "Du’a for Journey",
        wordAudioPairs = listOf(
            "سُبْحٰنَ الَّذِیْ " to R.raw.dua15_part03_audio01,
            "سَخَّرَلَنَا " to R.raw.dua15_part03_audio02,
            "ھٰذَا " to R.raw.dua15_part03_audio04,
            "وَمَاکُنَّاْْ " to R.raw.dua15_part03_audio05,
            "لَہ مُقْرِنِیْنَo" to R.raw.dua15_part03_audio06,
            "وَاِنَّآاِلٰی" to R.raw.dua15_part03_audio07,
            "رَبِّنَا" to R.raw.dua15_part03_audio08,
            "لَمُنْقَلِبُوْنَ o" to R.raw.dua15_part03_audio09,
        )
    ),
    Dua(
        arabic = "اَلسَّلَامُ عَلَیْکُمْ وَرَحْمَةُ اللّٰہِ وَ بَرَکَاتُہُِِ",
        translation = "May peace, mercy and blessings\n"+ "(of Allah) be upon You.",
        reference = "[Sunan Abu Dawūd]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua16_part01_audio01,
        image = R.drawable.dua_16,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "17. ",
        textheading = "Du’a When Meeting\n" +
                "a Muslim",
        wordAudioPairs = listOf(
            "اَلسَّلَامُ عَلَیْکُمْ وَرَحْمَةُ اللّٰہِ وَ بَرَکَاتُہْ" to R.raw.dua16_part01_audio01

        )
    ),
    Dua(
        arabic = "وَعَلَیْکُمُ السَّلَامُ وَ رَ حْمَةُ اللّٰہِ وَ بَرَکَاتُہُُِِ",
        translation = "May peace, mercy and blessings\n"+ "(of Allah) be upon You as Well.",
        reference = "[Sunan Abu Dawūd]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua16_part02_audio01,
        image = R.drawable.dua_16,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "17. ",
        textheading = "Du’a When Meeting\n" +
                "a Muslim",
        wordAudioPairs = listOf(
            "وَعَلَیْکُمُ السَّلَامُ وَ رَ حْمَةُ اللّٰہِ وَ بَرَکَاتُہ" to R.raw.dua16_part02_audio01
        )
    ),


    Dua(
        arabic = "سُبْحَانَکَ اللّٰھُمَّ وَبِحَمْدِکَ،اَشْھَدُاَنْ لَّااِلٰہَ اِلَّااَنْتَ، اَسْتَغْفِرُکَ وَاَتُوْبُ اِلَیْکَِ",
        translation = "Glory be to You O Allah, and all praise be to You! I\n" + "bear witness that there is no true deity except\n" +"You, I seek Your forgiveness and turn in\n" + "repentance to You.",
        reference = " [Sunan Abu Dawūd]",
        backgroundResId = R.drawable.header_bg,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua17_part01_audio01,
        image = R.drawable.dua_greeting,
        duaNumber = "18. ",
        textheading = "Du’a at the End of a\n" +"Gathering",
        wordAudioPairs = listOf(
            "سُبْحَانَکَ اللّٰھُمَّ وَبِحَمْدِکَ،َّ" to R.raw.dua17_part01_audio01,
            "اَشْھَدُاَنْ لَّااِلٰہَ اِلَّااَنْتَ،َّْ" to R.raw.dua17_part01_audio02,
            "اَسْتَغْفِرُکَ وَاَتُوْبُ اِلَیْک" to R.raw.dua17_part01_audio03,
        )
    ),
    Dua(
        arabic = "لَا اِلٰہَ اِلَّا اللّٰہُ وَحْدَہُ لَا شَرِیْکَ لَہُ،لَہُ الْمُلْکُ وَلَہُ الْحَمْدُ  یُحْیی وَ یُمِیْتُ وَھُوَ حَی لَّا یَمُوْتُ بِیَدِہِ الْخَیْرُ وَھُوَ عَلٰی کُلِّ شَیْئٍ قَدِیْرَِ",
        translation = "There is no true deity except Allah, alone, He has\n" +  "no partners. To Him belongs the dominion and\n" + "for Him is all praise. He gives life and causes\n" + "death,  and He is living and does not die. In His\n" + "hand is all good and He is upon all things always\n" + "All-Powerful.",
        reference = "[Sunan al-Tirmidhi]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua18_part01_audio01,
        image = R.drawable.dua_18,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "19. ",
        textheading = "Du’a When Entering the\n" + "Market",
        wordAudioPairs = listOf(
            "لَا اِلٰہَ اِلَّا اللّٰہُ" to R.raw.dua18_part01_audio01,
            "وَحْدَہُ لَا شَرِیْکَ لَہُ،" to R.raw.dua18_part01_audio02,
            "لَہُ الْمُلْکُ " to R.raw.dua18_part01_audio03,
            "وَلَہُ الْحَمْد" to R.raw.dua18_part01_audio04,
            " یُحْیی " to R.raw.dua18_part01_audio05,
            " وَ یُمِیْتُ " to R.raw.dua18_part01_audio06,
            "وَھُوَ حَی لَّا یَمُوْتُ" to R.raw.dua18_part01_audio07,
            "  بِیَدِہِ الْخَیْرُ" to R.raw.dua18_part01_audio08,
            "وَھُوَ عَلٰی کُلِّ شَیْئٍ قَدِیْرُ" to R.raw.dua18_part01_audio09,

        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ افْتَحْ لِیْ اَبْوَابَ رَحْمَتِکَُُِِ",
        translation = "O Allah, open for me the doors of your mercy.",
        reference = "[Sunan al-Nasa’i]",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua19_part01_audio01,
        image = R.drawable.dua_19,
        duaNumber = "20. ",
        steps = "Enter the Masjid with the right foot and say:",
        textheading = " Du’a When Entering\nthe Masjid",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ افْتَحْ لِیْ" to R.raw.dua19_part01_audio01,
            "اَبْوَابَ رَحْمَتِکَ" to R.raw.dua19_part01_audio02,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ اِنِّی اَسْئَلُکَ مِنْ فَضْلِکَُُِِ",
        translation = "O Allah, I ask you of Your bounties.",
        reference = " [Sahih Muslim]",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua20_part01_audio01,
        image = R.drawable.dua_20,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "21. ",
        steps = "Leave the masjid with the left foot and say:",
        textheading = "Du’a When Leaving\n" +
                "the Masjid",
        wordAudioPairs = listOf(
            " اَللّٰھُمَّ اِنِّی اَسْئَلُکَ" to R.raw.dua20_part01_audio01,
            "مِنْ فَضْلِکَُُِِ" to R.raw.dua20_part01_audio02,
        )
    ), Dua(
        arabic = "بِسْمِ اللّٰہَُُِِِ",
        translation = "In the Name of Allah.",
        reference = "  [Sunan Ibn Majah] ",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua12_part01_audio01,
        image = R.drawable.dua_21,
        duaNumber = "22. ",
        steps = "1. Sit while eating and drinking\n" +"2. Eat with your right hand and eat from\n" + "what is in front of you and say:",
        textheading = "Du’a Before\n" +
                "Eating & Drinking",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہ" to R.raw.dua21_part01_audio01,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہِ فِی اَوَّلِہِ وَ آخِرِہَُُِِِ",
        translation = "In the name of Allah, at the beginning and\n" + "at the end.",
        reference = "  [Sunan Ibn Majah] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua21_part02_audio01,
        image = R.drawable.dua_21,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "22. ",
        steps = "3. If you forgot to read the Du'a at the beginning\n"+ "then upon remembering say:",
        textheading = "Du’a When Leaving\n" +
                "the Masjid",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua21_part02_audio01,
            " فِی اَوَّلِہ" to R.raw.dua21_part02_audio02,
            " وَآخِرِہِ" to R.raw.dua21_part02_audio03,
        )
    ),
    Dua(
        arabic = "اَلّٰھُمَّ بَارِکْ لَناَ فِیْہِ وَاَطْعِمْنَا خَیْرًا مِّنْہُ",
        translation = "O Allah, You grant us blessings in it and \n" +
                "grant us better than it.",
        reference = "[Sunan al-Tirmidhi] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_22,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "23. ",
        textheading = "Du’a after\n" +
                "Eating & Drinking",
        wordAudioPairs = listOf(
            "اَلّٰھُمَّ بَارِکْ لَناَ فِیْہِ  " to R.raw.dua22_part01_audio01,
            "وَاَطْعِمْنَا" to R.raw.dua22_part01_audio02,
            "خَیْرًا مِّنْہُ" to R.raw.dua22_part01_audio03,
        )
    ),
    Dua(
        arabic = "لَا بَأْسَ طَهُورٌ إِنْ شَاءَ اللَّهَُُُُِِِ",
        translation = "No harm, (this illness will be) a purification (from\n" + "sins), if Allah wills.",
        reference = " [Sahih al-Bukhari]  ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_23,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "24. ",
        textheading = "Du’a When Visiting\n" +
                "the Sick",
        wordAudioPairs = listOf(
            "لَا بَأْسَ طَهُور " to R.raw.dua22_part01_audio01,
            "إِنْ شَاءَ اللَّه" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
        arabic = "اَسْاَلُ اللّٰہَ الْعَظِیْمَ رَبَّ الْعَرْشِ الْعَظِیْمِ اَنْ یَّشْفِیَکََُُُُِِِ",
        translation = "I ask Allah, the Great, Lord of the Magnificent\n" + "Throne, to cure you.",
        reference = "[Sunan al-Tirmidhi ] ",
        backgroundResId = R.drawable.header_bg,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_23,
        duaNumber = "24. ",
        textheading = "Du’a When Visiting\n" +
                "the Sick",
        wordAudioPairs = listOf(
            "اَسْاَلُ اللّٰہَ " to R.raw.dua22_part01_audio01,
            "الْعَظِیْمَ رَبَّ الْعَرْشِ" to R.raw.dua22_part01_audio01,
            "لْعَظِیْمِ اَنْ یَّشْفِیَک" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
       arabic = "اِنَّا لِلّٰہِ وَاِنَّا اِلَیْہِ رَاجِعُوْنَ \u2028اَللّٰھُمَّ اَجِرْنِیْ فِیْ مُصِیْبَتِیْ وَاَخْلِفْ لِیْ خَیْرًا مِّنْھَا",
        translation = "Indeed we belong to Allah, and indeed to Him we will return. O Allah, recompense me for my affliction and replace it for me with something better.",
        reference = "[Sahih Muslim]  ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_24,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "25. ",
        textheading = "Du’a For one Afficted\n" +
                "by a Calamity",
            wordAudioPairs = listOf(
                "اِنَّا لِلّٰہِ" to R.raw.dua22_part01_audio01,
                "وَاِنَّا اِلَیْہِ رَاجِعُوْنَ" to R.raw.dua22_part01_audio01,
                "اَللّٰھُمَّ اَجِرْنِیْ" to R.raw.dua22_part01_audio01,
                "فِیْ مُصِیْبَتِیْ" to R.raw.dua22_part01_audio01,
                "وَأَخْلِفْ لِیْ" to R.raw.dua22_part01_audio01,
                "خَیْرًا مِّنْهَا" to R.raw.dua22_part01_audio01
        )
    ),
    Dua(
       arabic = "اَلْحَمْدُ للهِ الَّذِیْ عَافَانِیْ مِمَّا ابْتَلَاکَ بِهِ وَ فَضَّلَنِیْ عَلٰی کَثِیْرٍ مِمَّنْ خَلَقَ تَفْضِیْلً",
        translation = "All praise is for Allah, Who saved me from that\n" +
                "which He tested you with and favoured me over\n" + "much of His creation.",
        reference = "[Sunan al-Tirmidhi] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "26. ",
        image = R.drawable.dua_25,
        textheading = "Du‘a upon Seeing \n" +
                "Someone in Calamity ",
            wordAudioPairs = listOf(
                "اَلْحَمْدُ للهِ الَّذِیِْ" to R.raw.dua22_part01_audio01,
                "عَافَانِیْ مِمَّا" to R.raw.dua22_part01_audio01,
                "ابْتَلَاکَ بِهِْ" to R.raw.dua22_part01_audio01,
                "وَ فَضَّلَنِیْْ" to R.raw.dua22_part01_audio01,
                "عَلٰی کَثِیْر" to R.raw.dua22_part01_audio01,
                "مِمَّنْ خَلَقَ تَفْضِیْل" to R.raw.dua22_part01_audio01
        )
    ),
    Dua(
       arabic = "اَعُوْذُ بِاللّٰہِ مِنَ الشَّیْطَانِ الرَّجِیْمًِ",
        translation = "I seek refuge in Allah from the Satan the rejected.",
        reference = " [Sahih al-Bukhari] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_26,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "27. ",
        steps = "Seek refuge with Allah from the Satan: ",
        textheading = "Du‘a when Angry",
            wordAudioPairs = listOf(
                "اَعُوْذُ بِاللّٰہِْ" to R.raw.dua22_part01_audio01,
                "مِنَ الشَّیْطَانِ" to R.raw.dua22_part01_audio01,
                "الرَّجِیْمًِِْ" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
       arabic = "اَلْحَمْدُ لِلّٰہًِِ",
        translation = "All praises to Allah.",
        reference = " [Sahih al-Bukhari] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_27,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "28. ",
        steps = "Say after sneezing: ",
        textheading = "Du‘a when Sneezing",
            wordAudioPairs = listOf(
                "اَلْحَمْدُ لِلّٰہِْ" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
       arabic = "یَرْحَمُکَ اللّٰہًُِِ",
        translation = "May Allah have mercy on you.",
        reference = " [Sahih al-Bukhari] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_27,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "28. ",
        steps = "Dua by one who hears someone saying اَلْحَمْدُ لِلّٰہِ",
        textheading = "Du‘a when Sneezing",
            wordAudioPairs = listOf(
                "یَرْحَمُکَ اللّٰہِْ" to R.raw.dua22_part01_audio01,
        )
    ), Dua(
       arabic = "یَھْدِیْکُمُ اللّٰہُ وَیُصْلِحُ بَالَکُمًِِْ",
        translation = "May Allah guide you and improve your position.",
        reference = " [Sahih al-Bukhari] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        duaNumber = "28. ",
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        image = R.drawable.dua_27,
        steps = "Du’a in reply by the person who sneezes:",
        textheading = "Du‘a when Sneezing",
            wordAudioPairs = listOf(
                "یَھْدِیْکُمُ اللّٰہُِْ" to R.raw.dua22_part01_audio01,
                " وَیُصْلِحُ بَالَکُم" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
       arabic = "اَلّٰھُمَّ اجْعَلْہُ  صَیِّباً  نَافِعاًًِِْ",
        translation = "O Allah, may it be a beneficial rain cloud.",
        reference = "[Sunan al-Nasa’i]  ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_28,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "29. ",
        textheading = "Du‘a when it Rains",
            wordAudioPairs = listOf(
                " اَلّٰھُمَّ اجْعَلْہُ" to R.raw.dua22_part01_audio01,
                "  صَیِّباً  نَافِعا" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
       arabic ="اَلّٰھُمَّ اَھْلِلْہُ عَلَیْنَا بِلْیُمْنِ وَالْاِیْمَانِ وَالسَّلَامَةِ وَالْاِسْلَامِ رَبِّیْ وَرَبُّکَ اللّٰہُ",
        translation = "O Allah! Let the moon appear over us with\n" + "security, faith, peace and Islam. [O moon!] My\n" + "Lord and your Lord is Allah. ",
        reference = "[Sunan al-Tirmidhi] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_29,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "30. ",
        textheading = "Du‘a upon\nSighting the Crescent Moon",
            wordAudioPairs = listOf(
                "  اَلّٰھُمَّ اَھْلِلْہُ" to R.raw.dua22_part01_audio01,
                "عَلَیْنَا بِلْیُمْنِ" to R.raw.dua22_part01_audio01,
                " وَالْاِیْمَانِ وَالسَّلَامَةِ" to R.raw.dua22_part01_audio01,
                "وَالْاِسْلَامِ رَبِّیْ" to R.raw.dua22_part01_audio01,
                " وَرَبُّکَ اللّٰہ" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
       arabic ="اللَّهُمَّ اِنِّىْ اَسْئَلُكَ حُبَّكَ وَ حُبَّ مَنْ يُّحِبُّكَ وحُبَّ عَمَلٍ يُّقَرِّبُ اِلى حُبِّكَُ",
        translation = "O Allah! I ask You for Your love and for the love of\n"+"the one who loves You and I ask for the deeds\n"+"which lead me towards Your love.",
        reference = "[Sunan al-Tirmidhi] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_31,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "31. ",
        textheading = "Du‘a for\nSeeking Allah’s Love",
            wordAudioPairs = listOf(
                "اللَّهُمَّ اِنِّىْ" to R.raw.dua22_part01_audio01,
                " اَسْئَلُكَ حُبَّكَ" to R.raw.dua22_part01_audio01,
                "  وَ حُبَّ مَنْ يُّحِبُّكَ" to R.raw.dua22_part01_audio01,
                " وحُبَّ عَمَلٍ يُّقَرِّبُ " to R.raw.dua22_part01_audio01,
                "   اِلى حُبِّك" to R.raw.dua22_part01_audio01,
        )
    ),
    Dua(
       arabic ="اَللّٰھُمَّ اَنْتَ رَبِّیْ،لَا اِلٰہَ اِلَّااَنْتَ خَلَقْتَنِیْ وَاَنَا عَبْدُکَ وَاَنَا عَلٰی عَھْدِکَ وَوَعْدِکَ مَااسْتَطَعْتُ اَعُوْذُبِکَ مِنْ شَرِّ مَاصَنَعْتُ اَبُوْئُ لَکَ بِنِعْمَتِکَ عَلَیَّ وَاَبُوْئُ بِذَنْبِیْ فَاغْفِرْ لِیْ اِنَّہُ لَا یَغْفِرُالذُّنُوْبَ اِلَّا اَنْتََُ",
        translation = "O Allah, You are my Lord; there is no deity worthy\n"+"of worship except You. You created me and I am\n"+"your servant, I abide by Your covenant and\n"+"promise to the best of my ability. I seek refuge \n" +
                "with You from the evil of which I have committed. \n" +
                "I acknowledge Your blessings upon me and I\n"+"acknowledge my sin, so forgive me, for verily\n"+"none forgives sins except You. ",
        reference = "[Sahih al-Bukhari] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_32,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "32. ",
        textheading = "Du‘a:\nSayyid-ul-Istighfar ",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ اَنْتَ رَبِّیْ" to R.raw.dua22_part01_audio01,
            "لَا اِلٰہَ اِلَّا اَنْتَ" to R.raw.dua22_part01_audio01,
            "خَلَقْتَنِیْ وَ اَنَا عَبْدُکَ" to R.raw.dua22_part01_audio01,
            "وَ اَنَا عَلٰی عَھْدِکَ وَ وَعْدِکَ مَا اسْتَطَعْتُ" to R.raw.dua22_part01_audio01,
            "اَعُوْذُ بِکَ مِنْ شَرِّ مَا صَنَعْتُ" to R.raw.dua22_part01_audio01,
            "اَبُوْءُ لَکَ بِنِعْمَتِکَ عَلَیَّ" to R.raw.dua22_part01_audio01,
            "وَ اَبُوْءُ بِذَنْبِیْ" to R.raw.dua22_part01_audio01,
            "فَاغْفِرْ لِیْ" to R.raw.dua22_part01_audio01,
            "اِنَّہُ لَا یَغْفِرُ الذُّنُوْبَ اِلَّا اَنْتَ" to R.raw.dua22_part01_audio01
        )

    ),
    Dua(
       arabic ="اَعُوْذُ بِوَجْہِ اللّٰہِ الْعَظِیْمِ الَّذِیْ لَیْسَ شَیْئ اَعْظَمَ مِنْہُ وَبِکَلِمَاتِ اللّٰہِ التَّامَّاتِ الَّتِیْ لَا یُجَاوِزُھُنَّ بَرّ وَّ لَا فَاجِر وَ بِاَسْمَائِ اللّٰہِ الْحُسْنٰی کُلِّھَا مَا عَلِمْتُ مِنْھَا وَ مَا لَمْ اَعْلَمْ مِنْ شَرِّ مَا خَلَقَ وَ بَرَاَ وَذَرَاَ",
        translation = "I seek refuge with the immense Face of Allah -\n" +
                "there is nothing greater than it - and with the \n"+"complete words of Allah which neither the good\n"+"person nor the corrupt can exceed and with all\n"+"the most beautiful names of Allah, what I know of\n"+"them and what I do not know, from the evil of\n"+"what He has created and originated and multiplied.",
        reference = "[Mishkat ul-Masabih] ",
        backgroundResId = R.drawable.header_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua22_part01_audio01,
        image = R.drawable.dua_33,
        hinditranslation = "ऐ अल्लाह, तेरी ही आज्ञा से हम सुबह तक पहुँचे हैं और तेरी ही आज्ञा से हम शाम तक पहुँचे हैं और तेरी ही आज्ञा से हम जीते हैं और मरते हैं, और तेरी ही ओर हमें लौटना है।",
        duaNumber = "33. ",
        textheading = "Seeking Refuge\nwith Allah",
        wordAudioPairs = listOf(
            "اَعُوْذُ بِوَجْہِ اللّٰہِ الْعَظِیْمِ" to R.raw.dua22_part01_audio01,
            "الَّذِیْ لَیْسَ شَیْئ اَعْظَمَ مِنْہُ" to R.raw.dua22_part01_audio01,
            "وَبِکَلِمَاتِ اللّٰہِ التَّامَّاتِ" to R.raw.dua22_part01_audio01,
            "الَّتِیْ لَا یُجَاوِزُھُنَّ بَرّ وَّ لَا فَاجِر" to R.raw.dua22_part01_audio01,
            "وَبِاَسْمَائِ اللّٰہِ الْحُسْنٰی کُلِّھَا" to R.raw.dua22_part01_audio01,
            "مَا عَلِمْتُ مِنْھَا وَمَا لَمْ اَعْلَمْ" to R.raw.dua22_part01_audio01,
            "مِنْ شَرِّ مَا خَلَقَ وَ بَرَاَ وَذَرَاَ" to R.raw.dua22_part01_audio01
        )
    ),
)
