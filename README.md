# Universal Code Interpreter

## What is it?
The goal of this project is to create a simple way to specify the syntax and desired outputs for simple programming languages, such as assembly programming languages. The main use case I envision is for easier programming of Minecraft redstone computers, though I'm sure this program may have other use cases.

## What can is do?
This program reads all folders found in the ```syntax_profiles``` folder and attempts to read them as valid syntax profiles. These profiles can contain a ```READEME.txt``` file, a ```config.txt``` file which specifies certain parameters, and two files which specify a syntax for assembling and compiling a program.

When the program start, it will prompt the users with all valid syntax profiles, allowing the user to select a program, then select whether they would like to compile a file, assemble a file, or both. Then, the user will be prompted to input the file they would like to compile and/or assemble. 

The program will then output the resulting compiled and/or assembled files, or print errors if the inputted file contains errors according to the specified syntax in the syntax profile.

This program also contains a debug mode for debugging syntax profiles. This allows users that are developing syntax profiles to address errors in their profiles without distracting from the main user experience. This debug mode is enabled by inputting ```-1``` in the main menu of the program.

This is a test for git bash (secondary test)