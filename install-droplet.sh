#!/bin/bash

# Assumptions: 
#   a user with sudo privilege, named 'quizki' exists.

# 
# This script should be run as sudo
#
if [ "$(id -u)" != 0 ]; then
    echo "You should run this as root."
    exit 1
fi

echo ------------------------*
echo Doin\' setup stuff
echo ------------------------*
echo
cd ~
apt-get update 
apt-get install -y unzip git dpkg-dev maven authbind
mkdir work
sleep 5

echo --------------------------
echo Installing Oracle Java 8
echo --------------------------
echo
# install oracle java 8 (remember to tip this guy)
mkdir apps/java -p
cd work
git clone git://github.com/rraptorr/oracle-java8.git
cd oracle-java8
sh ./prepare.sh
dpkg-buildpackage -uc -us
cp x64/* ~/apps/java -R

echo "export JAVA_HOME=/home/quizki/apps/java" >> ~/quizki_env_vars
sleep 5

echo ----------------------------
echo installing Tomcat
echo ----------------------------
echo
# install tomcat
cd ~
mkdir apps/tomcat -p
cd work
curl -# -L http://apache.mirrors.lucidnetworks.net/tomcat/tomcat-7/v7.0.67/bin/apache-tomcat-7.0.67.tar.gz > apache-tomcat-7.0.67.tar.gz
tar -xvf apache-tomcat-7.0.67.tar.gz
if [ $? -ne 0 ]; then
    echo
    echo "ERROR downloading Tomcat 7.0.67!"
    echo
    echo "I bet the link is bad. Replace the curl URL in the TOMCAT section of install-droplet.sh, and all the file names as appropriate."
    echo
    echo "You will need to change the filename, etc in install-quizki-post.sh, too!!"
    echo
    exit
fi
mv apache-tomcat-7.0.67 ~/apps/

echo "export TOMCAT_HOME=/home/quizki/apps/apache-tomcat-7.0.67" >> ~/quizki_env_vars
sleep 5

echo ------------------------------
echo Installing MySQL
echo ------------------------------
echo 
# install mysql
apt-get install -y mysql-server

echo "Initializing the Quizki user. Executing the following command."
echo
echo "Use the ROOT password!"
sleep 7
echo
echo "mysql -u root -p < ./src/main/resources/META-INF/sql/mysql/init_quizki_user.sql"
bash -c "cd ~/apps/quizki && mysql -u root -p < ./src/main/resources/META-INF/sql/mysql/init_quizki_user.sql"

echo "Initializing the Quizki database. Executing the following command."
echo 
echo "Use the 'quizki' password."
sleep 7
echo
echo "mysql -u quizki -p < ./src/main/resources/META-INF/sql/mysql/populate_database_quizkiDotCom_20150820.sql"
bash -c "cd ~/apps/quizki && mysql -u quizki -p < ./src/main/resources/META-INF/sql/mysql/populate_database_quizkiDotCom_20150820.sql"
sleep 5

echo ----------------------------------
echo

echo "export PATH=/home/quizki/apps/java/bin:/home/quizki/apps/apache-tomcat-7.0.57/bin:$PATH" >> ~/quizki_env_vars
cp ~/quizki_env_vars /etc/profile.d/quizki_env_vars.sh
chmod +x /etc/profile.d/quizki_env_vars.sh

echo "Reboot the droplet now. Then run cd ~/apps/quizki && sudo ./install-droplet-post.sh"
echo 
