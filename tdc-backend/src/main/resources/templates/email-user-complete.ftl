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
        text-align: center;
        padding-right: 3em;
        padding-left: 3em;
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
        text-align: center;
      }
      ul {
        margin: 0;
        padding: 0;
      }
      .footer div ul {
        display: inline-block;
        margin: 2em;
      }
      .footer div ul li {
        list-style: none;
        display: inline;
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
      .menu-list li a:hover {
        color: #BF5748;
      }
      .menu-list li a:visited {
        color: #BF5748;
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
            <h1>New user registered</h1>
        </div>
    </div>
    <div class="body-container">
        <p>Hi,</p>
        <p> A new user was registered in the application, now needs complete the company information.</p>
        <p> <b>User: </b> ${ name } </p>
        <p> <b>Email : </b> ${ email } </p>
        <p> <b>Phone number: </b> ${ phoneNumber } </p>
        <p><a class="btn" href="${redirectTo}">Click here to complete the information</a></p>
</div>
<hr>
    <div class="footer">
        <div>TDC Fashion, Inc.</div>
        <div>
            <ul class="menu-list">
                <li><a href="https://tdc.fashion/">Home</a></li>
                <li><a href="https://www.tdc.fashion/pricing/">Pricing</a></li>
                <li><a href="https://www.tdc.fashion/solutions/">Solutions</a></li>
                <li><a href="https://www.tdc.fashion/impact/">Impact</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>