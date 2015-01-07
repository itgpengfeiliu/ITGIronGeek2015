Google Cloud

instance: open a browser window
sudo apt-get install apache2
$ echo '<!doctype html><html><body><h1>Hello World!</h1></body></html>' | sudo tee /var/www/html/index.ht
http://130.211.156.28/


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

