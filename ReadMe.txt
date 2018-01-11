Application Title: DocComparer
Author: Bastian Graebener
Date: 11.01.2018
Module: Object Oriented Programming
Lecturer: John Healy

GitHub repository: https://github.com/bGraebener/DocComparer.git

1. Overview

The application 'DocComparer' compares text-based documents and calculates the 'Jaccard Index' for these documents.
The 'Jaccard Index' indicates a magnitude of similarities shared by the documents.

The Runner class is the entry point of the application containing the main method.

The Menu class' responsibility is displaying the command line GUI to the user. Here the user has the opportunity to
specify the number of documents the user wishes to compare and their location on the file system.
Furthermore the Menu class keeps an instance of the Settings class. The user has the option to use the default values
provided by the Settings class or enter new values which override the default values.

The settings are then passed on to the MinHasher class. The MinHasher class' responsibility is to create a list of
minHashes for all documents. In order to calculate the minHashes all documents are parsed using FileParser instances.
The Consumer then creates Tasks to process the Shingles created by the FileParsers.
The MinHasher provides a thread safe queue to hold Shingles and a thread safe map which holds a List of minHashes for every document.
These are passed to the FileParsers and the Consumer instances.
This means that the parsing of the documents and the calculating of the minHashes can happen in parallel. 

The FileParser is a specification of a Parser. The Parser interface can be implemented by all classes that parse a source.
The FileParser specifies that the source to be parsed is a text-based file. Other sources could be JSON, XML or HTML. The FileParser
reads the file line by line and creates Shingles in the user specified size. It puts these into a thread safe queue for
concurrent processing by the Consumer and Tasks.

The Consumer provides the List of random numbers as well as an instance of a CountDownLatch to the Task instances. The random numbers
are used to calculate the minHashes for a single Shingle by performing a XOR operation on the Shingles' hash value.
The CountDownLatch keeps track of Tasks that finish their operations.

A Tasks' responsibility is to create a List of minHashes for a single Shingle. It then compares the minHashes for the current Shingle
with the minHashes from the previous Shingles and adds the smallest to the list of minHashes for the document.

The JaccardCalculator is a specification of a Calculator. A Calculator is an interface all classes should implement that perform
some form of calculation. The JaccardCalculator specifies that a Jaccard Index is being calculated by that class.

Shingles are numeric representations of a String of an arbitrary number of words or characters. PoisonShingles are a specialisation
of a Shingle to indicate the end of a document and no more Shingles are put onto the queue.


2. Extra Features

	a. Support for the comparison of a main document against a user defined number of other documents
	b. user specified shingle size, number of threads processing the shingles and
	   amount of random numbers to calculate the minHashes


3. Compiling and Executing the application
	
	a. For compiling the source files (from inside the source folder)
		to compile:
		javac ./ie/gmit/sw/*.*
	
		to run the application:
		java ie.gmit.sw.Runner
		
	b. For executing the jar
		java -cp ./oop.jar ie.gmit.sw.Runner
		
		  
