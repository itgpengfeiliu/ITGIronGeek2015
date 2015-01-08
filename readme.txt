Google Cloud

The Cloud SDK for Java requires Java 1.7+ and Python 2.7.x.
https://cloud.google.com/sdk/#install-cygwin

BLOCKED
Installation from an archive (.zip) on Windows
https://cloud.google.com/sdk/#install-archive
Python 2.X only that is 2.6 greater, set the CLOUDSDK_PYTHON environment variable
https://www.python.org/downloads/release/python-279/
CLOUDSDK_PYTHON=C:\Python27\python.exe

instance: open a browser window
sudo apt-get install apache2
$ echo '<!doctype html><html><body><h1>Hello World!</h1></body></html>' | sudo tee /var/www/html/index.ht
http://130.211.156.28/

sudo service tomcat7 start
sudo service tomcat7 stop

Get Glassfish Zip file
wget download.java.net/glassfish/4.0/release/glassfish-4.0.zip
sudo apt-get install unzip
sudo unzip glassfish-4.0.zip -d /opt
export PATH=/opt/glassfish4/bin:$PATH


Cloud SDK>gcloud compute copy-files att.pdf instance-1:/home/pfliu2010_gmail_com --zone us-central1-f
Cloud SDK>gcloud compute copy-files att.pdf instance-1:/home/pfliu2010_gmail_com --zone us-central1-f


MySQL
mysql --host=173.194.86.205 --user=root --password
show databases;
CREATE DATABASE IronGeekCloudComputing;
USE IronGeekCloudComputing;
CREATE TABLE BrokerTradingSummary (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  broker VARCHAR(50),
  tradedate datetime,
  side VARCHAR(20),
  totaltradedvalue bigint
);
CREATE TABLE DailyBrokerRankingReport (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  broker VARCHAR(50),
  tradedate datetime,
  totalrealizedgain bigint
);
show tables;


mvn archetype:generate -Dappengine-version=1.9.17 -Dapplication-id=third-node-814 Dfilter=com.google.appengine.archetypes:
