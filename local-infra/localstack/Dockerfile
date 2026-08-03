FROM localstack/localstack:3

# Normalize CRLF from Windows checkouts so ready.d hooks run under Linux.
COPY init-s3.sh /etc/localstack/init/ready.d/init-s3.sh
RUN sed -i 's/\r$//' /etc/localstack/init/ready.d/init-s3.sh \
    && chmod +x /etc/localstack/init/ready.d/init-s3.sh
