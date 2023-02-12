plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "top.ntutn"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.15.3")
    // https://mvnrepository.com/artifact/commons-codec/commons-codec
    implementation("commons-codec:commons-codec:1.15")
    // https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk18on
    implementation("org.bouncycastle:bcprov-jdk18on:1.72")
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}

tasks.register("buildRelease") {
    dependsOn(tasks.build)
    doLast {
        val installShellFile = File(project.buildDir, "distributions/hugow")
        installShellFile.takeIf { it.exists() }?.delete()
        installShellFile.writeText("""
            #!/usr/bin/env sh

            if ! [ -x "${'$'}(command -v curl)" ]; then
              echo "Please install curl to download the program"
              exit
            fi
            
            if ! [ -x "${'$'}(command -v java)" ]; then
              echo "Please install java to run the program"
              exit
            fi
            
            encrypt_command=hugo_encryptor_kt/bin/hugo_encryptor_kt
            if [ ! -f "${"$"}encrypt_command" ]; then
                echo Encryptor not found, downloading...
                
                zipname=hugo_encryptor_kt-$version
                curl -L -o ${"$"}zipname.zip https://github.com/zerofancy/hugo_encryptor_kt/releases/download/$version/${"$"}zipname.zip
                unzip -a ${"$"}zipname.zip
                rm ${"$"}zipname.zip
                mv ${"$"}zipname hugo_encryptor_kt
                hugo_encryptor_kt/bin/hugo_encryptor_kt install
            fi

            hugo "${'$'}@"
            hugo_encryptor_kt/bin/hugo_encryptor_kt
        """.trimIndent())
    }
}