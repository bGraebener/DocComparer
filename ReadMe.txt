Application Title: DocComparer
Author: Bastian Graebener
Date: 11.01.2018
Module: Object Oriented Programming
Lecturer: John Healy

1. Overview

The application 'DocComparer' compares text-based documents and calculates the 'Jaccard Index' for these documents. 
The 'Jaccard Index' indicates a magnitude of similarities shared by the documents.

The Runner class is the entry point of the application containing the main method.

The Menu class' responsibility is displaying the command line GUI to the user. Here the user has the opportunity to 
specify the number of documents the user wishes to compare and their location on the file system. 
Furthermore the Menu class keeps an instance of the Settings class. The user has the option to use the default values
provided by the Settings class or enter new values which override the default values.

The settings are then passed on to the MinHasher class. The MinHasher class' responsibility is to create a list of 
minHashes for all documents.


2. Features
