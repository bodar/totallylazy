import com.googlecode.jcompilo.*;
import com.googlecode.jcompilo.convention.BuildConvention;

public class Build extends BuildConvention {
    @Override public String group() { return "com.googlecode." + artifact(); }
    @Override public String artifact() { return "totallylazy"; }
}
