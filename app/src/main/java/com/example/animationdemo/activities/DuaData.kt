package com.example.animationdemo.activities

import com.example.animationdemo.R

data class Dua(
    val arabic: String,
    val translation: String,
    val reference: String,
    val backgroundResId: Int,
    val statusBarColorResId: Int,
    val fullAudioResId: Int,
    val image: Int,
    val textheading: String,
    val steps: String? = null ,
    val wordAudioPairs: List<Pair<String, Int>>
)

val duaList = listOf(
    Dua(
        arabic = "سُبْحَانَ اللّٰہِ وَبِحَمْدِہِ سُبْحَانَ اللّٰہِ الْعَظِیْمِ",
        translation = "Glory be to Allah and all praise be to Him; Glory\nbe to Allah, the Most Great.",
        reference = "[Ṣaḥīḥ Muslim]",
        textheading = "Praise and Glory",
        backgroundResId = R.drawable.duaa1_bd,
        statusBarColorResId = R.color.top_nav_new,
        image = R.drawable.kaaba,
        fullAudioResId = R.raw.dua1_audio1,
        wordAudioPairs = listOf(
            "سُبْحَانَ اللّٰہِ" to R.raw.dua1_audio1,
            "وَبِحَمْدِہِ" to R.raw.dua1_audio2,
            "سُبْحَانَ اللّٰہِ الْعَظِیْمِ" to R.raw.dua1_audio3,

            )
    ),
    Dua(
        arabic = "اَللّٰہُ اَکْبَرُکَبِیْرًاوَّالْحَمْدُ لِلّٰہِ کَثِیْرًاوَّسُبْحَانَ اللّٰہِ بُکْرَةً وَّاَصِیْلًا",
        translation = "Allah is truly Great, praise be to Allah in\nabundance and glory be to Allah in the morning\nand the evening.",
        reference = "[Sahih Muslim]",
        backgroundResId = R.drawable.duaa1_bd,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio1,
        image = R.drawable.kaaba,
        textheading = "Praise and Glory",
        wordAudioPairs = listOf(
            "اَللّٰہُ اَکْبَرُکَبِیْرًا" to R.raw.dua2_audio1,
            "وَّالْحَمْدُ لِلّٰہِ کَثِیْرًا" to R.raw.dua2_audio2,
            "وَّسُبْحَانَ اللّٰہِ" to R.raw.dua2_audio3,
            "بُکْرَةً وَّاَصِیْلًا۔" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ وَّعَلٰی آلِ مُحَمَّدٍِ",
        translation = "O Allah! Bestow Your mercy upon Muhammad\nand upon the descendants of Muhammad.",
        reference = "[Sunan al-Nasā‘ī]",
        backgroundResId = R.drawable.duaa5_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Peace and Blessing upon\nthe Prophet Muhammad",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
//            "اَللّٰھُمَّ" to R.raw.dua2_audio4,
//            "صَلِّ عَلٰی" to R.raw.dua2_audio4,
//            "مُحَمَّدٍ وَّعَلٰیَ" to R.raw.dua2_audio4,
//            "آلِ مُحَمَّدَ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ عَبْدِکَ وَ رَسُوْلِکَ کَمَا صَلَّیْتَ عَلٰی اِبْرَاھِیْمَ وَ بَارِکْ عَلٰی مُحَمَّدٍ وَّعَلٰی آلِ مُحَمَّدٍ کَمَا بَارَکْتَ عَلٰی اِبْرَاھِیْمَ وَآلِ اِبْرَاھِیْمَِ",
        translation = "O Allah! Bestow Your mercy upon your servant\nand messenger, Muhammad as You bestowed upon\nIbrahim and bless Muhammad and the descendants of\nMuhammad, as You blessed Ibrahim and the descendants\nof Ibrahim.",
        reference = "[Sunan al-Nasā‘ī]",
        backgroundResId = R.drawable.duaa5_bg,
        statusBarColorResId = R.color.top_nav,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a of Morning",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
//            "اَللّٰھُمَّ" to R.raw.dua2_audio4,
//            "صَلِّ عَلٰی مُحَمَّدٍ" to R.raw.dua2_audio4,
//            "عَبْدِکَ وَ رَسُوْلِکَ" to R.raw.dua2_audio4,
//            "کَمَا صَلَّیْتَ عَلٰی اِبْرَاھِیْمَ" to R.raw.dua2_audio4,
//            "وَ بَارِکْ عَلٰی مُحَمَّدٍ" to R.raw.dua2_audio4,
//            "وَّعَلٰی آلِ مُحَمَّدٍ" to R.raw.dua2_audio4,
//            "کَمَا بَارَکْتَ عَلٰی اِبْرَاھِیْمَ" to R.raw.dua2_audio4,
//            "وَآلِ اِبْرَاھِیْمَِ" to R.raw.dua2_audio4
        )
    ),

    Dua(
        arabic = "اَللّٰھُمَّ بِکَ اَصْبَحْنَا وَ بِکَ اَمْسَیْنَا وَ بِکَ نَحْیَا وَ بِکَ نَمُوْتُ وَ إِلَـيْكَ اَلْمَصِیْرِ",
        translation = "O Allah, by Your leave we have reached the\nmorning and by Your leave we have reached the\nevening and by Your leave we live and die, and\nunto You is our return.",
        reference = " [Sunan al-Tirmidhī]",
        backgroundResId = R.drawable.dua_morning_bd,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a of Morning",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
//            "اَللّٰھُمَّ بِکَ اَصْبَحْنَا" to R.raw.dua2_audio4,
//            "وَبِکَ اَمْسَیْنَا" to R.raw.dua2_audio4,
//            "وَبِکَ نَحْیَاَُ" to R.raw.dua2_audio4,
//            "وَبِکَ نَمُوْتَُ" to R.raw.dua2_audio4,
//            "وَإِلَـيْكَ اَلْمَصِیْر" to R.raw.dua2_audio4,

            )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ بِکَ اَمْسَیْنَا وَبِکَ اَصْبَحْنَا وَبِکَ نَحْیَا وَبِکَ نَمُوْتُ وَاِلَیْکَ النُّشُوْرُ۔ِ",
        translation = "O Allah! By Your leave we reach the evening and\nby Your leave we reach the morning and by Your\nleave we live and by Your leave we will die and\nto You is our resurrection.",
        reference = "[Sunan al-Tirmidhī]",
        backgroundResId = R.drawable.duaa_evenning_bd,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a for Evening",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
//            "اَللّٰھُمَّ بِکَ اَمْسَیْنَا" to R.raw.dua2_audio4,
//            "وَبِکَ اَصْبَحْنَا" to R.raw.dua2_audio4,
//            "وَبِکَ نَحْیَاُ" to R.raw.dua2_audio4,
//            "وَبِکَ نَحْیَاَُ" to R.raw.dua2_audio4,
//            "وَبِکَ نَمُوْتَُُ" to R.raw.dua2_audio4,
//            "وَإِلَـيْكَ اَلْمَصِیْر" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَعُوْذُ بِکَلِمَاتِ اللّٰہِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
        translation = "I seek refuge in the complete, perfect words of\nAllah from the evil of what He has created.",
        reference = "[Ṣaḥīḥ Muslim]",
        backgroundResId = R.drawable.dua_protection,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a for Protection",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
//            "اَعُوْذُ" to R.raw.dua2_audio4,
//            "بِکَلِمَاتِ" to R.raw.dua2_audio4,
//            "اللّٰہِ" to R.raw.dua2_audio4,
//            "التَّامَّاتِ" to R.raw.dua2_audio4,
//            "مِنْ" to R.raw.dua2_audio4,
//            "شَرِّ" to R.raw.dua2_audio4,
//            "مَا" to R.raw.dua2_audio4,
//            "خَلَقَ" to R.raw.dua2_audio4
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ بِاسْمِكَ اَمُوْتُ وَاَحْیَا",
        translation = "O Allah! By Your name I die and by Your\nname I live.",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        backgroundResId = R.drawable.dua_sleeping_bg,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a for Sleeping",
        steps = "1. Dust the bed before sleeping\n2. Sleep on the right side\n3. Put your hand under your right\n    cheek and say",
        wordAudioPairs = listOf(
//            "اَللّٰھُمَّ" to R.raw.dua2_audio4,
//            "بِاسْمِكَ" to R.raw.dua2_audio4,
//            "اَمُوْتُ" to R.raw.dua2_audio4,
//            "وَاَحْیَا" to R.raw.dua2_audio4
        )
    ),
