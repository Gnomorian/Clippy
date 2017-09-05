package nz.burntoast.clippy;

import java.awt.MenuContainer;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

public class ClipboardControl extends Thread implements ClipboardOwner {
	// sets how many recent strings it can hold
	int history;
	// contains the recent strings on the clipboard
	public ArrayList<MenuItem> stringHistory = new ArrayList<MenuItem>();
	
	Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	/**
	 * @param history sets the amount of clipboard history to store
	 * @param tc 
	 */
	public ClipboardControl(int history) {
		this.history = history;
	}
	
	public void run() {
	    Transferable trans = sysClip.getContents(this);
	    regainOwnership(trans);
	    System.out.println("Listening to Clipboard...");
	    while(true) {}
	}
	
	/**
	  * Place a String on the clipboard, and make this class the
	  * owner of the Clipboard's contents.
	  */
	  public void setClipboardContents(Transferable transferable) {
		  sysClip.setContents(transferable, this);
	  }

	  /**
	   * get the current object on the Clipboard
	   * @return
	   */
	  public Transferable getClipboardContents() {
	    Transferable contents = sysClip.getContents(null);
	    return contents;
	  }
	/** use this to find when something new has been added to the Clipboard */
	@Override
	public void lostOwnership(Clipboard board, Transferable clip) {
		try {
			// give the other program writing to the Clipboard time to write it.
		    Thread.sleep(20);
		  } catch(Exception e) {
		    System.out.println("Exception: " + e);
		  }
		Transferable contents = sysClip.getContents(this);
		addNewSavedClip(contents);
		regainOwnership(contents);
	}
	
	/**
	 * works out if the contents of clipboard is image or text, then saves it to clipboard
	 * if the clipboard is over the cap, remove the oldest clip
	 * @param clipboardContents
	 */
	public void addNewSavedClip(Transferable clipboardContents) {
		// is the clipboard content text?
		// is it new?
		boolean hasTransferableText = (clipboardContents != null) && clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor);
	    if (hasTransferableText && isUnique(clipboardContents, stringHistory)) {
	    	// check how long the menu list will be
	    	// add the clipboard content to the Tray
	    	// if the menu list is longer than the cap, remove old clip
	    	MenuItem clip = App.tc.createTextMenuItem(clipboardContents);
	    	stringHistory.add(clip);
	    	if(stringHistory.size() == history) {
		    	  MenuContainer menu = stringHistory.get(0).getParent();
		    	  menu.remove(stringHistory.get(0));
		    	  stringHistory.remove(0);
		    }
	    	System.out.println("Clipboard Changed, adding new Item");
	    }
	}
	void regainOwnership(Transferable t) {
	    sysClip.setContents(t, this);
	}
	private boolean isUnique(Transferable clipboard, ArrayList<MenuItem> list) {
		String data = "";
		try {
			data = (String)clipboard.getTransferData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getLabel().equals(data))
				return false;
		}
		return true;
		
	}
}
