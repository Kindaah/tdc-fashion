#
# This source code is the confidential, proprietary information of
# Dress Club here in, you may not disclose such Information,
# and may only use it in accordance with the terms of the license
# agreement you entered into with Dress Club.
#
# 2018: Dress Club.
# All Rights Reserved.
#

FROM node:8.11.4

RUN wget https://github.com/Yelp/dumb-init/releases/download/v1.2.1/dumb-init_1.2.1_amd64.deb && \
    dpkg -i dumb-init_*.deb
ENTRYPOINT ["dumb-init"]

RUN apt-get update && apt-get install -qy --force-yes \
    gconf-service \
    libasound2 \
    libatk1.0-0 \
    libc6 \
    libcairo2 \
    libcups2 \
    libdbus-1-3 \
    libexpat1 \
    libfontconfig1 \
    libgcc1 \
    libgconf-2-4 \
    libgdk-pixbuf2.0-0 \
    libglib2.0-0 \
    libgtk-3-0 \
    libnspr4 \
    libpango-1.0-0 \
    libpangocairo-1.0-0 \
    libx11-6 \
    libx11-xcb1 \
    libxcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxi6 \
    libxrandr2 \
    libxrender1 \
    libxss1 \
    libxtst6 \
    ca-certificates \
    fonts-liberation \
    libappindicator1 \
    libnss3 \
    lsb-release \
    xdg-utils \
    xvfb \
    libappindicator3-1 \
    && wget -O /tmp/chrome64_66.0.3359.181.deb https://www.slimjet.com/chrome/download-chrome.php?file=lnx%2Fchrome64_66.0.3359.181.deb \
    && dpkg -i /tmp/chrome64_66.0.3359.181.deb \
    && rm /tmp/chrome64_66.0.3359.181.deb \
    && rm -rf /var/lib/apt/lists/*

# docker build -t tdcregistry.azurecr.io/tdc_pipeline/frontend:<tag> .