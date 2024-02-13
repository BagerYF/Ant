package com.example.ant.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ant.R
import com.example.ant.model.input.InputModel
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black12
import com.example.ant.ui.theme.grey75757514
import com.example.ant.ui.theme.grey75757516
import com.example.ant.ui.theme.red14
import com.example.ant.ui.theme.red16

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InputView(item: InputModel, modifier: Modifier) {

    var text by remember { mutableStateOf("") }
    var hasFocus by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(item, item.showError) {
        text = item.text
        showError = item.showError
    }

    Column(modifier = modifier) {
        if (!item.hideTitle) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${item.title}${
                        if (item.required) {
                            "*"
                        } else {
                            ""
                        }
                    }",
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .weight(1f),
                    style = black12
                )
                if (item.optional) {
                    Text(
                        text = "optional",
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        style = grey75757514
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            item.callBack?.let { it() }
        }) {

            if (item.leftImg != null) {
                AsyncImage(
                    model = item.leftImg,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(28.dp)
                )
            }

            BasicTextField(
                value = text,
                onValueChange = { t ->
                    text = t
                    item.text = t
                    item.showError = false
                },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                enabled = item.callBack == null,
                maxLines = if (item.title == "Message") 5 else 1,
                modifier = Modifier
                    .weight(1f)
                    .height(if (item.title == "Message") 120.dp else 40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White)
                    .focusRequester(focusRequester)
                    .onFocusChanged { f ->
                        hasFocus = f.isFocused
                    },
                visualTransformation = if (item.title != "Password") VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                })
            ) { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = text,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = item.title != "Message",
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                        start = 0.dp,
                        top = 10.dp,
                        end = 0.dp,
                        bottom = 10.dp
                    ),
                    placeholder = {
                        Text(
                            text = item.placeHolder ?: item.title,
                            style = grey75757516
                        )
                    },
                )
            }

            if (item.rightImg != null) {
                Icon(
                    painterResource(id = item.rightImg!!),
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 2.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Greyeeeeee)
        )

        if (showError) {
            Text(
                text = "Enter a ${item.title}",
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                style = red14
            )
        }
    }


}

