FROM ubuntu:16.04
RUN apt-get update -y
RUN apt-get install -y software-properties-common python-software-properties
RUN add-apt-repository ppa:webupd8team/java
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
RUN echo "deb http://repo.mongodb.org/apt/ubuntu $(cat /etc/lsb-release | grep DISTRIB_CODENAME | cut -d= -f2)/mongodb-org/3.2 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.2.list
RUN apt-get update -y
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get install -y oracle-java8-installer mongodb-org
RUN mkdir -p /data/db
COPY bft_smart_library /usr/local/bft_smart_library
COPY my_app /usr/local/my_app
RUN chmod +x /usr/local/my_app/startPeer.sh
CMD ["/usr/local/my_app/startPeer.sh"]