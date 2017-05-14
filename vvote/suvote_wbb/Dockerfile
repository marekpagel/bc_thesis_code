FROM ubuntu:16.04
RUN apt-get update -y && apt-get install -y apt-transport-https
RUN apt-get install -y wget unzip gcc flex libgmp3-dev build-essential cmake bison software-properties-common python-software-properties
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get update -y
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get install -y oracle-java7-installer
RUN wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" download.oracle.com/otn-pub/java/jce/7/UnlimitedJCEPolicyJDK7.zip
RUN unzip UnlimitedJCEPolicyJDK7.zip
RUN mv UnlimitedJCEPolicy/local_policy.jar /usr/lib/jvm/java-7-oracle/jre/lib/security/
RUN mv UnlimitedJCEPolicy/US_export_policy.jar /usr/lib/jvm/java-7-oracle/jre/lib/security/
RUN wget http://crypto.stanford.edu/pbc/files/pbc-0.5.14.tar.gz
RUN tar -xvzf pbc-0.5.14.tar.gz
RUN cd pbc-0.5.14 && ./configure && make && make install
RUN wget http://downloads.sourceforge.net/project/jpbc/jpbc_2_0_0/jpbc-2.0.0.tar.gz
RUN mkdir jpbc
RUN tar -xvzf jpbc-2.0.0.tar.gz -C ./jpbc/
RUN cd jpbc/jpbc-pbc && cmake . && make && cp build/libjpbc-pbc.so /usr/lib
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
RUN echo "deb http://repo.mongodb.org/apt/ubuntu $(cat /etc/lsb-release | grep DISTRIB_CODENAME | cut -d= -f2)/mongodb-org/3.2 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.2.list
RUN apt-get update && apt-get install -y mongodb-org
RUN mkdir -p /data/db
RUN ldconfig
COPY wbb /usr/local/suvote_wbb
RUN chmod +x /usr/local/suvote_wbb/release_demo/StartPeer.sh
CMD ["/usr/local/suvote_wbb/release_demo/StartPeer.sh"]