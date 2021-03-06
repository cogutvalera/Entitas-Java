package com.ilargia.games.entitas.codeGenerator;


import com.ilargia.games.entitas.codeGenerator.data.ComponentInfo;
import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;

import java.io.*;
import java.util.*;

public class CodeGeneratorOld {

    public static final String COMPONENT_SUFFIX = "Component";
    public static final String DEFAULT_COMPONENT_LOOKUP_TAG = "ComponentsLookup";
    public static final String AUTO_GENERATED_HEADER_FORMAT = String.join("\n",
            "---------------------------------------------------------------------------",
            " '<auto-generated>'  This code was generated by CodeGeneratorApp.",
            "---------------------------------------------------------------------------"
    );

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

    public static Map<String, List<ComponentInfo>> generateMap(List<ComponentInfo> componentInfos) {
        Map<String, List<ComponentInfo>> poolsComponents = new HashMap<>();
        componentInfos.sort((c1, c2) -> c1.typeName.compareTo(c2.typeName));

        for (ComponentInfo info : componentInfos) {
            for (String poolName : info.contexts) {
                if (!poolsComponents.containsKey(poolName)) {
                    poolsComponents.put(poolName, new ArrayList<>());
                }
                List<ComponentInfo> list = poolsComponents.get(poolName);
                list.add(info);
            }
        }

        for (List<ComponentInfo> infos : poolsComponents.values()) {
            int index = 0;
            for (ComponentInfo info : infos) {
                info.index = index++;
                info.totalComponents = infos.size();
            }
        }
        return poolsComponents;

    }

    public static void toFile(JavaClassSource javaClass, File srcFolder) {
        File f = srcFolder;
        String[] parts = javaClass.getPackage().split("\\.");

        try {
            if (!srcFolder.getAbsolutePath().endsWith(parts[parts.length - 1])) {
                f = new File(f, parts[parts.length - 1]);
                createParentDirs(f);
            }
            f = new File(f, javaClass.getName() + ".java");
            write(f, javaClass.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createParentDirs(File file) throws IOException {
        if (file != null) {
            File parent = file.getCanonicalFile();
            if (parent == null) {
                return;
            }
            parent.mkdirs();

            if (parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Unable to create parent directories of " + file);
            }
        }
    }

    public static void write(File file, String content) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getAbsolutePath()), "utf-8"))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public List<CodeGenFile> generate(ICodeGeneratorDataProvider provider, String destinyDirectory, List<ICodeGenerator> codeGenerators) {

        List<String> scrDir = new ArrayList<String>() {{
            add("main/java");
            add("test/java");
            add("main\\java");
            add("test\\java");
            add("src");
        }};
        Optional<String> sourcePackage = scrDir.stream().filter((base) -> destinyDirectory.lastIndexOf(base) != -1)
                .map((base) -> destinyDirectory.substring(destinyDirectory.lastIndexOf(base) + base.length() + 1))
                .map((base) -> base.replaceAll("/", ".").replaceAll("\\\\", "."))
                .findFirst();

        ArrayList<CodeGenFile> generatedFiles = new ArrayList<CodeGenFile>();
        List<JavaClassSource> files = new ArrayList<>();

//        if (sourcePackage.isPresent()) {
//            for (ICodeGenerator generator : generators) {
//                if (generator instanceof IContextCodeGenerator) {
//                    for (JavaClassSource javaClassSource : ((IContextCodeGenerator) generator).generate(provider.poolNames(), sourcePackage.get())) {
//                        files.add(javaClassSource);
//                        generatedFiles.add(new CodeGenFile(javaClassSource.getCanonicalName(), javaClassSource, generator.getClass().getName()));
//                    }
//
//                }
//                if (generator instanceof IComponentCodeGenerator) {
//                    for (JavaClassSource javaClassSource : ((IComponentCodeGenerator) generator).generate(provider.componentInfos(), sourcePackage.get())) {
//                        files.add(javaClassSource);
//                        generatedFiles.add(new CodeGenFile(javaClassSource.getCanonicalName(), javaClassSource, generator.getClass().getName()));
//                    }
//
//                }
//                if (generator instanceof IBlueprintsCodeGenerator) {
//                    for (JavaClassSource javaClassSource : ((IBlueprintsCodeGenerator) generator).generate(provider.blueprintNames(), sourcePackage.get())) {
//                        files.add(javaClassSource);
//                        generatedFiles.add(new CodeGenFile(javaClassSource.getCanonicalName(), javaClassSource, generator.getClass().getName()));
//                    }
//
//                }
//            }
//        }

        CodeGenFile entitas = generatedFiles.get(generatedFiles.size() - 1);
        for (CodeGenFile generatedFile : generatedFiles) {
            if (!generatedFile.fileName.endsWith("Matcher") && !entitas.fileContent.getPackage().equals(generatedFile.fileContent.getPackage())) {
                entitas.fileContent.addImport(generatedFile.fileName);
            }
        }
        writeFiles(destinyDirectory, files);
        return generatedFiles;

    }

    public void writeFiles(String directoryName, List<JavaClassSource> files) {
        File directory = new File(String.valueOf(directoryName));
        if (!directory.exists()) {
            directory.mkdir();
        }

        files.stream().forEach((file) -> {
            JavaDocSource javaDoc = file.getJavaDoc();
            javaDoc.setFullText(String.format(AUTO_GENERATED_HEADER_FORMAT, "CodeGenerator"));
            toFile(file, directory);

        });

    }
}


