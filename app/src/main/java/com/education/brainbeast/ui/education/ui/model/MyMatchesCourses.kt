/*
 * Copyright (c) 2020. rogergcc
 */
package com.education.brainbeast.ui.education.ui.model

import com.education.brainbeast.R

class MyMatchesCourses private constructor() {

    val data: List<MatchCourse>
        get() = listOf<MatchCourse>(
            //                new MatchCourse(1, "Everyday Candle", "$12.00 USD", R.drawable.shop1),
            //                new MatchCourse(2, "Small Porcelain Bowl", "$50.00 USD", R.drawable.shop2),
            //                new MatchCourse(3, "Favourite Board", "$265.00 USD", R.drawable.shop3),
            //                new MatchCourse(4, "Earthenware Bowl", "$18.00 USD", R.drawable.shop4),
            //                new MatchCourse(5, "Porcelain Dessert Plate", "$36.00 USD", R.drawable.shop5),
            //                new MatchCourse(6, "Detailed Rolling Pin", "$145.00 USD", R.drawable.shop6)
            MatchCourse(1, "Digital Marketing", "12 courses available", R.drawable.education_2),
            MatchCourse(2, "Business", "50 courses available", R.drawable.education_3),
            MatchCourse(3, "Development", "265 courses available", R.drawable.education_4),
            MatchCourse(4, "Security", "18 courses available", R.drawable.education_1),
            MatchCourse(5, "Ethical Hacking", "36 courses available", R.drawable.education_5),
            MatchCourse(6, "Mobile", "145 courses available", R.drawable.education_6)
        )

    companion object {
        private const val STORAGE = "shop"
        fun get(): MyMatchesCourses {
            return MyMatchesCourses()
        }
    }
}
