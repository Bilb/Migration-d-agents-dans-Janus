import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.message.ObjectMessage;
import org.janusproject.kernel.status.Status;


public class ListenerMessageAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7143872746722384597L;

	@Override
	public Status live() {
		/*Iterable<Message> messages = getMessages();

		if(messages != null) {
			for (Message msg : getMessages()) {
				
				if(msg instanceof ObjectMessage) {
					ObjectMessage msg_s = (ObjectMessage) msg;
					Object obj = msg_s.getContent();
					if(obj instanceof String) {
						String content = (String) obj;
						//System.out.println("got message: " + (content)); //$NON-NLS-1$
					}
				}
			}
		}*/




		return super.live();
	}


}
