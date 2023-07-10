package com.aivle.presentation.util.common

import androidx.annotation.DrawableRes
import com.aivle.presentation.R

object CategoryImageBinder {

    const val NO_ID = -1

    private val categoryMap: Map<Int, DrawableResIds> = mapOf(
        0 to DrawableResIds(R.drawable.icon_chair, R.drawable.icon_chair_gray, R.drawable.icon_chair_circle, R.drawable.icon_chair_circle_gray),
        1 to DrawableResIds(R.drawable.icon_tv, R.drawable.icon_tv_gray, R.drawable.icon_tv_circle, R.drawable.icon_tv_circle_gray),
        2 to DrawableResIds(R.drawable.icon_bicycle, R.drawable.icon_bicycle_gray, R.drawable.icon_bicycle_circle, R.drawable.icon_bicycle_circle_gray),
        3 to DrawableResIds(R.drawable.icon_fan, R.drawable.icon_fan_gray, R.drawable.icon_fan_circle, R.drawable.icon_fan_circle_gray),
        4 to DrawableResIds(R.drawable.icon_sofa, R.drawable.icon_sofa_gray, R.drawable.icon_sofa_circle, R.drawable.icon_sofa_circle_gray),
        5 to DrawableResIds(R.drawable.icon_desk, R.drawable.icon_desk_gray, R.drawable.icon_desk_circle, R.drawable.icon_desk_circle_gray),
        6 to DrawableResIds(R.drawable.icon_drawer, R.drawable.icon_drawer_gray, R.drawable.icon_drawer_circle, R.drawable.icon_drawer_circle_gray),
        7 to DrawableResIds(R.drawable.icon_jar, R.drawable.icon_jar_gray, R.drawable.icon_jar_circle, R.drawable.icon_jar_circle_gray),
        8 to DrawableResIds(R.drawable.icon_pot, R.drawable.icon_pot_gray, R.drawable.icon_pot_circle, R.drawable.icon_pot_circle_gray),
        9 to DrawableResIds(R.drawable.icon_bed, R.drawable.icon_bed_gray, R.drawable.icon_bed_circle, R.drawable.icon_bed_circle_gray),
        10 to DrawableResIds(R.drawable.icon_stone_bed, R.drawable.icon_stone_bed_gray, R.drawable.icon_stone_bed_circle, R.drawable.icon_stone_bed_circle_gray),
    )

    private val medalIcons: Map<Int, Int> = mapOf(
        0 to R.drawable.medal_gold,
        1 to R.drawable.medal_silver,
        2 to R.drawable.medal_bronze,
    )

    fun normal(indexLargeCategory: Int): Int {
        return categoryMap[indexLargeCategory]?.normal ?: NO_ID
    }

    fun normalGray(indexLargeCategory: Int): Int {
        return categoryMap[indexLargeCategory]?.normalGray ?: NO_ID
    }

    fun circle(indexLargeCategory: Int): Int {
        return categoryMap[indexLargeCategory]?.circle ?: NO_ID
    }

    fun circle(largeCategory: String): Int {
        val index = when (largeCategory) {
            "의자" -> 0
            "TV" -> 1
            "자전거" -> 2
            "선풍기" -> 3
            "소파(안락의자)" -> 4
            "책상(유리별도)" -> 5
            "서랍장" -> 6
            "항아리" -> 7
            "화분" -> 8
            "침대(목재)" -> 9
            "침대(돌,옥,황토)" -> 10
            else -> 0
        }
        return categoryMap[index]?.circle ?: NO_ID
    }

    fun circleGray(indexLargeCategory: Int): Int {
        return categoryMap[indexLargeCategory]?.circleGray ?: NO_ID
    }

    fun medal(rank: Int): Int {
        return medalIcons[rank] ?: NO_ID
    }

    data class DrawableResIds(
        @DrawableRes val normal: Int,
        @DrawableRes val normalGray: Int,
        @DrawableRes val circle: Int,
        @DrawableRes val circleGray: Int,
    )
}