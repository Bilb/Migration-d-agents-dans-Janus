import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;


public class TestAgent extends Agent{
	
	@Override
	public Status live() {
		System.out.println("LIVE");
		return super.live();
	}

	
}
