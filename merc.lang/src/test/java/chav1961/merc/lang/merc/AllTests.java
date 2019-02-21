package chav1961.merc.lang.merc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BasicMercProgramTest.class, MercCodeBuilderTest.class, MercCompilerTest.class, MercOptimizerTest.class,
		MercReposTest.class, MercTotalTest.class, VarDescriptorTest.class })
public class AllTests {

}
