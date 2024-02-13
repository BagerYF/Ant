package com.example.ant.page.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ant.common.enum.AppBarBackType
import com.example.ant.common.view.TopAppBar
import com.example.ant.ui.theme.black16
import com.example.ant.ui.theme.black20

val privacyPolicyList = listOf(
    mapOf(
        "type" to "title",
        "text" to "About Shopify",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """This website is owned and run Ark Technologies Pty. Ltd. (“Company”). We are committed  to protecting your privacy and prepared this Privacy Policy bound by the Australian Privacy Principles and the Privacy Act 1988 (Cth)  to describe  to you our practices regarding the personal information we collect from users of those websites that link  to this Privacy Policy (each, a “Site” and collectively, the “Sites”)and the services we offer (the “Services”). """,
    ),
    mapOf(
        "type" to "title",
        "text" to "Questions; Contacting Company; Reporting Violations.",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """If you have any questions or concerns or complaints about our Privacy Policy or our data collection or processing practices, or if you want  to report any security violations  to us, please mail us at the following address to
Ark Technologies Pty. Ltd.
40/140 Williams Street
Melbourne
Vic toria 3000
ACN to 603 272 359

        A Note about Children. We do not intentionally gather personal information from
visi tors who are under the age of 13. If a child under 13 submits personal information  to the Company and we learn that the personal information is the information of a child under 13, we will attempt  to delete the information as soon as possible. If you believe that we might have any personal information from a child under 13, please contact us at to legal@Shopify.com """,
    ),
    mapOf(
        "type" to "title",
        "text" to "Personal Data",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """In this Privacy Policy, "Personal Data" refers  to any data, whether true or not, about an individual who can be identified (a) from that data; or (b) from that data and other information  to which we have or are likely  to have access, including data in our records as may be updated from time  to time.

        Examples of such "Personal Data" you may provide  to us include your name, passport or other identification number, telephone numbers, mailing address, email address, network data and any other information relating  to any individuals which you have provided us in any forms you may have submitted  to us, or via other forms of interaction with you.""",
    ),
    mapOf(
        "type" to "title",
        "text" to "Information Collected",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """We collect "Personal Data", directly from you, or from publicly available sources or through our website in the following ways to""",
    ),
    mapOf(
        "type" to "row",
        "title" to "1.",
        "text" to
                "when you submit a form for registering as a cus tomer on our website or any other forms relating  to any of our Products and Services",
    ),
    mapOf(
        "type" to "row",
        "title" to "2.",
        "text" to
                "when you interact with our member relations team, for example, via telephone calls, letters, face- to-face meetings and emails",
    ),
    mapOf(
        "type" to "row",
        "title" to "3.",
        "text" to
                "when you use some of our services, for example, websites and apps including establishing any online accounts with us",
    ),
    mapOf(
        "type" to "row",
        "title" to "4.",
        "text" to
                "when you request that we contact you, be included in an email or other mailing list",
    ),
    mapOf(
        "type" to "row",
        "title" to "5.",
        "text" to
                "when you respond  to our promotions, initiatives or  to any request for additional Personal Data",
    ),
    mapOf(
        "type" to "row",
        "title" to "6.",
        "text" to
                "when you are contacted by, and respond  to, our marketing representatives and cus tomer service team",
    ),
    mapOf(
        "type" to "row",
        "title" to "7.",
        "text" to
                "when we receive references from business partners and third parties, for example, where you have been referred by them",
    ),
    mapOf(
        "type" to "row",
        "title" to "8.",
        "text" to
                """when you submit your "Personal Data"  to us for any other reasons.""",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """When you browse our website, you generally do so anonymously. However, please note of other features present. Please see paragraph 4 below on the understanding of Cookies.""",
    ),
    mapOf(
        "type" to "row",
        "title" to "1.",
        "text" to
                "In order  to fully access this Site, you must first complete the registration process. During the registration process, we collect personal information such as your name and email address. Once you have completed and submitted your registration, you will be opted in  to receive email communication from us.",
    ),
    mapOf(
        "type" to "row",
        "title" to "2.",
        "text" to
                "We also collect personal information when you choose  to use certain features of the Site, such as making purchases or electing  to receive text messages about upcoming events or promotions. When you choose  to use these additional features, we require you  to provide additional personal information such as your phone number, billing and shipping addresses and credit card information, and we may request additional personal information such as your shopping preferences and demographics.",
    ),
    mapOf(
        "type" to "row",
        "title" to "3.",
        "text" to
                "In addition, through the Site, we au tomatically gather certain information about the use of the Site, such as how frequently certain areas of the Site are visited, including through the use of cookies, web beacons and other technologies. Most browsers can be set  to prevent cookies or  to notify you when one is being placed. However, cookies are necessary  to access the Site.",
    ),
    mapOf(
        "type" to "row",
        "title" to "4.",
        "text" to
                "The Site also makes use of a technology designed  to enhance your browsing experience and  to provide aggregate non-personally identifiable information about the use of the Site  to us. Information on deleting or controlling cookies is available at www.AboutCookies.org. Please note that by deleting our cookies or disabling future cookies you may not be able  to access certain areas or features of our site.",
    ),
    mapOf(
        "type" to "title",
        "text" to "How We Use Your Information",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """We will not share or disclose your information  to anyone except as described in this Privacy Policy.

        We use personally identifiable information in order  to give you a more enjoyable, convenient shopping experience and  to help us identify and/or provide information, products or services that may be of interest  to you. We collect, may use and disclose your "Personal Data" for the following purposes to""",
    ),
    mapOf(
        "type" to "row",
        "title" to "1.",
        "text" to "responding  to your queries and requests;",
    ),
    mapOf(
        "type" to "row",
        "title" to "2.",
        "text" to "fulfilling your order(s);",
    ),
    mapOf(
        "type" to "row",
        "title" to "3.",
        "text" to "tracking email invitations you send;",
    ),
    mapOf(
        "type" to "row",
        "title" to "4.",
        "text" to
                "managing administrative and business operations and complying with internal policies and procedures;",
    ),
    mapOf(
        "type" to "row",
        "title" to "5.",
        "text" to
                "facilitating business asset transactions (which may extend  to any mergers, acquisitions or asset sales, and transfers made as part of an insolvency or bankruptcy proceeding) involving any of the all or part of our business or as part of a corporate reorganization, s tock sale or other change in control;",
    ),
    mapOf(
        "type" to "row",
        "title" to "6.",
        "text" to
                """matching any "Personal Data" held which relates  to you for any of the purposes listed herein;""",
    ),
    mapOf(
        "type" to "row",
        "title" to "7.",
        "text" to
                "resolving complaints and handling requests and enquiries through your personalised profile of your shopping his tory;",
    ),
    mapOf(
        "type" to "row",
        "title" to "8.",
        "text" to
                "preventing, detecting and investigating crime and analysing and managing commercial risks;",
    ),
    mapOf(
        "type" to "row",
        "title" to "9.",
        "text" to "providing media announcements and responses;",
    ),
    mapOf(
        "type" to "row",
        "title" to "10.",
        "text" to
                "moni toring or recording phone calls and cus tomer-facing interactions for quality assurance, employee training and performance evaluation and identity verification purposes;",
    ),
    mapOf(
        "type" to "row",
        "title" to "11.",
        "text" to "organising promotional events;",
    ),
    mapOf(
        "type" to "row",
        "title" to "12.",
        "text" to
                "legal purposes (including but not limited  to obtaining legal advice and dispute resolution);",
    ),
    mapOf(
        "type" to "row",
        "title" to "13.",
        "text" to
                "conducting investigations relating  to disputes, billing or fraud;",
    ),
    mapOf(
        "type" to "row",
        "title" to "14.",
        "text" to
                "providing postal mailing list (consisting of cus tomer names and postal mailing addresses, but not email addresses)  to other companies whose products we believe may be of interest  to you. In order  to determine those products that we believe may be of interest  to you, the information from you and your order may be combined with other personally identifiable information (such as demographic information and past purchase his tory) available from our records and other sources.",
    ),
    mapOf(
        "type" to "row",
        "title" to "15.",
        "text" to
                "meeting or complying with any applicable rules, laws, regulations, codes of practice or guidelines issued by any legal or regula tory bodies (including but not limited  to responding  to regula tory complaints, disclosing  to regula tory bodies and conducting audit checks, due diligence and investigations); and purposes which are reasonably related  to the aforesaid.",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """Plus, we may share personally identifiable or other information with our parent, subsidiaries, divisions, and affiliates.

        We reserve the right  to disclose information in order  to comply with a subpoena, court order, administrative or governmental order, or any other requirement of law, or when we, in our sole discretion, believe it is necessary in order  to protect our rights or the rights of others,  to prevent harm  to individuals or property,  to fight fraud and credit risk reduction.

        You will receive notice when your personally identifiable information might be provided  to any third party for any reason other than as set forth in this Privacy Policy, and you will have an opportunity  to request that we do not share such information.

        We use non-personally identifiable information in the aggregate, so that we can improve the Site and for business and administrative purposes. We may also use or share with third parties for any purpose aggregated data that contains no personally identifiable information.""",
    ),
    mapOf(
        "type" to "title",
        "text" to "How We Protect Your Information",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """We are committed  to protecting the information we receive from you. We follow reasonable technical and management practices  to help protect the confidentiality, security and integrity of data s tored in our system. While no computer system is completely secure, we believe the measures we have implemented reduce the likelihood of security problems  to a level appropriate  to the type of data involved.

        The Site encrypts your credit card number and other personal information using secure socket layer (SSL) technology  to provide for the secure transmission of the information from your PC  to our servers. In addition, only those employees and third parties who need access  to your information in order  to perform their duties are allowed such access.""",
    ),
    mapOf(
        "type" to "title",
        "text" to "Disclosure of Personal Data",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """We will take reasonable steps  to protect your "Personal Data" against unauthorised disclosure. Subject  to the provisions of any applicable law, your "Personal Data" may be disclosed, for the purposes listed above (where applicable),  to the following to""",
    ),
    mapOf(
        "type" to "row",
        "title" to "1.",
        "text" to
                "Related corporations and employees  to provide content, Products and Services  to you, address your questions and requests in relation  to your cus tomer accounts, subscription and billing or order arrangements with us as well as our Products and Services,  to activate, deactivate, install, maintain and operate our systems and/or services;",
    ),
    mapOf(
        "type" to "row",
        "title" to "2.",
        "text" to
                "companies providing services relating  to insurance and consultancy;",
    ),
    mapOf(
        "type" to "row",
        "title" to "3.",
        "text" to
                "agents, contrac tors or third party service providers who provide us operational services, such as courier services, telecommunications, information technology, payment, printing, billing, payroll, processing, technical services, training, market research, call centre, security or other services to",
    ),
    mapOf(
        "type" to "row",
        "title" to "4.",
        "text" to
                "vendors or third party service providers in connection with marketing promotions and services offered by us or our preferred partners;",
    ),
    mapOf(
        "type" to "row",
        "title" to "5.",
        "text" to
                "any business partner, inves tor, assignee or transferee (actual or prospective)  to facilitate business asset transactions (which may extend  to any merger, acquisition or asset sale) involving any of the",
    ),
    mapOf(
        "type" to "row",
        "title" to "6.",
        "text" to "our professional advisers such as audi tors and lawyers;",
    ),
    mapOf(
        "type" to "row",
        "title" to "7.",
        "text" to
                "relevant government regula tors, statu tory boards or authorities or law enforcement agencies  to comply with any laws, rules, guidelines and regulations or schemes imposed by any governmental authority; and",
    ),
    mapOf(
        "type" to "row",
        "title" to "8.",
        "text" to
                """any other party  to whom you authorise us  to disclose your "Personal Data"  to.""",
    ),
    mapOf(
        "type" to "title",
        "text" to "Quality of Personal Information",
    ),
    mapOf(
        "type" to "text",
        "text" to """We will take reasonable steps  to to""",
    ),
    mapOf(
        "type" to "row",
        "title" to "1.",
        "text" to
                "make sure that the personal information we collect, use or disclose is accurate, complete and up  to date;",
    ),
    mapOf(
        "type" to "row",
        "title" to "2.",
        "text" to
                "protect your personal information from misuse, loss, unauthorised access, modification or disclosure both physically and through computer security methods; and",
    ),
    mapOf(
        "type" to "row",
        "title" to "3.",
        "text" to
                "destroy or permanently de-identify personal information if it is no longer needed for its purpose of collection.",
    ),
    mapOf(
        "type" to "title",
        "text" to
                "Accessing and Updating Your Personal Information and Preferences",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """If you are a registered user, you may access and update your registration information and your preferences  to receive email or other communications by updating the required information in the “Account Details" section of the website.

        We will take commercially reasonable steps  to implement your opt-out requests promptly, but you may still receive communications from us for up  to ten business days while we process your request.

        While we make efforts  to accommodate requests  to restrict our use of your information, we reserve the right  to delete all or any portion of cus tomer information if we are not able  to reasonably accommodate a requested restriction.""",
    ),
    mapOf(
        "type" to "title",
        "text" to "Privacy Policy Changes",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """If we decide  to change our Privacy Policy for the Site, we will post the revised Privacy Policy here so that you will always know what information we gather, how we might use that information and whether we may disclose it  to anyone. Your continued use of the Site indicates your assent  to the Privacy Policy as posted.""",
    ),
    mapOf(
        "type" to "title",
        "text" to "Governing Law",
    ),
    mapOf(
        "type" to "text",
        "text" to
                """This agreement shall be governed by the law of the State of Vic toria and the parties submit  to the jurisdiction of the courts of that State.""",
    ),
    mapOf(
        "type" to "row",
        "title" to "",
        "text" to "",
    ),
)

@Composable
fun PrivacyPolicyPage(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(text = "Privacy Policy", backType = AppBarBackType.BACK, navController = navController)
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
                items(privacyPolicyList) { item ->
                    if (item["type"] == "title") {
                        Text(
                            text = item["text"] ?: "",
                            textAlign = TextAlign.Left,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            style = black20
                        )
                    } else if (item["type"] == "text") {
                        Text(
                            text = item["text"] ?: "",
                            textAlign = TextAlign.Left,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(16.dp),
                            style = black16
                        )
                    } else if (item["type"] == "row") {
                        Text(
                            text = "${item["title"] ?: ""}${item["text"] ?: ""}",
                            textAlign = TextAlign.Left,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
                            style = black16
                        )
                    }
                }
            }
        }
    }
}
