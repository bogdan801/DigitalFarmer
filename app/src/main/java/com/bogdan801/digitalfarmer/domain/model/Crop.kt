package com.bogdan801.digitalfarmer.domain.model

import com.bogdan801.digitalfarmer.R

enum class Crop(val drawableID: Int, val localizedNameID: Int) {
    Wheat(R.drawable.ic_wheat, R.string.crop_wheat),
    Rye(R.drawable.ic_rye, R.string.crop_wheat),
    Oat(R.drawable.ic_oat, R.string.crop_oat),
    Buckwheat(R.drawable.ic_buckwheat, R.string.crop_buckwheat),
    Corn(R.drawable.ic_corn, R.string.crop_corn),
    Sunflower(R.drawable.ic_sunflower, R.string.crop_sunflower),
    Potato(R.drawable.ic_potato, R.string.crop_potato),
    Rapeseed(R.drawable.ic_rapeseed, R.string.crop_rapeseed),
    Soy(R.drawable.ic_soy, R.string.crop_soy),
    Rice(R.drawable.ic_rice, R.string.crop_rice),
    Beetroot(R.drawable.ic_beetroot, R.string.crop_beetroot)
}