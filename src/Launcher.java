import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;


public class Launcher {

	public static void main(String[] args) {
		Kernel k = Kernels.get(true);
		
		k.launchLightAgent(new TestAgent());
	}
}
