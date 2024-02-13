package com.example.ant.page.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ant.R
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.ListItem
import com.example.ant.common.view.TopAppBar
import com.example.ant.model.designer.kDesigersMaps
import com.example.ant.ui.theme.Greyeeeeee
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.black20
import com.example.ant.ui.theme.red16

val selectedCurrencyCode = ""

var kTitleList = listOf<String>(
    "How do I place an order?",
    "Do I need an account to place an order?",
    "I’ve forgotten my password - what should I do?",
    "What payment methods does Shopify accept?",
    "Afterpay",
    "Klarna (US and AU only)",
    "Which countries doesShopify ship to?",
    "How long does delivery take and how can I track my item?",
    "How much does shipping cost?",
    "Is my package insured?",
    "How do I return an item?",
    "How long does it take to process my refund?",
    "Are Shopify products new or pre-owned?",
    "Are Shopify products authentic?",
    "How do I subscribe to your newsletter?",
    "I can`t find an item that I am looking for?",
)

var faqList = listOf<String>(
    faq(),
    faq1(),
    faq2(),
    faq3(),
    faq4(),
    faq5(),
    faq6(),
    faq7(),
    faq8(),
    faq9(),
    faq10(),
    faq11(),
    faq12(),
    faq13(),
    faq14(),
    faq15()
)

fun faq(): String {
    return """
1. Select a category (Women, Men, Sale) to explore.
2. Once you have done so, choose your size under the dropdown menu of an item and click “Add to Cart”. You will see a number appear next to the shopping bag icon indicating the number of items you have in your cart.
3. When you are ready to check out, click on the bag icon and you will be taken to a page with a summary of the items you wish to purchase.
4. Click on “Proceed to checkout”
5. Enter your details and address and click “Pay now”.
        """
}

fun faq1(): String {
    return "No, you may place an order as a guest. However, we highly recommend that you sign up for an account so you can track and review purchases from your account. You can also save your address and personal details which will allow for a smoother shopping experience next time."
}

fun faq2(): String {
    return "On the Log In page, click on the “Forgot password” link and we will email you instructions to reset your password."
}

fun faq3(): String {
    if (selectedCurrencyCode == "AUD") {
        return "Shopify accepts Visa, Mastercard, American Express and Afterpay."
    } else if (selectedCurrencyCode == "GBP") {
        return "Shopify accepts Visa, Mastercard and American Express."
    } else if (selectedCurrencyCode == "EUR") {
        return "Shopify accepts Visa, Mastercard and American Express."
    } else {
        return "Shopify accepts Visa, Mastercard, American Express and Afterpay."
    }
}

fun faq4(): String {
    if (selectedCurrencyCode == "AUD") {
        return """
Afterpay is a deferred payment service that allows you to shop today and pay in four payments every 2 weeks without interest.
The first payment is taken when you place an order and the three remaining payments will be automatically taken from your chosen credit or debit card every 2 weeks. You can also log into your Afterpay account to view all payments due and make payments in advance.

There is a limit of $2000 AUD per order made with Afterpay.

For more information, please click here for Afterpay"s Terms and Conditions.
          """
    } else if (selectedCurrencyCode == "GBP") {
        return ""
    } else if (selectedCurrencyCode == "EUR") {
        return ""
    } else if (selectedCurrencyCode == "SGD") {
        return ""
    } else {
        return """
Afterpay is a deferred payment service that allows you to shop today and pay in four payments every 2 weeks without interest.
The first payment is taken when you place an order and the three remaining payments will be automatically taken from your chosen credit or debit card every 2 weeks. You can also log into your Afterpay account to view all payments due and make payments in advance.

There is a limit of USD $2000 per order made with Afterpay.

For more information, please click here for Afterpay"s Terms and Conditions.
          """
    }
}

