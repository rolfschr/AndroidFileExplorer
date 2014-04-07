AndroidFileExplorer
===================

Android File Explorer



Install
=======

* To setup this app in Eclipse (ADT) install the EGit plugin first.
* Clone this repo (see link on github page of this repo) using the Git perspective in Eclipse.
* In the Git perspective, right click on cloned repo -> Import Projects... -> Use the New Project Wizard -> Android Project from Existing Code -> Go to and select ~/git/AndroidFileExplorer and import

(Alternatively, checkout the repo [using command line git] and directly use the New Project Wizard to import.)

ChangeLog
=========

[07Apr2014]
* Add context action bar to rename/delete files. Remove context menu.

[01Apr2014]
* Make it possible to change colors of dirs and files

[28Mar2014]
* Added rename file dialog

[27Mar2014]
* Make context menu when long pressing a file (instead of context action menu)

[25Mar2014]
* Open unknown files with browser by default
* Added preferences.xml

[24Mar2014]
* Added context menu and possibility to delete files

[21Mar2014]
* Fixed Activity state reset on rotate
* Added possibility to open files with appropriate app
* Set up Async Task to load directory entries in background

[20Mar2014]
* Back key goes one up in directory structure
* Implemented view holder pattern for directory entry list
* Implemented directory traversal

ToDo
====

* Check for external media in file system tree (?)
* ~~Add a test to check whether chosen directory and normale file colors equal (?)~~
* Set up StrictMode config / How to use this?
* ~~Use ViewHolders/Background thread for the dir list (?)~~
* ~~Test back key when dialog is active~~
* Abreviate directory length with dots when too long (?)
* Remember list view position to make go-back-functionality more intuitive
* ~~Remember directory when phone gets rotated~~
* Add icons to folders/files (?)
* ~~Declare Listeners in XML layout file (?)~~
* ~~Make context menu only visible for files~~
* ~~Check read rights before opening a file~~
* Change color of selected view item
* ~~Test rotation and fix crash~~

Questions
=========

* Should I use a ListFragment for the directory list in order to store parent (=previous) directories in the back stack? / When does it make sense to use a fragment in general?
* ~~How to open umlaut files?~~
