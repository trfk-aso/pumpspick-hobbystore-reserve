package pumpspick.hobbystore.reserve.data.model

import androidx.annotation.StringRes
import pumpspick.hobbystore.reserve.R

enum class ProductCategory(@field:StringRes val titleRes: Int) {
    HOBBIES(R.string.category_hobbies),
    GIFTS_SOUVENIRS(R.string.category_gifts_souvenirs),
    GAMES_PUZZLES(R.string.category_games_puzzles),
    STATIONERY_ART(R.string.category_stationery_art),
    HOME_LIFESTYLE(R.string.category_home_lifestyle),
}
