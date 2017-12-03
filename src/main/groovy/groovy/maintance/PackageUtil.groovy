package groovy.maintance

import groovy.io.FileType

/**
 * Created by david on 12/3/17.
 */

class PackageUtil {
    // the top-level groovy project folder
    def projectFolder = "/opt/github/groovy/"
    def newline = System.getProperty("line.separator")

    void matchMakeFile() {
        def packages = [:]

        // gather all the packages that are in core to check against for the subprojects
        def pack = new File(projectFolder + "/src/main/")
        pack.eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith(".java") || file.name.endsWith(".groovy")) {
                def lines = file.readLines()
                lines.each { line ->
                    if (line.startsWith("package")) {
                        packages.put(line.split()[1].replace(";", ""), [])
                    }
                }
            }
        }
        // check all the subprojects see if they match the core for same package
        def sub = new File(projectFolder + "/subprojects/")
        sub.eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith(".java") || file.name.endsWith(".groovy")) {
                def lines = file.readLines()
                lines.each { line ->
                    if (line.startsWith("package")) {
                        def key = line.split()[1].replace(";", "")
                        def list = packages.get(key)
                        if (list != null) {
                            def fileName = file.toString().replace(projectFolder,'')
                            list.add(fileName)
                            packages.put(key, list)
                        }
                    }
                }
            }
        }
        // now go through the map and if the key has a list with items then we had a match print it in the file
        File file = new File("./package_results.txt")
        if(file.exists()){
            file.deleteDir()
        }
        file << "The following is a list of subproject java and groovy files with the same main src package$newline"
        file << newline
        packages.entrySet().each { keys ->
            keys.each { key ->
                if (key.value != null && key.value.size() > 0) {
                    file << newline
                    file << "Matched Package " + key.key + newline
                    key.value.each { value ->
                        file << value.toString() + newline
                    }
                }
            }
        }
        println file.text
    }

}
