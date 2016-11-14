package com.ilargia.games.entitas.codeGenerator;


import com.ilargia.games.entitas.codeGenerator.interfaces.*;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CodeGenerator {

    public static final String DEFAULT_POOL_NAME = "Pool";
    public static final String COMPONENT_SUFFIX = "Component";

    public static final String DEFAULT_COMPONENT_LOOKUP_TAG = "ComponentIds";
    public static final String AUTO_GENERATED_HEADER_FORMAT = String.join("\n",
            "//------------------------------------------------------------------------------",
            "// <auto-generated>",
            "//     This code was generated by {0}.",
            "//",
            "//     Changes to this file may cause incorrect behavior and will be lost if",
            "//             public static CodeGenFile[] Generate(ICodeGeneratorDataProvider provider, String directory, ICodeGenerator[] codeGthe code is regenerated.",
            "// </auto-generated>",
            "//------------------------------------------------------------------------------"
    );


    public static CodeGenFile[] generate(ICodeGeneratorDataProvider provider, String destinyDirectory, ICodeGenerator[] codeGenerators) throws IOException {

        ArrayList<CodeGenFile> generatedFiles = new ArrayList<CodeGenFile>();
        ComponentInfo[] componentInfos = provider.componentInfos();
        List<JavaClassSource> files = new ArrayList<>();


        for (int i = 0; i < codeGenerators.length; i++) {

            if (codeGenerators[i] instanceof IPoolCodeGenerator) {
                IPoolCodeGenerator generator = (IPoolCodeGenerator) codeGenerators[i];
                files.addAll(generator.generate(provider.poolNames(),"com.pruebas.entitas"));

            }

            if (codeGenerators[i] instanceof IComponentCodeGenerator) {
                IComponentCodeGenerator generator = (IComponentCodeGenerator) codeGenerators[i];
                files.addAll(generator.generate(componentInfos,"com.pruebas.entitas"));

            }

            if (codeGenerators[i] instanceof IBlueprintsCodeGenerator) {
                IBlueprintsCodeGenerator generator = (IBlueprintsCodeGenerator) codeGenerators[i];
                files.addAll(generator.generate(provider.blueprintNames(),"com.pruebas.entitas"));

            }
            writeFiles(destinyDirectory, files);
        }


        return (CodeGenFile[]) generatedFiles.toArray();

    }

    static void writeFiles(String directoryName, List<JavaClassSource> files) {

        File directory = new File(String.valueOf(directoryName));
        if (!directory.exists()) {
            directory.mkdir();
        }

        files.stream().forEach((file) -> {
            String fileName = directory.getPath() + file.getName() + ".java";
            String header = String.format(AUTO_GENERATED_HEADER_FORMAT, "CodeGenerator");
            write(fileName, header + file);

        });

    }

    public static void write(String fileName, String content) {
        File file = new File(fileName);
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String capitalize(final String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

}

