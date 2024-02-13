package com.example.ant.model.input

import androidx.compose.ui.graphics.Color

data class InputModel(
    var title: String,
    var text: String = "",
    var optional: Boolean = false,
    var line: Boolean = true,
    var error: String? = null,
    var showError: Boolean = false,
    var required: Boolean = true,
    var callBack: (() -> Unit)? = null,
    var leftImg: String? = null,
    var rightImg: Int? = null,
    var show: Boolean = true,
    var placeHolder: String? = null,
    var hideTitle: Boolean = false,
    var lineColor: Color = Color.Black,
)