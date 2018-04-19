# FileChooser

This app is a simple prototype to test androids build in file chooser.

This app has two use-cases:

* Save a file on a folder you choose
* Open and display a file you picked before

## Problem

After you picked a large file, the screen is frozen for a few seconds on the file-chooser.
Although the read process is asynchronous.
The expected behaviour is that after you choose a file, the display activity should be shown.
On this screen you should see a progressbar. After the reading is done, the progressbar should be gone and the read text should be displayed.

To resolve this issue you can call `displayText()` like this: `textview.post{ displayText() }`

