FROM --platform=linux/arm64/v8 amazoncorretto:18-al2-full

WORKDIR /app/swdev

# Install required dependencies
RUN yum -y update && yum -y --setopt=skip_missing_names_on_install=False install \
    ca-certificates \
    dos2unix \
    git-core \
    gnupg \
    lsof \
    maven \
    net-tools \
    nasm \
    postgresql \
    postgresql-contrib \
    psmisc \
    python-pytest \
    tar \
    unzip \
    wget \
    zip

# Install Heroku CLI


# Install the Spring Boot CLI
RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c -i 'sdk install springboot'

COPY scripts/wait.sh /app/wait.sh
RUN dos2unix /app/wait.sh && chmod a+x /app/wait.sh
RUN mv /root /root.orig



ENTRYPOINT ["/app/wait.sh"]