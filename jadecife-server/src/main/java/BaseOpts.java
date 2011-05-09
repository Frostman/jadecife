import com.beust.jcommander.Parameter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author slukjanov aka Frostman
 */
public class BaseOpts {
    @Parameter
    public List<String> parameters = Lists.newArrayList();

    @Parameter(names = {"-log", "-verbose"}, description = "Level of verbosity")
    public Integer verbose = 1;

    @Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
    public String groups;

    @Parameter(names = "-debug", description = "Debug mode")
    public boolean debug = false;

}
