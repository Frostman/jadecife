import com.beust.jcommander.Parameter;

/**
 * @author slukjanov aka Frostman
 */
public class ServerOpts extends BaseOpts{
      @Parameter(names = "-dd", description = "Dd mode")
      public boolean dd = false;


}
