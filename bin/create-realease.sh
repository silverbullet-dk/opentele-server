#!/bin/zsh

# Creates release
for e in {staging,production}; do 
    for l in {nord,midt,hovedstaden}; do 
        echo "Cleaning...."
        echo "Building war for region: $l environment: $e"  
        grails -Dgrails.env="${l}_${e}" war
        if [ ! -d target/release/$e/$l ]; mkdir -p target/release/$e/$l fi
        mv target/opentele-server.war target/release/$e/$l/ROOT.war
        echo "Created and relocated WAR"
    done
done
