package com.badoo.mvicoredemo.ui.main.datamodel

import com.badoo.feature1.Feature1
import com.badoo.mvicoredemo.R

class DataModelTransformer1 : (Feature1.State) -> DataModel {

    override fun invoke(state1: Feature1.State): DataModel {
        return DataModel(
            buttonColors = colors(state1.activeButtonIdx),
            counter = state1.counter,
            imageIsLoading = false,
            imageUrl = null
        )
    }

    private fun colors(active: Int?): List<Int> = listOf(
        if (active == 0) R.color.pink_800 else R.color.pink_500,
        if (active == 1) R.color.light_blue_800 else R.color.light_blue_500,
        if (active == 2) R.color.lime_800 else R.color.lime_500,
        if (active == 3) R.color.yellow_800 else R.color.yellow_500
    )
}
