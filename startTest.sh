#!/bin/bash

## Checking arguments...
if [[ $# != 0 ]]; then
    printf "Error: Unexpected parameters\n";
    exit 1;
fi

##################################################

bold=$(tput bold)
green=$(tput setaf 2)
reset=$(tput sgr0)

##################################################

## Compiling
echo
echo "${bold}Compiling java project...${reset}"

javac Progetto_Gruppo_33/*.java -d Progetto_Gruppo_33/.class/

echo 
echo "${bold}Compiling completed, starting test:${reset}"

##################################################

## Declaring start, success, failure, and extraTests command
start="java -cp Progetto_Gruppo_33/.class/ Progetto_Gruppo_33.Main"

extraTestDir="./tests/extraTests/"
successTestDir="./tests/success/"
failureTestDir="./tests/failure/"

##################################################

## TESTS ##


## Failure tests
echo
echo "${bold}Starting Failure test...${reset}"

    ### Syntax error tests ###
    echo
    echo "${bold}Starting Syntax error test...${reset}"
    
    for ((i=1; i<=3; i++))
    do
        echo
        echo "${green}Test n.$i${reset}"
        inputFile="$failureTestDir/syntax/prog0$i.txt"
        $start -i "$inputFile"
    done


    ### static-semantics-only tests ###
    echo
    echo "${bold}Starting static-semantics-only test...${reset}"

    echo
    echo "${green}With -ntc enabled${reset}"
    $start -ntc -i "$failureTestDir/static-semantics-only/prog01.txt"
    
    echo
    echo "${green}Without -ntc enabled${reset}"
    $start -i "$failureTestDir/static-semantics-only/prog01.txt"


    ### Static-semantics tests ### 
    echo
    echo "${bold}Starting Static-Semantics test...${reset}"

    for ((i=1; i<=4; i++))
    do
        echo
        echo "${green}Test n.$i${reset}"
        inputFile="$failureTestDir/static-semantics/prog0$i.txt"
        $start -i "$inputFile"
    done


    ### Dynamic-semantics tests ###
    echo
    echo "${bold}Starting Dynamic-Semantics test..."

    for ((i=1; i<=5; i++))
    do
        echo
        echo "${green}Test n.$i${reset}"
        inputFile="$failureTestDir/dynamic-semantics/prog0$i.txt"
        $start -i "$inputFile"
    done


## Success tests
echo
echo "${bold}Starting Success test...${reset}"

for ((i=1; i<=8; i++))
do
    echo
    echo "${green}Test n.$i${reset}"
    inputFile="$successTestDir/prog0$i.txt"
    $start -i "$inputFile"
done


## Extra tests
echo
echo "${bold}Starting Extra Success test...${reset}"

for ((i=9; i<=14; i++))
do
    echo
    echo "${green}Test n.$i${reset}"

    if [[ $i -gt 9 ]]
    then
        inputFile="$successTestDir/prog$i.txt"
    else 
        inputFile="$successTestDir/prog0$i.txt"
    fi

    $start -i "$inputFile"
done

echo