//    Dua(
//        arabic = "اَعُوْذُ بِکَلِمَاتِ اللّٰہِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَِ",
//        translation = "I seek refuge in the complete, perfect words of Allah from the evil of what He has created.",
//        reference = "[Sahih Muslim]",
//        backgroundResId = R.drawable.duaa6_bg,
//        statusBarColorResId = R.color.top_nav6,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "مِنْ شَرِّ مَا خَلَق" to R.raw.audioo1,
//            "اللّٰہِ التَّامَّاتِ" to R.raw.audioo2,
//            "اَعُوْذُ بِکَلِمَاتِ" to R.raw.audioo3,
//        )
//    ),
//    Dua(
//        arabic = "اَللّٰھُمَّ بِاسْمِكَ اَمُوْتُ وَاَحْیَاِ",
//        translation = "O Allah! In your name I die and I live.",
//        reference = "[Ṣaḥīḥ al-Bukhārī]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ),
//    Dua(
//        arabic = "اَلْحَمْدُ لِلهِ الَّذِیْ اَحْيَانَا بَعْدَ مَا اَمَاتَنَا وَاِلَيْهِ النُّشُوْرُِ",
//        translation = "All praise is for Allah ho gave us life after death (sleep) and to Him is the resurrection.",
//        reference = "[Sahih Muslim]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ),
//    Dua(
//        arabic = "اَللّٰھُمَّ اِنِّیْ اَعُوْذُبِکَ مِنَ الْخُبُثِ وَ الْخَبَائِثُِِ",
//        translation = "O Allah, indeed I seek refuge in You from the impure male jinns and impure female jinns.",
//        reference = "[Ṣaḥīḥ al-Bukhārī]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ),
//    Dua(
//        arabic = "سُبْحَانَ اللّٰہِ وَبِحَمْدِہِ سُبْحَانَ اللّٰہِ الْعَظِیْمُِِِ",
//        translation = "Glory be to Allah and all praise be to Him; Glory be to Allah, the Most Great.",
//        reference = "[Sahih Muslim]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ),
//    Dua(
//        arabic = "الحَمْدُ لِلهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍُِِِ",
//        translation = "All praise is for Allah who has clothed me with this garment and provided it for me, with no power or might from myself. ",
//        reference = "[Sahih Muslim]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ),
//    Dua(
//        arabic = "بِسْمِ اللّٰہُِِِِ",
//        translation = "In the Name of Allah.",
//        reference = "[Ṣaḥīḥ Jāmi’ al-Ṣaghīr]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ), Dua(
//        arabic = "بِسْمِ اللّٰہِ تَوَکَّلْتُ عَلَی اللّٰہِ لَاحَوْلَ وَلَا قُوَّةَ اِلَّا بِاللّٰہِ ُِِِِ",
//        translation = "All praise is for Allah who has clothed me with this garment and provided it for me, with no power or might from myself.",
//        reference = "[Sunan Abī Dawūd]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ), Dua(
//        arabic = " اَلسَّلَامُ عَلَیْکُمُِِِِْ",
//        translation = "May Peace (of Allah) be upon you.",
//        reference = "[al-Nūr: 27]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ), Dua(
//        arabic = "اَللّٰهُمَّ إِنِّیْ أَعُوْذُبِکَ مِنْ مُنْکَرَاتِ الْأخلاَ قِ، وَالْأعْمَالِ، وَالْأھْوَاءُِِِِِ",
//        translation = "O Allah! Indeed I seek refuge in You from a detestable conduct and from disliked deeds and desires.",
//        reference = "[Sunan al-Tirmidhī]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ), Dua(
//        arabic = "اَلسَّلَامُ عَلَیْکُمْ وَرَحْمَةُ اللّٰہِ وَ بَرَکَاتُہُُِِِِ",
//        translation = "May peace, mercy and blessings (of Allah) be upon You.",
//        reference = "[Sunan Abī Dawūd] ",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ),
//    Dua(
//        arabic = "الحَمْدُ لِلهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍُِِِِ",
//        translation = "All praise is for Allah who has clothed me with this garment and provided it for me, with no power or might from myself.",
//        reference = "[Sunan Abī Dawūd]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ), Dua(
//        arabic = "الحَمْدُ لِلهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍُِِِِ",
//        translation = "All praise is for Allah who has clothed me with this garment and provided it for me, with no power or might from myself. ",
//        reference = "[Sunan Abī Dawūd]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ), Dua(
//        arabic = "الحَمْدُ لِلهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍُِِِِ",
//        translation = "All praise is for Allah who has clothed me with this garment and provided it for me, with no power or might from myself. ",
//        reference = "[Sunan Abī Dawūd]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    ), Dua(
//        arabic = "الحَمْدُ لِلهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍُِِِِ",
//        translation = "All praise is for Allah who has clothed me with this garment and provided it for me, with no power or might from myself.",
//        reference = "[Sunan Abī Dawūd]",
//        backgroundResId = R.drawable.praiseandglory_bg,
//        statusBarColorResId = R.color.top_nav,
//        fullAudioResId = R.raw.dua1_part1_audio1,
//        image = R.drawable.kaaba,
//        wordAudioPairs = listOf(
//            "سُبْحَانَ اللّٰہ" to R.raw.audioo1,
//            "وَبِحَمْدِہِ" to R.raw.audioo2,
//            "سُبْحَانَ اللّٰہِ الْعَظِیْمَ" to R.raw.audioo3,
//        )
//    )
)
