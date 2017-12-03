package groovy.maintance

/**
 * Created by david on 12/3/17.
 */
class PackageTest extends GroovyTestCase {

    void testMatchFile() {
        PackageUtil packageUtil = new PackageUtil()
        packageUtil.matchMakeFile()
    }
}