fun faq5(): String {
    if (selectedCurrencyCode == "AUD") {
        return """
Klarna offers a flexible way to pay for the things you want. With Klarna, you pay in four interest-free instalments. Each payment is collected from your chosen credit or debit card. The first instalment is paid when you place your order, the subsequent three instalments are paid each fortnight.

You can log in to the Klarna app to view your payment schedule or make payments in advance.

For more information, see the Klarna Terms and Conditions.
          """
    } else if (selectedCurrencyCode == "GBP") {
        return ""
    } else if (selectedCurrencyCode == "EUR") {
        return ""
    } else if (selectedCurrencyCode == "SGD") {
        return ""
    } else {
        return """
Klarna offers a flexible way to pay for the things you want. With Klarna, you pay in four interest-free instalments. Each payment is collected from your chosen credit or debit card. The first instalment is paid when you place your order, the subsequent three instalments are paid each fortnight.

You can log in to the Klarna app to view your payment schedule or make payments in advance.

For more information, see the Klarna Terms and Conditions.
          """
    }
}

fun faq6(): String {
    return """
Currently, we ship within Australia, United States of America, Canada, Japan, New Zealand, Singapore, South Korea, Hong Kong, China, Indonesia, Philippines, Macau, Taiwan, Thailand, Saudi Arabia, United Arab Emirates, Qatar, Mexico, Argentina, Colombia, Peru and countries within the European Union
    We are growing rapidly and will add other countries soon. Please subscribe to our newsletter if you would like to receive updates.
"""
}

fun faq7(): String {
    return "Delivery within Australia takes 3-7 business days. For information on how to track your purchase, please see our Orders and Shipping page."
}

fun faq8(): String {
    if (selectedCurrencyCode == "AUD") {
        return "Shipping on orders over AUD 300 is free. A nominal shipping fee is charged for orders under AUD 300."
    } else if (selectedCurrencyCode == "GBP") {
        return "Shipping on orders over GBP 200 is free. A nominal shipping fee is charged for orders under GBP 200."
    } else if (selectedCurrencyCode == "EUR") {
        return "Shipping on orders over EUR 200 is free. A nominal shipping fee is charged for orders under EUR 200."
    } else if (selectedCurrencyCode == "SGD") {
        return "Shipping on orders over SGD 250 is free. A nominal shipping fee is charged for orders under SGD 250."
    } else {
        return "Shipping on orders over USD 250 is free. A nominal shipping fee is charged for orders under USD 250."
    }
}

fun faq9(): String {
    return "Yes we insure every package while in transit to our customers."
}

fun faq10(): String {
    return """
Go to your account at the top right corner where it says your name. Click "Create Return" next to the order you would like to return and follow the prompts. If you encounter any problems, please contact our customer service at customerservice@Shopify.com
    If you checked out as a guest, please sign up for an account with the same email address you used to make the order and initiate a return from there.
"""
}

fun faq11(): String {
    return "Please note that refunds may take up to 10 working days to process due to varying processing times between payment providers. To ensure your refund is as quick as possible, please see our Returns Policy"
}

fun faq12(): String {
    return "We only sell new items."
}

fun faq13(): String {
    return "All products are guaranteed to be authentic."
}

fun faq14(): String {
    return "In the footer of the homepage, there is a text box under “Sign up for our newsletter” where you can enter your email address. You will then receive an email notifying you that you are subscribed. Our subscribers are the first to receive sale alerts, new arrivals and discount codes."
}

fun faq15(): String {
    return "Please email our customer care at customerservice@Shopify.com and we will do our best to help."
}

@Composable
fun FAQPage(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    var showIndex by remember { mutableStateOf(0) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = "FAQ", backType = AppBarBackType.BACK, navController = navController)
        },
    )
    {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()

            ) {
                kTitleList.forEachIndexed { index: Int, text: String ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 50.dp)
//                                .height(50.dp)
                                .clickable {
                                    showIndex = index
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = text,
                                    modifier = Modifier.weight(1F),
                                    style = black16
                                )

                                if (showIndex == index) {
                                    Icon(
                                        painterResource(id = R.drawable.arrow_up),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(
                                        painterResource(id = R.drawable.arrow_down),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                            }

                            if (showIndex == index) {
                                Text(
                                    text = faqList[index],
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    style = black16
                                )
                            }

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(color = Greyeeeeee)
                            )
                        }
                    }
                }
            }
        }
    }
}
