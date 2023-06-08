<!--
  ~ Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
  ~ This software is the confidential and proprietary information of
  ~ The Dress Club. ("Confidential Information").
  ~ You may not disclose such Confidential Information, and may only
  ~ use such Confidential Information in accordance with the terms of
  ~ the license agreement you entered into with The Dress Club.
  -->

<html>
<head>
    <style type="text/css">
      body {
        margin: 0;
        padding: 0;
        font-family:ProximaNova,sans-serif;
      }
      hr {
        margin-top: 3em;
        margin-bottom: 1em;
      }
      .name-app {
        align-content: center;
        border-bottom: .3em solid #BF5748;
        margin-left: 20%;
        margin-right: 20%;
      }
      .container {
        text-align: center;
        width: 100%;
      }
      .body-container {
        text-align: justify;
        padding-right: 20%;
        padding-left: 20%;
      }
      .mail-padding {
        padding-top: 2em;
      }
      .logo {
        background-color: #FCEEE8;
        padding: 2em;
        text-align: center;
      }
      .btn {
        background-color: #BF5748;
        color: #ffffff;
        display: inline-block;
        font-weight: bold;
        text-align: center;
        white-space: nowrap;
        vertical-align: middle;
        user-select: none;
        border: 1px solid transparent;
        padding: 0.5rem 0.75rem;
        font-size: 1rem;
        line-height: 1.25;
        border-radius: 0.25rem;
        transition: all 0.15s ease-in-out;
        text-decoration: none;
      }
      .button {
        text-align: center;
      }
      .btn:link {
        color: #ffffff;
      }
      .btn:visited {
        color: #ffffff;
      }
      .btn:hover {
        color: #ffffff;
      }
      .btn:active {
        color: #ffffff;
      }
      .footer {
        text-align: justify;
        padding: 2em;
        padding-top: 0%;
        color: gray;
        font-size: 80%;
      }
      ul {
        margin: 0;
        padding: 0;
      }
      img {
        -ms-interpolation-mode:bicubic;
        border:0;
        height:auto;
        outline:0;
        text-decoration:none;
      }
      .menu-list li a {
        margin-right: 1em;
        text-decoration: none;
      }
      @font-face {
        font-family:ProximaNova;
        src:url(https://cdn.auth0.com/fonts/proxima-nova/proximanova-regular-webfont-webfont.eot);
        src:url(https://cdn.auth0.com/fonts/proxima-nova/proximanova-regular-webfont-webfont.eot?#iefix) format('embedded-opentype'),url(https://cdn.auth0.com/fonts/proxima-nova/proximanova-regular-webfont-webfont.woff) format('woff');
        font-weight:400;
        font-style:normal;
      }
      @font-face {
        font-family:ProximaNova;
        src:url(https://cdn.auth0.com/fonts/proxima-nova/proximanova-semibold-webfont-webfont.eot);
        src:url(https://cdn.auth0.com/fonts/proxima-nova/proximanova-semibold-webfont-webfont.eot?#iefix) format('embedded-opentype'),url(https://cdn.auth0.com/fonts/proxima-nova/proximanova-semibold-webfont-webfont.woff) format('woff');
        font-weight:600;
        font-style:normal;
      }
      @media only screen and (max-width:480px) {
        #bodyTable,body {
          width:100%!important;
        }
        a,blockquote,body,li,p,table,td {
          -webkit-text-size-adjust:none!important;
        }
        body {
          min-width:100%!important;
        }
        #signIn {
          max-width:280px!important;
        }
      }
    </style>
</head>
<body style="background-color: #F9F9F9;">
    <div class="container">
        <div class="logo">
            <img src="https://tdc.fashion/wp-content/themes/tdc-blog/img/logo-tdc-black.png" width="50" alt="Your logo goes here" style="-ms-interpolation-mode: bicubic;border: 0;height: auto;line-height: 100%;outline: none;text-decoration: none;">
            <div style="border-bottom: .3em solid <#if redirectTo?? > #BF5748 <#else> #7ED321 </#if>" class="name-app">
                <h1>Payment ${ paymentState }</h1>
            </div>
        </div>
        <div class="body-container">
            <p>Hello, ${ userName } ${ userLastName } </p>

            <p class="mail-padding">
                <#if isPaymentFailed >
                    There was a problem with your payment to TDC Fashion, we have been unable to charge your account for your order and we have suspended any further fulfillment action on it.
                <#else>
                    Your payment to TDC Fashion has been successfully processed.
                </#if>
            </p>

            <p class="mail-padding"> <b> Amount: </b> ${ paymentAmount } </p>
            <p> <b> Order: </b> ${ paymentOrderId } </p>
            <p> <b> Payment: </b> ${ paymentInvoice } </p>

            <p class="mail-padding">
                <#if isPaymentFailed >
                    If you don't re-submit your payment in the next 5 days, your order will be canceled.
                    Please note that you may still be liable even if the order was not entirely fulfilled. To re-submit your payment, please click in the next button:
                <#else>
                    You can see your order clicking here:
                </#if>
            </p>

            <p class="button"><a class="btn" href="${ redirectTo }">See order</a></p>

            <p class="mail-padding">
                <#if isPaymentFailed >
                    A charge can be declined for a variety of reasons. For more information on why the charge was declined,
                    please contact the bank in which you have your account.
                    Note that the bank may have declined the charge if any information you entered during payment does not exactly match the bank's information.
                </#if>
            </p>

            <p class="mail-padding"> Best regards, </p>
            <p> TDC Fashion </p>
            <p class="mail-padding">Please note that additional payments may be required in order to fully process your order. Please contact us via email at finance@tdc.fashion for any inquiry.</p>
        </div>
        <hr>
        <div class="footer">
            <p>
                By placing your order, you agree to <a href="https://www.tdc.fashion/privacy" target="_blank">TDC Fashion’s Terms of Use</a>.
                The contents of this message, together with any attachments, are intended only for the use of the company to which they are addressed and may contain confidential and/or privileged information. If you are not the intended recipient, immediately advise the sender and delete this message and any attachments. Any distribution, or copying of this message, or any attachment, is prohibited.
            </p>
            <p>
                This email was sent from a notification-only address that cannot accept incoming email. Please do not reply to this message.
            </p>
        </div>
    </div>
</body>
</html>