package billywangwang.main.game.undo;

import java.util.LinkedList;

public class UndoQueue {
	
	private LinkedList<UndoEvent> events = new LinkedList<UndoEvent>();
	
	public void undo(){
		if(events.size() > 0){
			events.getLast().undo();
			events.remove(events.size() - 1);
		}
	}
	
	public void add(UndoEvent e){
		events.add(e);
	}

}
