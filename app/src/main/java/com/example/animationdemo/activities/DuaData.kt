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
        backgroundResId = R.drawable.dua1_new1_img,
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
        translation = "Allah is truly Great, praise be to Allah in abundance and glory be to Allah in the morning and the evening.",
        reference = "[Sahih Muslim]",
        backgroundResId = R.drawable.dua1_new1_img,
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
        backgroundResId = R.drawable.dua2_new2_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Peace and Blessing upon\nthe Prophet Muhammad",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ" to R.raw.dua2_audio4,
            "صَلِّ عَلٰی" to R.raw.dua2_audio4,
            "مُحَمَّدٍ وَّعَلٰیَ" to R.raw.dua2_audio4,
            "آلِ مُحَمَّدَ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ عَبْدِکَ وَ رَسُوْلِکَ کَمَا صَلَّیْتَ عَلٰی اِبْرَاھِیْمَ وَ بَارِکْ عَلٰی مُحَمَّدٍ وَّعَلٰی آلِ مُحَمَّدٍ کَمَا بَارَکْتَ عَلٰی اِبْرَاھِیْمَ وَآلِ اِبْرَاھِیْمَِ",
        translation = "O Allah! Bestow Your mercy upon your servant and messenger, Muhammad as You bestowed upon Ibrahim and bless Muhammad and the descendants of Muhammad, as You blessed Ibrahim and the descendants of Ibrahim.",
        reference = "[Sunan al-Nasā‘ī]",
        backgroundResId = R.drawable.dua2_new2_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Peace and Blessing upon\nthe Prophet Muhammad",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ" to R.raw.dua2_audio4,
            "صَلِّ عَلٰی مُحَمَّدٍ" to R.raw.dua2_audio4,
            "عَبْدِکَ وَ رَسُوْلِکَ" to R.raw.dua2_audio4,
            "کَمَا صَلَّیْتَ عَلٰی اِبْرَاھِیْمَ" to R.raw.dua2_audio4,
            "وَ بَارِکْ عَلٰی مُحَمَّدٍ" to R.raw.dua2_audio4,
            "وَّعَلٰی آلِ مُحَمَّدٍ" to R.raw.dua2_audio4,
            "کَمَا بَارَکْتَ عَلٰی اِبْرَاھِیْمَ" to R.raw.dua2_audio4,
            "وَآلِ اِبْرَاھِیْمَِ" to R.raw.dua2_audio4
        )
    ),

    Dua(
        arabic = "اَللّٰھُمَّ بِکَ اَصْبَحْنَا وَ بِکَ اَمْسَیْنَا وَ بِکَ نَحْیَا وَ بِکَ نَمُوْتُ وَ إِلَـيْكَ اَلْمَصِیْرِ",
        translation = "O Allah, by Your leave we have reached the\nmorning and by Your leave we have reached the\nevening and by Your leave we live and die, and\nunto You is our return.",
        reference = " [Sunan al-Tirmidhī]",
        backgroundResId = R.drawable.dua3_new3_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a of Morning",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ بِکَ اَصْبَحْنَا" to R.raw.dua06_part01_audio01,
            "وَبِکَ اَمْسَیْنَا" to R.raw.dua06_part01_audio02,
            "وَبِکَ نَحْیَاَُ" to R.raw.dua06_part01_audio03,
            "وَبِکَ نَمُوْتَُ" to R.raw.dua06_part01_audio04,
            "وَإِلَـيْكَ اَلْمَصِیْر" to R.raw.dua06_part01_audio05,

            )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ بِکَ اَمْسَیْنَا وَبِکَ اَصْبَحْنَا وَبِکَ نَحْیَا وَبِکَ نَمُوْتُ وَاِلَیْکَ النُّشُوْرُ۔ِ",
        translation = "O Allah! By Your leave we reach the evening and by Your leave we reach the morning and by Your leave we live and by Your leave we will die and to You is our resurrection.",
        reference = "[Sunan al-Tirmidhī]",
        backgroundResId = R.drawable.dua4_new4_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a for Evening",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ بِکَ اَمْسَیْنَا" to R.raw.dua2_audio4,
            "وَبِکَ اَصْبَحْنَا" to R.raw.dua2_audio4,
            "وَبِکَ نَحْیَاُ" to R.raw.dua2_audio4,
            "وَبِکَ نَحْیَاَُ" to R.raw.dua2_audio4,
            "وَبِکَ نَمُوْتَُُ" to R.raw.dua2_audio4,
            "وَإِلَـيْكَ اَلْمَصِیْر" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَعُوْذُ بِکَلِمَاتِ اللّٰہِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
        translation = "I seek refuge in the complete, perfect words of\nAllah from the evil of what He has created.",
        reference = "[Ṣaḥīḥ Muslim]",
        backgroundResId = R.drawable.dua5_new5_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a for Protection",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
            "اَعُوْذُ" to R.raw.dua2_audio4,
            "بِکَلِمَاتِ" to R.raw.dua2_audio4,
            "اللّٰہِ" to R.raw.dua2_audio4,
            "التَّامَّاتِ" to R.raw.dua2_audio4,
            "مِنْ" to R.raw.dua2_audio4,
            "شَرِّ" to R.raw.dua2_audio4,
            "مَا" to R.raw.dua2_audio4,
            "خَلَقَ" to R.raw.dua2_audio4
        )
    ),
    Dua(
        arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
        translation = "In the name of Allah, with Whose name nothing\non earth or in the heavens can cause harm, and\nHe is the All-Hearing, the All-Knowing.",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        backgroundResId = R.drawable.dua5_new5_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a for Protection",
        wordAudioPairs = listOf(
            "بِسْمِ اللَّهِ" to R.raw.dua2_audio4,
            "الَّذِي لَا يَضُر" to R.raw.dua2_audio4,
            " مَعَ اسْمِهِ شَيْء" to R.raw.dua2_audio4,
            "فِي الْأَرْضِ" to R.raw.dua2_audio4,
            "وَلَا فِي السَّمَاءِ" to R.raw.dua2_audio4,
            "وَهُوَ السَّمِيعُ الْعَلِيم" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ بِاسْمِكَ اَمُوْتُ وَاَحْیَا",
        translation = "O Allah! By Your name I die and by Your\nname I live.",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        backgroundResId = R.drawable.dua6_new6_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a before Sleeping",
        steps = "1. Dust the bed before sleeping\n2. Sleep on the right side\n3. Put your hand under your right\ncheek and say",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ" to R.raw.dua2_audio4,
            "بِاسْمِكَ" to R.raw.dua2_audio4,
            "اَمُوْتُ" to R.raw.dua2_audio4,
            "وَاَحْیَا" to R.raw.dua2_audio4
        )
    ),
    Dua(
        arabic = "اَلْـحَمْدُ لِلّٰهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورَُِ",
        translation = "All praise is for Allah ho gave us life after death (sleep) and to Him is the resurrection.",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        backgroundResId = R.drawable.dua7_new7_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a aftar Waking Up",
        steps = "Rub your face and your eyes with your hands to\nremove any remaining effects of sleep and say:",
        wordAudioPairs = listOf(
            "اَلْـحَمْدُ لِلّٰه" to R.raw.dua2_audio4,
            "الَّذِي أَحْيَانَاِ" to R.raw.dua2_audio4,
            "بَعْدَ مَا أَمَاتَنَاِ" to R.raw.dua2_audio4,
            "وَإِلَيْهِ النُّشُور" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَللّٰھُمَّ اِنِّیْ اَعُوْذُبِکَ مِنَ الْخُبُثِ وَ الْخَبَائِثِِ",
        translation = "O Allah, indeed I seek refuge in You from the impure male jinns and impure female jinns.",
        reference = "[Ṣaḥīḥ al-Bukhārī]",
        backgroundResId = R.drawable.dua8_new8_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = " Du’a before Entering the\nToilet",
        image = R.drawable.kaaba,
        steps = "Enter with your left foot and say:",
        wordAudioPairs = listOf(
            "اَللّٰھُمَّ اِنِّیْ اَعُوْذُبِکَ" to R.raw.dua2_audio4,
            "مِنَ الْخُبُثِ" to R.raw.dua2_audio4,
            "وَ الْخَبَائِثَ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَلْـحَمْدُ لِلّٰهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُُِ",
        translation = "I ask you (Allah) for forgiveness.",
        reference = "[Sunan Abī Dawūd]",
        backgroundResId = R.drawable.dua9_new9_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a after Leaving \n" +
                "the Toilet",
        steps = "Leave with your right foot and say:",
        wordAudioPairs = listOf(
            "اَلْـحَمْدُ لِلّٰهِ" to R.raw.dua2_audio4,
            "الَّذِي أَحْيَانَا" to R.raw.dua2_audio4,
            " بَعْدَ مَا أَمَاتَنَا" to R.raw.dua2_audio4,
            " وَإِلَيْهِ النُّشُور" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "الحَمْدُ لِلّٰهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍُِِ",
        translation = "All praise is for Allah who has clothed me with this garment and provided it for me, with no power or might from myself. ",
        reference = "[Sunan Abī Dawūd]",
        backgroundResId = R.drawable.dua10_new10_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        textheading = "Du’a before Putting\n" +
                "on Dress",
        steps = "1. Shake and dust your dress before wearing it.\n" +
                "2. Start wearing the cloth from the right side (right\nsleeve, right side of the trouser, right socks, etc.).",
        image = R.drawable.kaaba,
        wordAudioPairs = listOf(
            "الحَمْدُ لِلّٰهِ الَّذِي" to R.raw.dua2_audio4,
            "كَسَانِي هَذَا" to R.raw.dua2_audio4,
            "الثَّوْبَ وَرَزَقَنِيهِ" to R.raw.dua2_audio4,
            " مِنْ غَيْرِ حَوْلٍ" to R.raw.dua2_audio4,
            "مِنِّي وَلَا قُوَّة" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہُِِِِ",
        translation = "In the Name of Allah.",
        reference = "[Ṣaḥīḥ Jāmi’ al-Ṣaghīr]",
        backgroundResId = R.drawable.dua11_new11_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a before Taking \n" +
                "off Dress",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہِ تَوَکَّلْتُ عَلَی اللّٰہِ لَاحَوْلَ وَلَا قُوَّةَ اِلَّا بِاللّٰہِ ُِِِِ",
        translation = "In the name of Allah, I have put my trust in Allah, and there is no power and no might except in Allah.",
        reference = "[Sunan Abī Dawūd]",
        backgroundResId = R.drawable.dua12_new12_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a before Leaving\n" +
                "the Home",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua2_audio4,
            "تَوَکَّلْتُ عَلَی اللّٰہِ" to R.raw.dua2_audio4,
            "لَاحَوْلَ وَلَا قُوَّةَ" to R.raw.dua2_audio4,
            "اِلَّا بِاللّٰہِ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہِ",
        translation = "In the Name of Allah ",
        reference = "[Ṣaḥīḥ Muslim] ",
        backgroundResId = R.drawable.dua13_new13_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a before Entering\n" +
                "the Home",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَلسَّلَامُ عَلَیْکُمْ",
        translation = "May Peace (of Allah) be upon you.",
        reference = "[al-Nūr: 27]",
        backgroundResId = R.drawable.dua13_new13_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a before Entering\n" +
                "the Home",
        wordAudioPairs = listOf(
            "اَلسَّلَامُ عَلَیْکُمِ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَللّٰهُمَّ إِنِّیْ أَعُوْذُبِکَ مِنْ مُنْکَرَاتِ الْأخلاَ قِ، وَالْأعْمَالِ، وَالْأھْوَاءِِ",
        translation = "O Allah! Indeed I seek refuge in You from a detestable conduct and from disliked deeds and desires.",
        reference = "[Sunan al-Tirmidhī]",
        backgroundResId = R.drawable.dua14_new14_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a from protection\n" +
                "from Bad Akhlaq",
        wordAudioPairs = listOf(
            "اَللّٰهُمَّ إِنِّیْ " to R.raw.dua2_audio4,
            "أَعُوْذُبِکَ مِنْْ " to R.raw.dua2_audio4,
            "مُنْکَرَاتِ الْأخلاَ قِ، " to R.raw.dua2_audio4,
            "وَالْأعْمَالِ، " to R.raw.dua2_audio4,
            "وَالْأھْوَاءِِ " to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "بِسْمِ اللّٰہِ",
        translation = "In the Name of Allah ",
        reference = " [Sunan Abī Dawūd]",
        backgroundResId = R.drawable.dua15_new15_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a for Journey",
        wordAudioPairs = listOf(
            "بِسْمِ اللّٰہِ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَلْحَمْدُ لِلّٰہِ",
        translation = "All Praise is for Allah.",
        reference = " [Sunan Abī Dawūd]",
        backgroundResId = R.drawable.dua15_new15_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a for Journey",
        wordAudioPairs = listOf(
            "اَلْحَمْدُ لِلّٰہ" to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "سُبْحٰنَ الَّذِیْ سَخَّرَلَنَاھٰذَاوَمَاکُنَّا لَہ مُقْرِنِیْنَoوَاِنَّآاِلٰی رَبِّنَا لَمُنْقَلِبُوْنَ oِِ",
        translation = "Exalted is He who has subjected this to us, and we could not have [otherwise] subdued it.And indeed we, to our Lord, will [surely] return.",
        reference = " [al-Zukhruf: 13-14]",
        backgroundResId = R.drawable.dua15_new15_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a for Journey",
        wordAudioPairs = listOf(
            "سُبْحٰنَ الَّذِیْ " to R.raw.dua2_audio4,
            "أسَخَّرَلَنَاھٰذَاوَمَاکُنَّاْْ " to R.raw.dua2_audio4,
            "مُقْرِنِیْنَoوَاِنَّآاِلٰی" to R.raw.dua2_audio4,
            "رَبِّنَا لَمُنْقَلِبُوْنَ o، " to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "اَلسَّلَامُ عَلَیْکُمْ وَرَحْمَةُ اللّٰہِ وَ بَرَکَاتُہُِِ",
        translation = "May peace, mercy and blessings\n(of Allah) be upon You.",
        reference = "[Sunan Abī Dawūd]",
        backgroundResId = R.drawable.dua16_new16_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a When Meeting\n" +
                "a Muslim",
        wordAudioPairs = listOf(
            "اَلسَّلَامُ عَلَیْکُمْ" to R.raw.dua2_audio4,
            "أوَرَحْمَةُ اللّٰہ " to R.raw.dua2_audio4,
            "وَ بَرَکَاتُہ " to R.raw.dua2_audio4,
        )
    ),
    Dua(
        arabic = "وَعَلَیْکُمُ السَّلَامُ وَ رَ حْمَةُ اللّٰہِ وَ بَرَکَاتُہُُِِ",
        translation = "May peace, mercy and blessings\n(of Allah) be upon You as Well.",
        reference = "[Sunan Abī Dawūd]",
        backgroundResId = R.drawable.dua16_new16_img,
        statusBarColorResId = R.color.top_nav_new,
        fullAudioResId = R.raw.dua2_audio4,
        image = R.drawable.kaaba,
        textheading = "Du’a When Meeting\n" +
                "a Muslim",
        wordAudioPairs = listOf(
            "وَعَلَیْکُمُ السَّلَامُْ" to R.raw.dua2_audio4,
            "وَ رَ حْمَةُ اللّٰہِ " to R.raw.dua2_audio4,
            "وَ بَرَکَاتُہ" to R.raw.dua2_audio4,
        )
    ),
)
