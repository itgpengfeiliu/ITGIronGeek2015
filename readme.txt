Google Cloud

The Cloud SDK for Java requires Java 1.7+ and Python 2.7.x.

instance: open a browser window
sudo apt-get install apache2
$ echo '<!doctype html><html><body><h1>Hello World!</h1></body></html>' | sudo tee /var/www/html/index.ht
http://130.211.156.28/

/var/lib/tomcat7/webapps
sudo service tomcat7 start
sudo service tomcat7 stop

apache2
/var/www/html
sudo /etc/init.d/apache2 stop

Get Glassfish Zip file
wget download.java.net/glassfish/4.0/release/glassfish-4.0.zip
sudo apt-get install unzip
sudo unzip glassfish-4.0.zip -d /opt

PATH=$PATH:/opt/glassfish4/bin
sudo chmod -R 777 domains 
asadmin start-domain
asadmin enable-secure-admin
asadmin change-admin-password
admin
lpf123fpl
asadmin restart-domain
asadmin deploy hello.war

put war file in
/opt/glassfish4/glassfish/domains/domain1/applications

asadmin deploy hello.war

transfer files:
C:\Program Files\Google\Cloud SDK
Cloud SDK>gcloud compute copy-files helloworld.war instance-1:/home/lpf66fpl --zone us-central1-f


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
