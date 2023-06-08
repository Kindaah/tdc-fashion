#
# This source code is the confidential, proprietary information of
#  Dress Club here in, you may not disclose such Information,
# and may only use it in accordance with the terms of the license
# agreement you entered into with Dress Club.
#
# 2018: Dress Club.
# All Rights Reserved.
#

FROM openjdk:8-jdk-alpine

COPY build/libs/ROOT.war /ROOT.war
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ROOT.war"]
